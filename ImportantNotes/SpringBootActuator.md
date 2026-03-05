# Spring Boot Actuator – Complete Beginner Guide

## 1. Introduction

**Spring Boot Actuator** is a module of Spring Boot that helps you **monitor and manage your application in production**.

It provides **built-in REST endpoints** that give information about the application's:

* Health
* Metrics
* Beans
* Environment
* Configuration
* Application status
* HTTP mappings
* Thread dumps
* Logging configuration

Actuator is mainly used by:

* Developers
* DevOps Engineers
* System Administrators
* Monitoring tools like **Prometheus**, **Grafana**, **New Relic**, etc.

These endpoints help in **observability**, **monitoring**, and **debugging** of applications.

---

# 2. Why Spring Boot Actuator is Important

In real production systems, you need to know:

* Is the application running?
* Is the database connected?
* How much memory is being used?
* How many HTTP requests are happening?
* What beans are loaded in Spring?

Spring Boot Actuator provides this **without writing any custom code**.

---

# 3. Actuator Architecture

Spring Boot Actuator works through **Endpoints**.

Endpoints expose internal application information using:

* **HTTP REST APIs**
* **JMX Beans**

Common endpoint format:

```
http://localhost:8080/actuator/{endpoint}
```

Example:

```
http://localhost:8080/actuator/health
```

---

# 4. Adding Dependency

## Maven

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

## Gradle

```gradle
implementation 'org.springframework.boot:spring-boot-starter-actuator'
```

Once added, Spring Boot automatically enables **basic endpoints**.

---

# 5. Default Endpoints

After adding Actuator, the base endpoint becomes:

```
/actuator
```

Example:

```
http://localhost:8080/actuator
```

This lists all available actuator endpoints.

---

# 6. Important Actuator Endpoints

## 1. Health Endpoint

```
/actuator/health
```

Purpose:

Checks if the application is running properly.

Example Response:

```json
{
  "status": "UP"
}
```

Health also checks:

* Database
* Disk space
* External services

---

## 2. Info Endpoint

```
/actuator/info
```

Displays custom application information.

Example:

```json
{
 "app":{
   "name":"Job Application API",
   "version":"1.0.0"
 }
}
```

---

## 3. Beans Endpoint

```
/actuator/beans
```

Shows all **Spring Beans loaded in the application context**.

Useful for debugging dependency injection.

Example Response:

```json
{
 "contexts":{
   "application":{
     "beans":{
       "jobController":{},
       "jobService":{}
     }
   }
 }
}
```

---

## 4. Environment Endpoint

```
/actuator/env
```

Displays all **environment properties**.

Includes:

* application.properties
* system properties
* OS environment variables

Example:

```
JAVA_HOME
spring.datasource.url
server.port
```

---

## 5. Metrics Endpoint

```
/actuator/metrics
```

Displays application metrics.

Examples:

* JVM memory
* CPU usage
* HTTP requests
* Garbage collection

Example metric query:

```
/actuator/metrics/jvm.memory.used
```

Example response:

```json
{
 "name":"jvm.memory.used",
 "measurements":[
   {
     "value": 48290304
   }
 ]
}
```

---

## 6. Mappings Endpoint

```
/actuator/mappings
```

Shows all HTTP endpoints available in the application.

Example:

```
GET /jobPosts
POST /jobPosts/add
```

Useful for debugging controllers.

---

## 7. Loggers Endpoint

```
/actuator/loggers
```

Allows viewing and modifying logging levels at runtime.

Example:

```
/actuator/loggers/root
```

Change logging level dynamically:

```
POST /actuator/loggers/root
```

Body:

```json
{
 "configuredLevel":"DEBUG"
}
```

---

## 8. Thread Dump Endpoint

```
/actuator/threaddump
```

Displays all running threads in JVM.

Useful for debugging:

* Deadlocks
* Performance issues

---

## 9. Heap Dump Endpoint

```
/actuator/heapdump
```

Downloads JVM heap memory dump.

Useful for memory leak analysis.

---

## 10. Shutdown Endpoint

```
/actuator/shutdown
```

Allows remotely shutting down the application.

Disabled by default for security reasons.

---

# 7. Enabling All Endpoints

By default, only a few endpoints are exposed.

Enable all endpoints in **application.yml**:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

Now all actuator endpoints become accessible.

---

# 8. Changing Actuator Base Path

Default path:

```
/actuator
```

You can change it:

```yaml
management:
  endpoints:
    web:
      base-path: /manage
```

Now endpoints become:

```
/manage/health
/manage/metrics
```

---

# 9. Running Actuator on Different Port

Sometimes monitoring tools require a separate port.

Configuration:

```yaml
management:
  server:
    port: 9000
```

Application runs on:

```
8080
```

Actuator runs on:

```
9000
```

Example:

```
http://localhost:9000/actuator/health
```

---

# 10. Health Details Configuration

By default, health details are hidden.

Enable them:

```yaml
management:
  endpoint:
    health:
      show-details: always
```

Response becomes:

```json
{
 "status":"UP",
 "components":{
   "db":{
     "status":"UP"
   },
   "diskSpace":{
     "status":"UP"
   }
 }
}
```

---

# 11. Securing Actuator Endpoints

Actuator endpoints expose sensitive information.

Always secure them using Spring Security.

Example configuration:

```java
http
  .authorizeHttpRequests(auth -> auth
      .requestMatchers("/actuator/health").permitAll()
      .requestMatchers("/actuator/**").hasRole("ADMIN")
  )
```

This allows:

* Public access to `/health`
* Admin access to other endpoints

---

# 12. Custom Actuator Endpoint

You can create your own actuator endpoint.

Example:

```java
@Component
@Endpoint(id = "customInfo")
public class CustomEndpoint {

    @ReadOperation
    public String info(){
        return "Application Running Successfully";
    }
}
```

Access it via:

```
/actuator/customInfo
```

---

# 13. Actuator with Monitoring Tools

Actuator integrates with:

* Prometheus
* Grafana
* Elastic Stack
* Datadog
* New Relic

Example:

```
/actuator/prometheus
```

Prometheus scrapes metrics from this endpoint.

---

# 14. Real World Use Cases

Actuator is used for:

Monitoring production systems

Checking health of microservices

Auto scaling in Kubernetes

Application performance monitoring

Debugging production issues

Integrating with monitoring tools

---

# 15. Best Practices

Never expose all endpoints publicly

Always secure Actuator with authentication

Run Actuator on a separate port

Allow only monitoring tools to access Actuator

Avoid enabling shutdown endpoint in production

---

# 16. Summary

Spring Boot Actuator provides powerful monitoring features with minimal configuration.

Key Benefits:

* Production monitoring
* Application health checks
* JVM metrics
* Debugging support
* Integration with monitoring tools

Most commonly used endpoints:

```
/actuator/health
/actuator/info
/actuator/metrics
/actuator/beans
/actuator/env
/actuator/mappings
/actuator/loggers
```

Spring Boot Actuator is an essential tool for building **observable and production-ready applications**.
