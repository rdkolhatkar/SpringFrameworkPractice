
---

# 📘 **Server-Side Template Engines in Spring Boot — Thymeleaf vs JSP**

A complete guide for beginners and junior developers

---

# 🏷️ **1. What Is a Server-Side Template Engine?**

A **server-side template engine** helps generate **dynamic HTML** pages on the server.

Workflow:

```
Controller --> Sends data (Model) --> Template Engine --> Generates HTML --> Browser
```

Example: Controller sending data

```java
model.addAttribute("name", "Mukesh");
return "home";
```

HTML template displays:

```html
<p>Hello, Mukesh!</p>
```

Spring Boot supports two popular template engines:

* **Thymeleaf** (modern)
* **JSP** (legacy but still used)

---

# 🟢 **2. Thymeleaf — Complete Explanation**

Thymeleaf is a **modern, HTML5-friendly, server-side template engine** designed for Spring Boot.

## ✔ Why Thymeleaf?

* Works naturally with HTML → designers can open templates directly in browser
* No JSP complexities
* Spring Boot auto-configures it
* Better expressions, reusable fragments, form binding, security

---

## 🧩 **Thymeleaf Project Setup in Spring Boot**

### **Add dependency**

```groovy
implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
```

### **Default folder structure**

```
src/main/resources/templates/*.html
```

---

# 🟦 **3. Thymeleaf Basic Syntax**

### **Show value**

```html
<p th:text="${name}"></p>
```

Output:

```
Hello Mukesh
```

---

### **Iterate over a list**

```html
<ul>
  <li th:each="emp : ${employees}" th:text="${emp.name}"></li>
</ul>
```

---

### **Conditional Rendering**

```html
<p th:if="${age >= 18}">Eligible</p>
<p th:unless="${age >= 18}">Not Eligible</p>
```

---

### **Setting HTML attributes**

```html
<a th:href="@{/home}">Home</a>
```

---

### **Inline Expression**

```html
<p>Welcome [[${name}]]!</p>
```

---

# 🟩 **4. Thymeleaf Advanced Syntax**

### **Fragments (Reusable components)**

**header.html**

```html
<div th:fragment="header">
  <h2>Welcome to My App</h2>
</div>
```

**Include in page**

```html
<div th:replace="header :: header"></div>
```

---

### **Thymeleaf Form Handling**

Controller:

```java
model.addAttribute("employee", new Employee());
return "form";
```

HTML:

```html
<form th:action="@{/save}" th:object="${employee}" method="post">
  <input th:field="*{name}"/>
  <input th:field="*{email}"/>
  <button type="submit">Save</button>
</form>
```

---

### **Displaying Error Messages**

```html
<span th:if="${#fields.hasErrors('name')}"
      th:errors="*{name}"></span>
```

---

### **Security (XSS protection)**

Thymeleaf *automatically escapes* script tags.

---

# 🟧 **5. How Thymeleaf Integrates with Spring Boot**

Thymeleaf uses the **Spring ViewResolver** system.

Flow:

1. Controller returns view name: `"home"`
2. Spring finds `/templates/home.html`
3. Thymeleaf renders model data
4. Browser receives final HTML

No XML, no config files needed.

---

---

# 🔵 **6. JSP (JavaServer Pages) — Complete Explanation**

JSP is an **older template engine** that allows Java code inside HTML.

Although not very modern, some legacy applications still use it.

---

## ✔ Why JSP?

* Works well with traditional Java web apps
* Simple to use for small dynamic pages

But NOT recommended for new Spring Boot applications.

---

# 🧩 **JSP Setup in Spring Boot**

### Add dependency:

```groovy
implementation 'org.apache.tomcat.embed:tomcat-embed-jasper'
```

### Configure prefix/suffix:

```properties
spring.mvc.view.prefix=/WEB-INF/jsp/
spring.mvc.view.suffix=.jsp
```

### Folder structure:

```
src/main/webapp/WEB-INF/jsp/*.jsp
```

---

# 🟨 **7. JSP Basic Syntax**

### **Show value**

```jsp
<p>${name}</p>
```

---

### **Scriptlet (Java inside JSP) ❌ discouraged**

```jsp
<%
  String message = "Hello";
%>
<p><%= message %></p>
```

---

### **JSTL tags (Better way)**

Add dependency:

```xml
<dependency>
   <groupId>javax.servlet</groupId>
   <artifactId>jstl</artifactId>
</dependency>
```

Use:

```jsp
<c:forEach var="item" items="${employees}">
  <p>${item.name}</p>
</c:forEach>
```

---

# 🟫 **8. JSP Advanced Syntax**

### **Include JSP**

```jsp
<jsp:include page="header.jsp" />
```

---

### **Conditional Rendering**

```jsp
<c:if test="${age >= 18}">
    Eligible
</c:if>
```

---

### **Form binding (manual)**

JSP does not have advanced form binding like Thymeleaf.

```jsp
<input name="name" value="${employee.name}">
```

---

# 🔄 **9. How JSP Integrates with Spring Boot**

Flow:

1. Controller returns `"home"`
2. Spring resolves `/WEB-INF/jsp/home.jsp`
3. JSP compiles into a servlet
4. Servlet generates HTML

Needs a servlet container (Tomcat), not HTML5-friendly.

---

# 🟥 **10. Thymeleaf vs JSP — Full Comparison Cheat Sheet**

| Feature                      | Thymeleaf           | JSP                                  |
| ---------------------------- | ------------------- | ------------------------------------ |
| Modern & HTML5-friendly      | ✅ Yes               | ❌ No                                 |
| Works well with Spring Boot  | ⭐ Excellent         | ⚠️ Requires extra config             |
| Template folder              | `/templates/*.html` | `/WEB-INF/jsp/*.jsp`                 |
| Can open directly in browser | ✔ Yes               | ❌ No (needs server)                  |
| Uses HTML attributes         | Yes (`th:*`)        | No                                   |
| Java in view                 | ❌ Not allowed       | ❌ Allowed but bad practice           |
| Reusable fragments           | ✔ Easy              | Limited                              |
| Spring form binding          | ✔ Strong            | ❌ Weak                               |
| URL linking                  | `@{/path}`          | `${pageContext.request.contextPath}` |
| Loops                        | `th:each`           | `<c:forEach>`                        |
| Conditionals                 | `th:if / th:unless` | `<c:if>`                             |
| Security/XSS protection      | Built-in            | Manual                               |
| Learning curve               | Easy                | Medium                               |
| Recommended for new apps     | ✔ Yes               | ❌ No                                 |
| Performance                  | High                | Medium                               |
| Supports natural HTML        | ✔ Yes               | ❌ No                                 |
| Designer-friendly            | Friendly            | Difficult                            |
| Standalone testing           | Possible            | Impossible                           |

---

# 🟦 **11. When to Use What?**

### ✔ Use **Thymeleaf** when:

* You're building a modern Spring Boot app
* You want clean HTML
* You need form binding
* You want reusable fragments
* You need better security

### ✔ Use **JSP** only when:

* You're maintaining an old application
* There is existing JSP code
* Migration is too costly

---

# 🎁 **12. 👍 Final Summary (Add to README)**

> **Thymeleaf** is the recommended server-side template engine for modern Spring Boot applications.
> It is HTML-friendly, supports reusable components, integrates seamlessly with Spring MVC, and provides powerful form handling and security features.
>
> **JSP**, while still used in legacy applications, is not suitable for new projects due to limited HTML compatibility, weaker Spring integration, and outdated syntax.

---
---

# 🧵 **1. What is a Server-Side Template Engine?**

A **server-side template engine (SST)** generates HTML on the server before sending it to the browser.

### Why do we need SST?

* To dynamically inject data into HTML pages
* To reduce Java code in JSP pages (MVC separation)
* To avoid manually building HTML strings
* To let the server render final HTML using variables, conditions, loops, forms, and objects

Spring Boot supports many SST engines:

* Thymeleaf
* JSP
* FreeMarker
* Mustache

We are focusing on **Thymeleaf** and **JSP**.

---

# 🧩 **2. Thymeleaf (Complete Beginner–Friendly Explanation)**

Thymeleaf is:

* A modern server-side template engine
* Natural-template (HTML opens directly in browser)
* Easy integration with Spring Boot
* Cleaner and more readable compared to JSP
* Supports advanced features (fragments, layouts, conditionals, loops, form binding)

---

## 📁 **Thymeleaf Project Folder Structure**

```
src
 └── main
     ├── java
     │    └── com.example.demo
     │         └── controller
     │              └── HomeController.java
     └── resources
          ├── templates  ← Thymeleaf HTML pages
          │     ├── index.html
          │     └── form.html
          └── application.properties
```

---

# 3. 🌿 **Thymeleaf Syntax (From Basic to Advanced)**

## ✔ Basic Output

```html
<p>Hello, <span th:text="${name}"></span></p>
```

## ✔ Variables

```html
<p th:text="${employee.employeeName}"></p>
```

## ✔ Loop (th:each)

```html
<tr th:each="emp : ${employees}">
    <td th:text="${emp.id}"></td>
    <td th:text="${emp.name}"></td>
</tr>
```

## ✔ Conditionals

```html
<p th:if="${age > 18}">Eligible</p>
<p th:unless="${active}">Not Active</p>
```

## ✔ Text + HTML Replace

```html
<div th:replace="fragments/header :: navbar"></div>
```

## ✔ Forms (ModelAttribute Binding)

```html
<form action="/submit" th:object="${user}" method="post">
    <input th:field="*{name}" />
    <input th:field="*{email}" />
    <button type="submit">Save</button>
</form>
```

## ✔ URL Syntax

```html
<a th:href="@{/employee/list}">View Employees</a>
```

---

# 4. 🔗 **Thymeleaf Integration in Spring Boot**

### Add Dependency

```gradle
implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
```

### Application Properties

```properties
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.cache=false
```

### Controller

```java
@Controller
public class HomeController {
    
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("name", "Ratnakar");
        return "index";  // Loads templates/index.html
    }
}
```

---

# 🟦 **Now JSP (Detailed Explanation)**

JSP is:

* Older Java template technology
* Allows Java code inside HTML via scriptlets (not recommended)
* Still usable in Spring Boot with some setup
* Not natural-template (cannot open in browser directly)
* Uses JSTL tags for modern development

---

## 📁 **JSP Project Folder Structure (Spring Boot)**

```
src
 └── main
     ├── java
     │    └── com.example.demo
     │         └── HomeController.java
     ├── resources
     │    └── application.properties
     └── webapp
          └── WEB-INF
               └── jsp
                   ├── index.jsp
                   └── form.jsp
```

---

# 5. 📘 **JSP Syntax (From Basic to Advanced)**

### ✔ Expression

```jsp
<p>Hello, ${name}</p>
```

### ✔ JSTL Tag Library

Add this at top:

```jsp
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
```

### ✔ Loop

```jsp
<c:forEach var="e" items="${employees}">
    <p>${e.name}</p>
</c:forEach>
```

### ✔ Conditionals

```jsp
<c:if test="${age > 18}">
    Eligible
</c:if>
```

### ✔ Include Pages

```jsp
<jsp:include page="header.jsp"/>
```

### ✔ Forms

```jsp
<form action="/submit" method="post">
    Name: <input name="name" />
</form>
```

---

# 6. 🔗 **JSP Integration in Spring Boot**

### Add Dependencies

```gradle
implementation 'org.apache.tomcat.embed:tomcat-embed-jasper'
implementation 'jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api:3.0.0'
implementation 'org.glassfish.web:jakarta.servlet.jsp.jstl:3.0.0'
```

### Application Properties

```properties
spring.mvc.view.prefix=/WEB-INF/jsp/
spring.mvc.view.suffix=.jsp
```

### Controller

```java
@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("name", "Ratnakar");
        return "index"; // Loads /WEB-INF/jsp/index.jsp
    }
}
```

---

# 🆚 **7. Thymeleaf vs JSP — Full Comparison (Cheat Sheet)**

| Feature                  | **Thymeleaf**                | **JSP**                              |
| ------------------------ | ---------------------------- | ------------------------------------ |
| Template Type            | Natural HTML Template        | Cannot open without running server   |
| Syntax                   | Modern, clean                | Older, JSTL-based                    |
| Supported in Spring Boot | Very strong support          | Medium support                       |
| Forms                    | Built-in form binder         | Basic HTML                           |
| Includes / Layout        | Fragments, layouts, reusable | JSP include                          |
| Loops                    | `th:each`                    | `<c:forEach>`                        |
| Conditions               | `th:if`, `th:unless`         | `<c:if>`                             |
| URL binding              | `@{...}`                     | `${pageContext.request.contextPath}` |
| Learning Curve           | Easy                         | Harder                               |
| Performance              | Fast                         | Fast                                 |
| HTML Validity            | Opens in browser             | Cannot be rendered directly          |
| Security                 | Built-in escaping            | Basic escaping                       |
| Based On                 | XHTML/HTML5                  | Java Servlet spec                    |
| Best For                 | Spring MVC apps              | Legacy systems                       |
| Recommended?             | ✔ Yes                        | ❓ Only if required                   |

---


