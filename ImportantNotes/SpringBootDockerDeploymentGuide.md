# Spring Boot Docker Deployment Guide (Beginner to Production)

This guide explains:

1. Installing Docker on **Windows, Mac, Linux**
2. Getting **OpenJDK inside Docker**
3. Packaging **Spring Boot as JAR and WAR**
4. Running Spring Boot inside Docker container
5. Creating Docker images using **docker commit**
6. Publishing ports (`-p 8081:8081`)
7. Deploying using **Dockerfile**
8. Running with **docker-compose**
9. Pushing images to **Docker Hub**
10. Running **MySQL + PostgreSQL failover setup**
11. Connecting Docker container to **local database**
12. CI/CD using **Jenkins / GitHub Actions**
13. **Kubernetes deployment**
14. **Docker multi-stage builds**
15. **Cleaning Docker storage**

---

# 1. Architecture Overview

### Simple Docker Architecture

```
                ┌────────────────────────────┐
                │        Client Machine      │
                │  Browser / Postman         │
                └─────────────┬──────────────┘
                              │
                              │ HTTP Request
                              ▼
                    ┌──────────────────┐
                    │ Docker Container │
                    │ Spring Boot App  │
                    │ Port 8081        │
                    └─────────┬────────┘
                              │
              ┌───────────────┴───────────────┐
              │                               │
      ┌──────────────┐                ┌───────────────┐
      │ MySQL DB     │                │ PostgreSQL DB │
      │ Primary DB   │                │ Failover DB   │
      └──────────────┘                └───────────────┘
```

---

# 2. Install Docker

## Windows Installation

1 Download Docker Desktop

[https://www.docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop)

Requirements

* Windows 10/11
* WSL2 enabled

Install Docker Desktop.

Verify installation

```bash
docker --version
docker run hello-world
```

---

## Mac Installation

Download Docker Desktop for Mac.

[https://www.docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop)

Install and start Docker.

Verify

```bash
docker --version
docker run hello-world
```

---

## Linux Installation (Ubuntu Example)

Update packages

```bash
sudo apt update
```

Install Docker

```bash
sudo apt install docker.io -y
```

Start docker

```bash
sudo systemctl start docker
```

Enable at boot

```bash
sudo systemctl enable docker
```

Verify

```bash
docker --version
```

---

# 3. Pull OpenJDK Image in Docker

Pull official OpenJDK image

```bash
docker pull openjdk:17
```

Check images

```bash
docker images
```

---

# 4. Start Docker Container with OpenJDK

```bash
docker run -it openjdk:17 /bin/bash
```

Now container terminal opens.

Inside container create temp folder

```bash
mkdir /temp
```

---

# 5. Package Spring Boot Application

You can build using **Maven or Gradle**.

---

# Maven Build

Build JAR

```bash
mvn clean package
```

Output

```
target/app.jar
```

Build WAR

```bash
mvn clean package -Dpackaging=war
```

Output

```
target/app.war
```

---

# Gradle Build

Build jar

```bash
gradle clean build
```

Output

```
build/libs/app.jar
```

Build war

```bash
gradle clean war
```

---

# 6. Copy JAR/WAR into Docker Container

First check container id

```bash
docker ps
```

Copy file

```bash
docker cp target/rest-api.jar containerId:/temp/
```

Verify inside container

```bash
ls /temp
```

---

# 7. Commit Docker Container as Image

Now create image.

Example command

```bash
docker commit \
--change='CMD ["java","-jar","/temp/rest-api.jar"]' \
containerId myrepo/springboot-api:1.0
```

Check images

```bash
docker images
```

---

# 8. Run Docker Container with Port Mapping

Run container

```bash
docker run -p 8081:8081 myrepo/springboot-api:1.0
```

Explanation

```
-p 8081:8081

Machine Port : Container Port
```

Now application accessible

```
http://localhost:8081
```

---

# 9. Deploy using Dockerfile (Recommended)

Create file

```
Dockerfile
```

Example

```dockerfile
FROM openjdk:17

WORKDIR /app

COPY target/rest-api.jar app.jar

EXPOSE 8081

CMD ["java","-jar","app.jar"]
```

Build image

```bash
docker build -t springboot-app .
```

Run container

```bash
docker run -p 8081:8081 springboot-app
```

---

# 10. Multi Stage Docker Build (Production)

```
┌─────────────┐
│ Build Stage │
│ Maven Build │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ Runtime     │
│ OpenJDK     │
└─────────────┘
```

Dockerfile

```dockerfile
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17

WORKDIR /app
COPY --from=builder /build/target/app.jar app.jar

EXPOSE 8081

CMD ["java","-jar","app.jar"]
```

Build

```bash
docker build -t springboot-prod .
```

---

# 11. Docker Compose Setup

Create

```
compose.yaml
```

Example

```yaml
version: "3"

services:

  app:
    image: springboot-app
    ports:
      - "8081:8081"
    depends_on:
      - mysql
      - postgres

  mysql:
    image: mysql:8
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: appdb
    ports:
      - "3306:3306"

  postgres:
    image: postgres
    environment:
      POSTGRES_PASSWORD: root
      POSTGRES_DB: backupdb
    ports:
      - "5432:5432"
```

Run

```bash
docker compose up
```

---

# 12. Connect Docker App to Local Database

Spring Boot config

```
application.properties
```

MySQL Primary

```
spring.datasource.url=jdbc:mysql://host.docker.internal:3306/appdb
spring.datasource.username=root
spring.datasource.password=root
```

PostgreSQL Failover

```
spring.second-datasource.url=jdbc:postgresql://host.docker.internal:5432/backupdb
spring.second-datasource.username=postgres
spring.second-datasource.password=root
```

`host.docker.internal` allows Docker container to connect to **host machine database**.

---

# 13. Push Image to Docker Hub

Login

```bash
docker login
```

Tag image

```bash
docker tag springboot-app username/springboot-app:1.0
```

Push image

```bash
docker push username/springboot-app:1.0
```

Now anyone can run

```bash
docker pull username/springboot-app:1.0
```

---

# 14. CI/CD with GitHub Actions

Example

```
.github/workflows/docker.yml
```

```yaml
name: Docker Build

on: [push]

jobs:

 build:

  runs-on: ubuntu-latest

  steps:

  - uses: actions/checkout@v3

  - name: Build Image
    run: docker build -t myapp .

  - name: Push Image
    run: |
      docker login -u ${{secrets.USER}} -p ${{secrets.PASS}}
      docker push myrepo/myapp
```

---

# Jenkins CI/CD Pipeline

```
pipeline {

 agent any

 stages {

  stage('Build') {
   steps {
    sh 'mvn clean package'
   }
  }

  stage('Docker Build') {
   steps {
    sh 'docker build -t springboot-app .'
   }
  }

  stage('Docker Push') {
   steps {
    sh 'docker push repo/springboot-app'
   }
  }

 }
}
```

---

# 15. Kubernetes Deployment

Deployment YAML

```yaml
apiVersion: apps/v1
kind: Deployment

metadata:
  name: springboot-app

spec:
  replicas: 2

  template:
    spec:
      containers:
        - name: springboot
          image: repo/springboot-app
          ports:
            - containerPort: 8081
```

Service YAML

```yaml
apiVersion: v1
kind: Service
kind: NodePort

metadata:
  name: springboot-service

spec:
  selector:
    app: springboot

  ports:
    - port: 8081
      targetPort: 8081
      nodePort: 30007
```

Deploy

```bash
kubectl apply -f deployment.yaml
```

---

# 16. Docker Storage Cleanup

Remove stopped containers

```bash
docker container prune
```

Remove unused images

```bash
docker image prune
```

Remove volumes

```bash
docker volume prune
```

Remove everything

```bash
docker system prune -a
```

---

# 17. Delete Everything From Docker

```bash
docker stop $(docker ps -aq)

docker rm $(docker ps -aq)

docker rmi $(docker images -q)

docker volume rm $(docker volume ls -q)
```

---

# 18. Clear Docker Cache

```bash
docker builder prune
```

---

# 19. Best Production Architecture

```
               ┌───────────────┐
               │   Load Balancer│
               └──────┬────────┘
                      │
        ┌─────────────┴─────────────┐
        │ Kubernetes Cluster        │
        │                           │
        │  ┌───────────────┐        │
        │  │ Spring Boot   │        │
        │  │ Container     │        │
        │  └───────────────┘        │
        │  ┌───────────────┐        │
        │  │ Spring Boot   │        │
        │  │ Container     │        │
        │  └───────────────┘        │
        │                           │
        └─────────────┬─────────────┘
                      │
       ┌──────────────┴──────────────┐
       │                              │
 ┌──────────────┐             ┌──────────────┐
 │ MySQL DB     │             │ PostgreSQL DB│
 │ Primary      │             │ Failover     │
 └──────────────┘             └──────────────┘
```

---

# Conclusion

You learned

* Docker installation
* Running Spring Boot in Docker
* Dockerfile deployment
* Docker Compose
* Multi-stage builds
* MySQL + PostgreSQL setup
* CI/CD pipelines
* Kubernetes deployment
* Docker storage cleanup

---