# Spring AOP (Aspect-Oriented Programming) - Complete Guide

## Table of Contents
1. [Introduction to AOP](#introduction-to-aop)
2. [Core AOP Concepts](#core-aop-concepts)
3. [Spring AOP Architecture](#spring-aop-architecture)
4. [Types of Advices](#types-of-advices)
5. [Pointcut Expressions](#pointcut-expressions)
6. [Implementation Examples](#implementation-examples)
7. [Advanced Topics](#advanced-topics)
8. [Interview Questions & Answers](#interview-questions--answers)

## Introduction to AOP

### What is AOP?
Aspect-Oriented Programming (AOP) is a programming paradigm that aims to increase modularity by allowing separation of cross-cutting concerns. Cross-cutting concerns are functionalities that span across multiple parts of an application (like logging, security, transaction management) but cannot be cleanly decomposed from the main business logic.

### Why AOP?
- **Code Reusability**: Common functionalities can be written once and applied across the application
- **Separation of Concerns**: Business logic remains clean and focused
- **Maintainability**: Changes to cross-cutting concerns need to be made in only one place
- **Reduced Code Duplication**: Eliminates scattered code for common functionalities

### Spring AOP vs AspectJ
- **Spring AOP**: Proxy-based, works only with Spring-managed beans, runtime weaving
- **AspectJ**: More powerful, compile-time/load-time weaving, works with any Java class

## Core AOP Concepts

### 1. Aspect
An aspect is a modularization of a concern that cuts across multiple classes. It contains advices and pointcuts.

### 2. Join Point
A point during the execution of a program, such as method execution or exception handling.

### 3. Advice
Action taken by an aspect at a particular join point. Different types: Before, After, Around, AfterReturning, AfterThrowing.

### 4. Pointcut
A predicate that matches join points. Pointcut expressions determine where advice should be applied.

### 5. Weaving
The process of linking aspects with other application types to create an advised object.

### 6. Target Object
Object being advised by one or more aspects.

### 7. AOP Proxy
An object created by AOP framework to implement aspect contracts.

## Spring AOP Architecture

Spring AOP uses either:
1. **JDK Dynamic Proxies** (default for interfaces)
2. **CGLIB Proxies** (for classes without interfaces)

## Types of Advices

### 1. Before Advice
Executed before the join point.

### 2. After Advice
Executed after the join point (finally advice).

### 3. AfterReturning Advice
Executed after successful completion of join point.

### 4. AfterThrowing Advice
Executed if method exits by throwing an exception.

### 5. Around Advice
Surrounds the join point, most powerful advice.

## Pointcut Expressions

### Common Pointcut Designators
- `execution`: Primary PCD for matching method execution
- `within`: Limits matching to join points within certain types
- `this`: Limits matching to join points where bean reference is of given type
- `target`: Limits matching to join points where target object is of given type
- `args`: Limits matching to join points where arguments are instances of given types
- `@target`, `@within`, `@annotation`, `@args`

### Execution Expression Format
```
execution(modifiers-pattern? return-type-pattern declaring-type-pattern? method-name-pattern(param-pattern) throws-pattern?)
```

## Implementation Examples

### Example 1: Basic Logging Aspect

#### Step 1: Add Dependencies
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

#### Step 2: Create Service Class
```java
package com.example.service;

import org.springframework.stereotype.Service;

/**
 * UserService class containing business logic for user operations.
 * This class demonstrates a typical service layer component where
 * we want to apply cross-cutting concerns like logging, security, etc.
 */
@Service
public class UserService {
    
    /**
     * Creates a new user in the system.
     * @param name Name of the user to create
     * @return Success message
     * 
     * This method represents a business operation where we might want to:
     * 1. Log method entry/exit
     * 2. Measure execution time
     * 3. Apply security checks
     * 4. Handle transactions
     * Without AOP, these concerns would be mixed with business logic.
     */
    public String createUser(String name) {
        // Business logic for creating user
        System.out.println("Creating user: " + name);
        
        // Simulate some processing time
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return "User " + name + " created successfully";
    }
    
    /**
     * Deletes a user from the system.
     * @param id User ID to delete
     * @return Success message
     * 
     * Similar to createUser, this method also has cross-cutting concerns.
     * With AOP, we can apply common behaviors without modifying this method.
     */
    public String deleteUser(Long id) {
        // Business logic for deleting user
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        System.out.println("Deleting user with ID: " + id);
        return "User with ID " + id + " deleted successfully";
    }
    
    /**
     * Finds a user by ID.
     * @param id User ID to find
     * @return User details
     * 
     * This method demonstrates a read operation where we might want:
     * 1. Performance monitoring
     * 2. Caching
     * 3. Access logging
     */
    public String findUserById(Long id) {
        System.out.println("Finding user with ID: " + id);
        return "User details for ID: " + id;
    }
}
```

#### Step 3: Create Logging Aspect
```java
package com.example.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * LoggingAspect demonstrates how to implement cross-cutting logging concern.
 * This aspect will be applied to multiple service methods without modifying
 * their business logic directly.
 * 
 * @Aspect annotation marks this class as an aspect - a modularization of
 * a concern that cuts across multiple classes.
 * 
 * @Component makes this aspect a Spring-managed bean so Spring can 
 * automatically detect and apply it.
 */
@Aspect
@Component
public class LoggingAspect {
    
    /**
     * Pointcut definition that matches all methods in UserService.
     * 
     * Pointcut is a predicate that matches join points. Here, it matches
     * execution of any method (*) in UserService class with any return type,
     * any method name, and any parameters.
     * 
     * We define pointcuts separately to reuse them across multiple advices.
     */
    @Pointcut("execution(* com.example.service.UserService.*(..))")
    public void userServiceMethods() {}
    
    /**
     * Pointcut for methods that have a String parameter.
     * This demonstrates how to match methods based on parameter types.
     */
    @Pointcut("execution(* com.example.service.UserService.*(String))")
    public void methodsWithStringParam() {}
    
    /**
     * Pointcut for methods that return String.
     * This demonstrates how to match methods based on return type.
     */
    @Pointcut("execution(String com.example.service.UserService.*(..))")
    public void methodsReturningString() {}
    
    /**
     * Before advice: Executes BEFORE the matched method runs.
     * 
     * @Before annotation specifies that this advice should run before
     * the join point (method execution).
     * 
     * JoinPoint parameter provides contextual information about the
     * intercepted method:
     * - Method signature
     * - Target object
     * - Method arguments
     * 
     * This advice logs method entry with method name and arguments.
     * Effect: Every time a UserService method is called, this log will appear first.
     */
    @Before("userServiceMethods()")
    public void logBeforeMethod(JoinPoint joinPoint) {
        // Get method name from join point
        String methodName = joinPoint.getSignature().getName();
        
        // Get method arguments
        Object[] args = joinPoint.getArgs();
        
        // Build argument string
        StringBuilder argsString = new StringBuilder();
        for (Object arg : args) {
            if (argsString.length() > 0) {
                argsString.append(", ");
            }
            argsString.append(arg);
        }
        
        // Log method entry
        System.out.println("[BEFORE] Entering method: " + methodName + 
                          " with arguments: [" + argsString + "]");
        
        // Real-world applications would use a logging framework like Log4j or SLF4J
        // This is a simplified example using System.out
    }
    
    /**
     * AfterReturning advice: Executes AFTER successful completion of method.
     * 
     * @AfterReturning annotation specifies that this advice should run after
     * the method successfully returns (no exception thrown).
     * 
     * The 'returning' attribute binds the method's return value to the
     * 'result' parameter of this advice.
     * 
     * This advice logs method exit with return value.
     * Effect: After successful execution, we get a log with the return value.
     */
    @AfterReturning(
        pointcut = "userServiceMethods()",
        returning = "result"
    )
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        
        // Log successful method completion with return value
        System.out.println("[AFTER RETURNING] Method: " + methodName + 
                          " successfully returned: " + result);
        
        // In real applications, you might:
        // 1. Log to file/database
        // 2. Send metrics to monitoring system
        // 3. Cache the result
        // 4. Update audit trails
    }
    
    /**
     * AfterThrowing advice: Executes if method exits by throwing an exception.
     * 
     * @AfterThrowing annotation specifies that this advice should run when
     * the method throws an exception.
     * 
     * The 'throwing' attribute binds the thrown exception to the
     * 'exception' parameter of this advice.
     * 
     * This advice logs exceptions without catching them (exceptions still propagate).
     * Effect: We get error logging without cluttering business logic with try-catch blocks.
     */
    @AfterThrowing(
        pointcut = "userServiceMethods()",
        throwing = "exception"
    )
    public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
        String methodName = joinPoint.getSignature().getName();
        
        // Log exception details
        System.out.println("[AFTER THROWING] Method: " + methodName + 
                          " threw exception: " + exception.getMessage());
        
        // In real applications, you might:
        // 1. Send alerts/notifications
        // 2. Log stack trace to error files
        // 3. Update error counters
        // 4. Transform exceptions
    }
    
    /**
     * After (finally) advice: Executes AFTER method completion (always runs).
     * 
     * @After annotation specifies that this advice should run after the method
     * completes, regardless of success or failure (like finally block).
     * 
     * This advice is useful for cleanup operations.
     * Effect: Guaranteed execution for resource cleanup or final logging.
     */
    @After("userServiceMethods()")
    public void logAfterMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        
        System.out.println("[AFTER FINALLY] Method: " + methodName + 
                          " execution completed (success or failure)");
        
        // Typical use cases:
        // 1. Close resources (files, connections)
        // 2. Clear thread-local variables
        // 3. Release locks
    }
    
    /**
     * Around advice: MOST POWERFUL advice that surrounds the join point.
     * 
     * @Around annotation specifies that this advice wraps the method execution.
     * It has control over whether the method actually executes and can modify
     * input/output.
     * 
     * ProceedingJoinPoint allows us to proceed with the original method call
     * using proceed() method.
     * 
     * This advice can:
     * 1. Prevent method execution
     * 2. Modify arguments
     * 3. Modify return value
     * 4. Add retry logic
     * 5. Implement caching
     */
    @Around("userServiceMethods()")
    public Object logAroundMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String methodName = proceedingJoinPoint.getSignature().getName();
        Object[] args = proceedingJoinPoint.getArgs();
        
        // Log before proceeding
        System.out.println("[AROUND START] Method: " + methodName + 
                          " is about to execute with args: " + java.util.Arrays.toString(args));
        
        // Start timing
        long startTime = System.currentTimeMillis();
        
        Object result = null;
        try {
            // Proceed with the original method execution
            // This is where the actual business method gets called
            result = proceedingJoinPoint.proceed();
            
            // Calculate execution time
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            
            // Log after successful execution
            System.out.println("[AROUND SUCCESS] Method: " + methodName + 
                              " executed successfully in " + executionTime + "ms");
            System.out.println("[AROUND SUCCESS] Return value: " + result);
            
        } catch (Exception e) {
            // Log if exception occurs
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            
            System.out.println("[AROUND ERROR] Method: " + methodName + 
                              " failed after " + executionTime + "ms");
            System.out.println("[AROUND ERROR] Exception: " + e.getMessage());
            
            // Re-throw the exception
            throw e;
        }
        
        // Return the result (could be modified)
        return result;
    }
    
    /**
     * Specific advice for methods with String parameter.
     * Demonstrates how to use specific pointcuts for targeted advice.
     */
    @Before("methodsWithStringParam()")
    public void logStringParamMethods(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        
        // This advice only runs for methods with String parameter
        System.out.println("[STRING PARAM METHOD] Method " + methodName + 
                          " called with string argument: " + args[0]);
        
        // We could add string-specific validations or transformations here
        if (args[0] != null && args[0].toString().length() > 100) {
            System.out.println("Warning: String parameter is very long");
        }
    }
}
```

#### Step 4: Main Application Class
```java
package com.example;

import com.example.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Main Spring Boot application class.
 * Demonstrates how AOP works in a Spring Boot application.
 */
@SpringBootApplication
public class AopDemoApplication {
    
    public static void main(String[] args) {
        // Start Spring application context
        ConfigurableApplicationContext context = 
            SpringApplication.run(AopDemoApplication.class, args);
        
        // Get UserService bean from Spring context
        // Note: This is actually a proxy object created by Spring AOP
        UserService userService = context.getBean(UserService.class);
        
        System.out.println("\n=== Testing AOP with UserService ===\n");
        
        // Test 1: Normal method execution
        System.out.println("Test 1: Creating user");
        String result1 = userService.createUser("John Doe");
        System.out.println("Result: " + result1);
        
        System.out.println("\n---\n");
        
        // Test 2: Method that might throw exception
        System.out.println("Test 2: Deleting user with null ID");
        try {
            String result2 = userService.deleteUser(null);
            System.out.println("Result: " + result2);
        } catch (Exception e) {
            System.out.println("Caught exception: " + e.getMessage());
        }
        
        System.out.println("\n---\n");
        
        // Test 3: Normal delete with valid ID
        System.out.println("Test 3: Deleting user with valid ID");
        String result3 = userService.deleteUser(123L);
        System.out.println("Result: " + result3);
        
        System.out.println("\n---\n");
        
        // Test 4: Find user
        System.out.println("Test 4: Finding user by ID");
        String result4 = userService.findUserById(456L);
        System.out.println("Result: " + result4);
        
        // Close application context
        context.close();
    }
}
```

#### Step 5: Application Properties
```properties
# Enable Spring AOP auto-proxying (enabled by default in Spring Boot)
spring.aop.auto=true

# Use CGLIB proxies instead of JDK dynamic proxies
# CGLIB can proxy classes without interfaces
spring.aop.proxy-target-class=true
```

#### Example Output
```
=== Testing AOP with UserService ===

Test 1: Creating user
[STRING PARAM METHOD] Method createUser called with string argument: John Doe
[BEFORE] Entering method: createUser with arguments: [John Doe]
[AROUND START] Method: createUser is about to execute with args: [John Doe]
Creating user: John Doe
[AROUND SUCCESS] Method: createUser executed successfully in 105ms
[AROUND SUCCESS] Return value: User John Doe created successfully
[AFTER RETURNING] Method: createUser successfully returned: User John Doe created successfully
[AFTER FINALLY] Method: createUser execution completed (success or failure)
Result: User John Doe created successfully

---

Test 2: Deleting user with null ID
[BEFORE] Entering method: deleteUser with arguments: [null]
[AROUND START] Method: deleteUser is about to execute with args: [null]
[AROUND ERROR] Method: deleteUser failed after 0ms
[AROUND ERROR] Exception: User ID cannot be null
[AFTER THROWING] Method: deleteUser threw exception: User ID cannot be null
[AFTER FINALLY] Method: deleteUser execution completed (success or failure)
Caught exception: User ID cannot be null

---

Test 3: Deleting user with valid ID
[BEFORE] Entering method: deleteUser with arguments: [123]
[AROUND START] Method: deleteUser is about to execute with args: [123]
Deleting user with ID: 123
[AROUND SUCCESS] Method: deleteUser executed successfully in 1ms
[AROUND SUCCESS] Return value: User with ID 123 deleted successfully
[AFTER RETURNING] Method: deleteUser successfully returned: User with ID 123 deleted successfully
[AFTER FINALLY] Method: deleteUser execution completed (success or failure)
Result: User with ID 123 deleted successfully

---

Test 4: Finding user by ID
[BEFORE] Entering method: findUserById with arguments: [456]
[AROUND START] Method: findUserById is about to execute with args: [456]
Finding user with ID: 456
[AROUND SUCCESS] Method: findUserById executed successfully in 0ms
[AROUND SUCCESS] Return value: User details for ID: 456
[AFTER RETURNING] Method: findUserById successfully returned: User details for ID: 456
[AFTER FINALLY] Method: findUserById execution completed (success or failure)
Result: User details for ID: 456
```

### Example 2: Transaction Management Aspect

```java
package com.example.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * TransactionAspect demonstrates how to implement declarative transaction management.
 * This is similar to how @Transactional annotation works in Spring.
 * 
 * Real-world applications should use Spring's @Transactional annotation,
 * but this example shows the underlying AOP mechanism.
 */
@Aspect
@Component
public class TransactionAspect {
    
    private final PlatformTransactionManager transactionManager;
    
    /**
     * Constructor injection of PlatformTransactionManager.
     * Spring automatically provides the appropriate transaction manager.
     */
    public TransactionAspect(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    
    /**
     * Pointcut for service methods that need transaction management.
     * Typically matches methods that modify data.
     */
    @Pointcut("@annotation(com.example.annotation.Transactional)")
    public void transactionalMethods() {}
    
    /**
     * Pointcut for service methods that update data.
     * Matches methods starting with save, update, delete, create.
     */
    @Pointcut("execution(* com.example.service.*.save*(..)) || " +
              "execution(* com.example.service.*.update*(..)) || " +
              "execution(* com.example.service.*.delete*(..)) || " +
              "execution(* com.example.service.*.create*(..))")
    public void dataModificationMethods() {}
    
    /**
     * Around advice for transaction management.
     * Demonstrates programmatic transaction control.
     */
    @Around("transactionalMethods() || dataModificationMethods()")
    public Object manageTransaction(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String methodName = proceedingJoinPoint.getSignature().getName();
        
        // Create transaction definition
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName(methodName + "Transaction");
        def.setReadOnly(false); // Read-write transaction
        
        // Start transaction
        TransactionStatus status = transactionManager.getTransaction(def);
        System.out.println("[TRANSACTION] Started transaction for method: " + methodName);
        
        Object result = null;
        try {
            // Execute the business method
            result = proceedingJoinPoint.proceed();
            
            // Commit transaction if successful
            transactionManager.commit(status);
            System.out.println("[TRANSACTION] Committed transaction for method: " + methodName);
            
        } catch (Exception e) {
            // Rollback transaction on exception
            transactionManager.rollback(status);
            System.out.println("[TRANSACTION] Rolled back transaction for method: " + 
                             methodName + " due to: " + e.getMessage());
            
            // Re-throw the exception
            throw e;
        }
        
        return result;
    }
    
    /**
     * Around advice for read-only operations.
     * Uses read-only transactions for better performance.
     */
    @Around("execution(* com.example.service.*.find*(..)) || " +
            "execution(* com.example.service.*.get*(..)) || " +
            "execution(* com.example.service.*.search*(..))")
    public Object manageReadOnlyTransaction(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String methodName = proceedingJoinPoint.getSignature().getName();
        
        // Create read-only transaction definition
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName(methodName + "ReadOnlyTransaction");
        def.setReadOnly(true); // Read-only transaction for better performance
        
        // Start transaction
        TransactionStatus status = transactionManager.getTransaction(def);
        System.out.println("[READ-ONLY TRANSACTION] Started read-only transaction for method: " + methodName);
        
        Object result = null;
        try {
            // Execute the business method
            result = proceedingJoinPoint.proceed();
            
            // Commit transaction
            transactionManager.commit(status);
            System.out.println("[READ-ONLY TRANSACTION] Committed read-only transaction for method: " + methodName);
            
        } catch (Exception e) {
            // Rollback transaction on exception
            transactionManager.rollback(status);
            System.out.println("[READ-ONLY TRANSACTION] Rolled back transaction for method: " + 
                             methodName + " due to: " + e.getMessage());
            
            // Re-throw the exception
            throw e;
        }
        
        return result;
    }
}
```

### Example 3: Custom Annotation for Security

```java
package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to mark methods that require authorization.
 * This demonstrates how to create domain-specific annotations for AOP.
 * 
 * @Target(ElementType.METHOD) means this annotation can only be applied to methods.
 * @Retention(RetentionPolicy.RUNTIME) means the annotation is available at runtime
 * via reflection, which is required for AOP to detect it.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermission {
    
    /**
     * Required permission to execute the method.
     * Default is "READ" permission.
     */
    String value() default "READ";
    
    /**
     * Role required to execute the method.
     * If empty, only permission is checked.
     */
    String role() default "";
    
    /**
     * Whether to log access attempts.
     */
    boolean logAccess() default true;
}
```

```java
package com.example.aspect;

import com.example.annotation.RequiresPermission;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * SecurityAspect demonstrates role-based and permission-based security.
 * This aspect intercepts methods annotated with @RequiresPermission and
 * checks if the current user has the required permissions.
 * 
 * In a real application, this would integrate with Spring Security.
 */
@Aspect
@Component
public class SecurityAspect {
    
    /**
     * Simulated current user context.
     * In real application, this would come from Spring Security Context.
     */
    private String currentUserRole = "ADMIN";
    private String currentUserPermissions = "READ,WRITE,DELETE";
    
    /**
     * Before advice for methods with @RequiresPermission annotation.
     * 
     * @annotation(requiresPermission) binds the annotation instance
     * to the 'requiresPermission' parameter.
     */
    @Before("@annotation(requiresPermission)")
    public void checkPermission(JoinPoint joinPoint, RequiresPermission requiresPermission) {
        String methodName = joinPoint.getSignature().getName();
        String requiredPermission = requiresPermission.value();
        String requiredRole = requiresPermission.role();
        boolean logAccess = requiresPermission.logAccess();
        
        if (logAccess) {
            System.out.println("[SECURITY] Checking access for method: " + methodName);
            System.out.println("[SECURITY] Required permission: " + requiredPermission);
            System.out.println("[SECURITY] Required role: " + requiredRole);
            System.out.println("[SECURITY] Current user role: " + currentUserRole);
            System.out.println("[SECURITY] Current user permissions: " + currentUserPermissions);
        }
        
        // Check role if specified
        if (!requiredRole.isEmpty()) {
            if (!requiredRole.equals(currentUserRole)) {
                String errorMessage = "Access denied to method " + methodName + 
                                    ". Required role: " + requiredRole + 
                                    ", but user has: " + currentUserRole;
                System.out.println("[SECURITY] " + errorMessage);
                throw new SecurityException(errorMessage);
            }
        }
        
        // Check permission
        if (!currentUserPermissions.contains(requiredPermission)) {
            String errorMessage = "Access denied to method " + methodName + 
                                ". Required permission: " + requiredPermission + 
                                ", but user has: " + currentUserPermissions;
            System.out.println("[SECURITY] " + errorMessage);
            throw new SecurityException(errorMessage);
        }
        
        System.out.println("[SECURITY] Access granted to method: " + methodName);
    }
    
    /**
     * More specific pointcut for admin-only methods.
     */
    @Before("execution(* com.example.service.*.delete*(..)) || " +
            "execution(* com.example.service.*.update*(..))")
    public void checkAdminAccess(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        
        System.out.println("[SECURITY] Checking admin access for method: " + methodName);
        
        if (!"ADMIN".equals(currentUserRole)) {
            String errorMessage = "Admin access required for method: " + methodName;
            System.out.println("[SECURITY] " + errorMessage);
            throw new SecurityException(errorMessage);
        }
        
        System.out.println("[SECURITY] Admin access granted for method: " + methodName);
    }
}
```

### Example 4: Performance Monitoring Aspect

```java
package com.example.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * PerformanceAspect demonstrates monitoring and metrics collection.
 * This aspect tracks method execution times, invocation counts, and
 * can trigger alerts for slow methods.
 */
@Aspect
@Component
public class PerformanceAspect {
    
    // Thread-safe maps to store performance metrics
    private final ConcurrentHashMap<String, AtomicLong> totalExecutionTime = 
        new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicInteger> invocationCount = 
        new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> maxExecutionTime = 
        new ConcurrentHashMap<>();
    
    /**
     * Pointcut for all service methods.
     */
    @Pointcut("execution(* com.example.service.*.*(..))")
    public void serviceMethods() {}
    
    /**
     * Pointcut for repository/data access methods.
     */
    @Pointcut("execution(* com.example.repository.*.*(..))")
    public void repositoryMethods() {}
    
    /**
     * Around advice for performance monitoring.
     * Collects metrics for every method execution.
     */
    @Around("serviceMethods() || repositoryMethods()")
    public Object monitorPerformance(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String methodName = proceedingJoinPoint.getSignature().toShortString();
        
        // Initialize metrics if first time
        totalExecutionTime.putIfAbsent(methodName, new AtomicLong(0));
        invocationCount.putIfAbsent(methodName, new AtomicInteger(0));
        maxExecutionTime.putIfAbsent(methodName, new AtomicLong(0));
        
        // Start timing
        long startTime = System.nanoTime();
        
        Object result = null;
        try {
            // Execute the method
            result = proceedingJoinPoint.proceed();
            
        } finally {
            // Calculate execution time
            long endTime = System.nanoTime();
            long executionTimeNanos = endTime - startTime;
            long executionTimeMillis = executionTimeNanos / 1_000_000;
            
            // Update metrics
            totalExecutionTime.get(methodName).addAndGet(executionTimeNanos);
            invocationCount.get(methodName).incrementAndGet();
            
            // Update max execution time
            AtomicLong maxTime = maxExecutionTime.get(methodName);
            long currentMax;
            long newMax;
            do {
                currentMax = maxTime.get();
                newMax = Math.max(currentMax, executionTimeNanos);
            } while (!maxTime.compareAndSet(currentMax, newMax));
            
            // Log slow methods (threshold: 100ms)
            if (executionTimeMillis > 100) {
                System.out.println("[PERFORMANCE WARNING] Slow method detected: " + methodName);
                System.out.println("[PERFORMANCE WARNING] Execution time: " + executionTimeMillis + "ms");
                
                // In real application, you might:
                // 1. Send alert to monitoring system
                // 2. Log to performance log file
                // 3. Trigger profiling
            }
            
            // Log detailed metrics every 10 invocations
            if (invocationCount.get(methodName).get() % 10 == 0) {
                printMetrics(methodName);
            }
        }
        
        return result;
    }
    
    /**
     * Prints performance metrics for a method.
     */
    private void printMetrics(String methodName) {
        long totalTime = totalExecutionTime.get(methodName).get();
        int count = invocationCount.get(methodName).get();
        long maxTime = maxExecutionTime.get(methodName).get();
        
        if (count > 0) {
            double avgTimeMillis = (totalTime / count) / 1_000_000.0;
            double maxTimeMillis = maxTime / 1_000_000.0;
            
            System.out.println("\n[PERFORMANCE METRICS] Method: " + methodName);
            System.out.println("[PERFORMANCE METRICS] Invocation count: " + count);
            System.out.println("[PERFORMANCE METRICS] Average time: " + 
                             String.format("%.2f", avgTimeMillis) + "ms");
            System.out.println("[PERFORMANCE METRICS] Max time: " + 
                             String.format("%.2f", maxTimeMillis) + "ms");
            System.out.println("[PERFORMANCE METRICS] Total time: " + 
                             (totalTime / 1_000_000) + "ms");
        }
    }
    
    /**
     * Method to get all performance metrics (could be exposed via JMX or REST).
     */
    public void printAllMetrics() {
        System.out.println("\n=== PERFORMANCE METRICS SUMMARY ===");
        totalExecutionTime.forEach((methodName, totalTime) -> {
            int count = invocationCount.get(methodName).get();
            if (count > 0) {
                double avgTimeMillis = (totalTime.get() / count) / 1_000_000.0;
                System.out.println(methodName + ": " + count + " calls, " + 
                                 String.format("%.2f", avgTimeMillis) + "ms avg");
            }
        });
        System.out.println("===================================\n");
    }
}
```

## Advanced Topics

### 1. Aspect Ordering
```java
@Aspect
@Component
@Order(1)  // Lower order number = higher precedence
public class LoggingAspect {
    // This aspect executes before SecurityAspect
}

@Aspect
@Component
@Order(2)
public class SecurityAspect {
    // This aspect executes after LoggingAspect
}
```

### 2. Introduction (Inter-type Declaration)
```java
@Aspect
@Component
public class IntroductionAspect {
    
    /**
     * Introduction adds new interface implementation to existing classes.
     * This makes UserService implement Auditable interface at runtime.
     */
    @DeclareParents(
        value = "com.example.service.UserService",
        defaultImpl = DefaultAuditable.class
    )
    public static Auditable mixin;
    
    public interface Auditable {
        void audit();
    }
    
    public static class DefaultAuditable implements Auditable {
        @Override
        public void audit() {
            System.out.println("Audit performed");
        }
    }
}
```

### 3. Load-time Weaving (LTW)
```java
// Enable LTW in application.properties
spring.aop.proxy-target-class=false

// Add VM argument for LTW
-javaagent:path/to/spring-instrument.jar
```

### 4. AspectJ Integration
```java
// Use AspectJ annotations for more powerful AOP
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AopConfig {
    // Configuration
}
```

## Best Practices

1. **Use specific pointcuts**: Avoid too broad pointcuts like `execution(* *.*(..))`
2. **Order aspects properly**: Use `@Order` annotation for dependent aspects
3. **Keep aspects simple**: Each aspect should handle one concern
4. **Use custom annotations**: For better readability and control
5. **Test aspects thoroughly**: Aspects affect multiple parts of application
6. **Monitor performance**: AOP adds overhead, monitor and optimize
7. **Document aspects**: Clearly document what each aspect does

## Common Pitfalls

1. **Self-invocation**: Methods calling other methods in same class won't be advised
2. **Final methods**: Cannot be proxied with CGLIB
3. **Private methods**: Not advised by Spring AOP
4. **Constructor calls**: Not intercepted
5. **Performance overhead**: Each advice adds execution time

## Interview Questions & Answers

### Q1: What is Spring AOP and why is it used?

**Answer:**
Spring AOP (Aspect-Oriented Programming) is a programming paradigm that allows separation of cross-cutting concerns from the main business logic. Cross-cutting concerns are functionalities that affect multiple parts of an application, such as logging, security, transaction management, and caching.

**Why use Spring AOP:**
1. **Separation of Concerns**: Business logic remains clean and focused on core functionality
2. **Code Reusability**: Common functionalities can be written once and applied across the application
3. **Maintainability**: Changes to cross-cutting concerns need to be made in only one place
4. **Reduced Code Duplication**: Eliminates scattered code for common functionalities
5. **Modularity**: Aspects can be developed and tested independently

**Example:** Without AOP, logging code would be scattered across every method:
```java
public class UserService {
    public void createUser(User user) {
        log.info("Creating user: " + user.getName());  // Logging mixed with business logic
        // Business logic
        log.info("User created successfully");
    }
    
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: " + id);  // Repeated logging code
        // Business logic
        log.info("User deleted successfully");
    }
}
```

With AOP:
```java
public class UserService {
    public void createUser(User user) {
        // Clean business logic only
    }
    
    public void deleteUser(Long id) {
        // Clean business logic only
    }
}

@Aspect
@Component
public class LoggingAspect {
    @Before("execution(* UserService.*(..))")
    public void logBefore(JoinPoint jp) {
        // Centralized logging for all methods
    }
}
```

### Q2: Explain the different types of advice in Spring AOP.

**Answer:**
Advice is the action taken by an aspect at a particular join point. Spring AOP supports five types of advice:

1. **Before Advice**: Executes before the join point
   ```java
   @Before("pointcutExpression()")
   public void beforeAdvice(JoinPoint jp) {
       // Code to execute before method
   }
   ```

2. **After Advice (Finally)**: Executes after the join point completes (always runs)
   ```java
   @After("pointcutExpression()")
   public void afterAdvice(JoinPoint jp) {
       // Code to execute after method (success or failure)
   }
   ```

3. **AfterReturning Advice**: Executes after successful completion of join point
   ```java
   @AfterReturning(pointcut="pointcutExpression()", returning="result")
   public void afterReturningAdvice(JoinPoint jp, Object result) {
       // Code to execute after successful method completion
   }
   ```

4. **AfterThrowing Advice**: Executes if method exits by throwing exception
   ```java
   @AfterThrowing(pointcut="pointcutExpression()", throwing="ex")
   public void afterThrowingAdvice(JoinPoint jp, Exception ex) {
       // Code to execute when method throws exception
   }
   ```

5. **Around Advice**: Most powerful advice that surrounds the join point
   ```java
   @Around("pointcutExpression()")
   public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
       // Code before method execution
       Object result = pjp.proceed();  // Can choose to proceed or not
       // Code after method execution
       return result;  // Can modify return value
   }
   ```

**Execution Order for same aspect:**
1. Around (before part)
2. Before
3. Method execution
4. Around (after part)
5. AfterReturning/AfterThrowing
6. After (finally)

### Q3: What is the difference between Spring AOP and AspectJ?

**Answer:**

| **Aspect** | **Spring AOP** | **AspectJ** |
|------------|----------------|-------------|
| **Weaving** | Runtime weaving using proxies | Compile-time, post-compile, or load-time weaving |
| **Performance** | Slower due to runtime proxies | Faster as weaving happens at compile/load time |
| **Join Points** | Only method execution join points | All join points (method, constructor, field access, etc.) |
| **Dependency** | Requires Spring container | Can work independently |
| **Learning Curve** | Simpler, uses proxy pattern | More complex, complete AOP implementation |
| **Usage** | Suitable for common cross-cutting concerns in Spring apps | Suitable for complex AOP requirements |
| **Proxy Type** | JDK dynamic proxies or CGLIB | No proxies, direct bytecode modification |

**When to use Spring AOP:**
- Simple cross-cutting concerns
- Already using Spring Framework
- Only need method interception
- Don't want to introduce new build tools

**When to use AspectJ:**
- Need to intercept field access, constructor calls, etc.
- Performance is critical
- Complex AOP requirements
- Working outside Spring container

### Q4: What are pointcut expressions? Explain with examples.

**Answer:**
Pointcut expressions are predicates that match join points. They determine where advice should be applied.

**Common Pointcut Designators (PCDs):**

1. **execution**: Primary PCD for matching method execution
   ```java
   // Match any public method
   @Pointcut("execution(public * *(..))")
   
   // Match methods starting with 'get' in any class
   @Pointcut("execution(* get*(..))")
   
   // Match methods in specific class
   @Pointcut("execution(* com.example.service.UserService.*(..))")
   
   // Match methods with specific return type and parameters
   @Pointcut("execution(String com.example.service.*.find*(Long))")
   ```

2. **within**: Limits matching to join points within certain types
   ```java
   // All methods in service package
   @Pointcut("within(com.example.service.*)")
   
   // All methods in service package and subpackages
   @Pointcut("within(com.example.service..*)")
   ```

3. **this**: Limits matching to join points where bean reference is of given type
   ```java
   // When proxy implements UserService interface
   @Pointcut("this(com.example.service.UserService)")
   ```

4. **target**: Limits matching to join points where target object is of given type
   ```java
   // When target object is UserService implementation
   @Pointcut("target(com.example.service.UserService)")
   ```

5. **args**: Limits matching to join points where arguments are instances of given types
   ```java
   // Methods with single String parameter
   @Pointcut("args(String)")
   
   // Methods with User as first parameter
   @Pointcut("args(com.example.model.User,..)")
   ```

6. **@annotation**: Limits matching to join points with given annotation
   ```java
   // Methods annotated with @Transactional
   @Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
   
   // Custom annotation
   @Pointcut("@annotation(com.example.annotation.Auditable)")
   ```

7. **@within**: Limits matching to join points within types with given annotation
   ```java
   // All methods in classes annotated with @Service
   @Pointcut("@within(org.springframework.stereotype.Service)")
   ```

**Combining Pointcuts:**
```java
// Combine using logical operators
@Pointcut("execution(* com.example.service.*.*(..)) && " +
          "!execution(* com.example.service.*.get*(..))")
public void serviceMethodsExceptGetters() {}

// Using named pointcuts
@Pointcut("serviceMethods() && transactionalMethods()")
public void serviceTransactionalMethods() {}
```

### Q5: Explain the proxy pattern in Spring AOP.

**Answer:**
Spring AOP uses the proxy pattern to implement aspects. A proxy is an object that wraps the target object and intercepts method calls to apply advice.

**Two types of proxies in Spring AOP:**

1. **JDK Dynamic Proxy** (default for interfaces):
    - Creates proxy implementing the same interface as target
    - Uses `java.lang.reflect.Proxy` class
    - Requires target to implement at least one interface
   ```java
   // Interface
   public interface UserService {
       void createUser(String name);
   }
   
   // Implementation
   public class UserServiceImpl implements UserService {
       public void createUser(String name) {
           // implementation
       }
   }
   
   // Spring creates proxy: Proxy implements UserService
   // and delegates to UserServiceImpl with advice
   ```

2. **CGLIB Proxy** (for classes without interfaces):
    - Creates proxy by subclassing target class
    - Uses bytecode generation library
    - Cannot proxy final classes/methods
   ```java
   // No interface
   public class UserService {
       public void createUser(String name) {
           // implementation
       }
   }
   
   // Spring creates: UserService$$EnhancerBySpringCGLIB extends UserService
   // Overrides non-final methods to add advice
   ```

**How to choose:**
```properties
# Force CGLIB proxies
spring.aop.proxy-target-class=true
```

**Proxy Creation Process:**
1. Spring container starts
2. Detects @Aspect beans
3. Creates proxies for beans matching pointcuts
4. Proxies intercept method calls
5. Apply advice before/after/around method execution

**Limitations:**
- Self-invocation (method calling another method in same class) bypasses proxy
- Only public methods can be advised
- Final methods cannot be advised (CGLIB)
- Private methods cannot be advised

### Q6: How to handle self-invocation problem in Spring AOP?

**Answer:**
Self-invocation occurs when a method in a proxied object calls another method in the same object. Since the call happens on `this` reference (not the proxy), advice is not applied.

**Problem Example:**
```java
@Service
public class UserService {
    
    public void createUser(User user) {
        // This method call won't be advised!
        validateUser(user);  // Self-invocation
        // Business logic
    }
    
    @Transactional
    public void validateUser(User user) {
        // Transactional advice won't be applied
        // when called from createUser
    }
}
```

**Solutions:**

1. **Use AspectJ**: Switch to AspectJ weaving (compile-time or load-time)
   ```java
   @EnableAspectJAutoProxy(proxyTargetClass=true, mode=AdviceMode.ASPECTJ)
   ```

2. **Inject Self Reference** (Workaround):
   ```java
   @Service
   public class UserService {
       
       @Autowired
       private ApplicationContext context;
       
       private UserService self;
       
       @PostConstruct
       public void init() {
           self = context.getBean(UserService.class);
       }
       
       public void createUser(User user) {
           // Call through proxy
           self.validateUser(user);  // Now advised
       }
       
       @Transactional
       public void validateUser(User user) {
           // Will be advised
       }
   }
   ```

3. **Restructure Code**: Move methods to different services
   ```java
   @Service
   public class ValidationService {
       @Transactional
       public void validateUser(User user) {
           // Transactional advice will work
       }
   }
   
   @Service
   public class UserService {
       @Autowired
       private ValidationService validationService;
       
       public void createUser(User user) {
           validationService.validateUser(user);  // Advised
       }
   }
   ```

4. **Use Programmatic AOP** (Advanced):
   ```java
   AopContext.currentProxy();
   ```

**Best Practice:** Design services to avoid self-invocation for methods that need advice.

### Q7: What is the difference between @Before and @Around advice?

**Answer:**

| **Aspect** | **@Before** | **@Around** |
|------------|-------------|-------------|
| **Control** | No control over method execution | Full control over whether/when method executes |
| **Exception Handling** | Cannot prevent exception propagation | Can catch and handle exceptions |
| **Return Value** | Cannot modify return value | Can modify or completely change return value |
| **Method Execution** | Method always executes after advice | Can choose to skip method execution |
| **Use Case** | Simple pre-processing (logging, validation) | Complex scenarios (caching, retry, transaction) |
| **Parameters** | JoinPoint only | ProceedingJoinPoint (has proceed() method) |

**Example Comparison:**

```java
// @Before example - Simple logging
@Before("execution(* UserService.*(..))")
public void logBefore(JoinPoint jp) {
    System.out.println("Before method: " + jp.getSignature().getName());
    // Method will definitely execute after this
    // Cannot stop execution even if validation fails
}

// @Around example - Complex scenario with caching
@Around("execution(* UserService.find*(..))")
public Object cacheAround(ProceedingJoinPoint pjp) throws Throwable {
    String cacheKey = createCacheKey(pjp);
    
    // Check cache first
    Object cachedValue = cache.get(cacheKey);
    if (cachedValue != null) {
        System.out.println("Returning cached value");
        return cachedValue;  // Return without executing method
    }
    
    // Execute method if not in cache
    System.out.println("Cache miss, executing method");
    Object result = pjp.proceed();  // Can choose to proceed or not
    
    // Store in cache
    cache.put(cacheKey, result);
    
    // Can modify result before returning
    return enhancedResult(result);
}
```

**When to use @Before:**
- Simple preconditions check
- Logging entry
- Simple validation
- Audit logging

**When to use @Around:**
- Performance monitoring (timing)
- Caching
- Transaction management
- Retry logic
- Circuit breaker pattern
- When you need to control method execution

### Q8: How to implement retry logic using Spring AOP?

**Answer:**
Retry logic is a perfect use case for @Around advice. Here's a complete implementation:

```java
package com.example.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * RetryAspect demonstrates how to implement retry logic for methods
 * that might fail due to transient errors (network issues, database locks, etc.)
 */
@Aspect
@Component
public class RetryAspect {
    
    /**
     * Custom annotation to configure retry behavior per method.
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Retryable {
        int maxAttempts() default 3;
        long delay() default 1000; // milliseconds
        Class<? extends Throwable>[] retryFor() default {Exception.class};
    }
    
    /**
     * Pointcut for methods annotated with @Retryable.
     */
    @Pointcut("@annotation(retryable)")
    public void retryableMethods(Retryable retryable) {}
    
    /**
     * Around advice implementing retry logic.
     */
    @Around("retryableMethods(retryable)")
    public Object retryMethod(ProceedingJoinPoint pjp, Retryable retryable) throws Throwable {
        String methodName = pjp.getSignature().getName();
        int maxAttempts = retryable.maxAttempts();
        long delay = retryable.delay();
        Class<? extends Throwable>[] retryExceptions = retryable.retryFor();
        
        AtomicInteger attempt = new AtomicInteger(1);
        Throwable lastException = null;
        
        while (attempt.get() <= maxAttempts) {
            try {
                System.out.println("[RETRY] Attempt " + attempt.get() + 
                                 " of " + maxAttempts + " for method: " + methodName);
                
                // Try to execute the method
                return pjp.proceed();
                
            } catch (Throwable e) {
                lastException = e;
                
                // Check if exception is retryable
                if (!isRetryableException(e, retryExceptions)) {
                    System.out.println("[RETRY] Non-retryable exception: " + e.getClass().getName());
                    throw e;
                }
                
                System.out.println("[RETRY] Attempt " + attempt.get() + 
                                 " failed with: " + e.getMessage());
                
                // Check if we should retry
                if (attempt.get() >= maxAttempts) {
                    System.out.println("[RETRY] Max attempts reached for method: " + methodName);
                    break;
                }
                
                // Wait before retry (with exponential backoff)
                long waitTime = delay * (long) Math.pow(2, attempt.get() - 1);
                System.out.println("[RETRY] Waiting " + waitTime + "ms before next attempt");
                Thread.sleep(waitTime);
                
                attempt.incrementAndGet();
            }
        }
        
        // All attempts failed
        System.out.println("[RETRY] All " + maxAttempts + 
                         " attempts failed for method: " + methodName);
        throw lastException;
    }
    
    /**
     * Check if exception is in retryable exceptions list.
     */
    private boolean isRetryableException(Throwable e, Class<? extends Throwable>[] retryExceptions) {
        for (Class<? extends Throwable> retryException : retryExceptions) {
            if (retryException.isAssignableFrom(e.getClass())) {
                return true;
            }
        }
        return false;
    }
}

// Usage example
@Service
public class PaymentService {
    
    @Retryable(maxAttempts = 5, delay = 1000, retryFor = {NetworkException.class})
    public PaymentResult processPayment(PaymentRequest request) {
        // This method will be retried up to 5 times
        // if NetworkException is thrown
        return paymentGateway.process(request);
    }
    
    @Retryable(maxAttempts = 3, delay = 500)
    public void updateInventory(Long productId, int quantity) {
        // Retry for any exception (default)
        inventoryRepository.updateStock(productId, quantity);
    }
}
```

**Key Features:**
1. **Configurable attempts**: Different methods can have different retry counts
2. **Delay between retries**: With exponential backoff
3. **Exception filtering**: Retry only for specific exceptions
4. **Logging**: Track retry attempts for monitoring

**Real-world Enhancements:**
- Add circuit breaker pattern
- Make delay configurable with jitter
- Add metrics collection
- Integrate with monitoring system
- Support for asynchronous retries

### Q9: How to implement caching using Spring AOP?

**Answer:**
Caching is another excellent use case for @Around advice. Here's a complete implementation:

```java
package com.example.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CacheAspect demonstrates method-level caching.
 * This is similar to Spring's @Cacheable but implemented manually to show AOP concepts.
 */
@Aspect
@Component
public class CacheAspect {
    
    // Simple in-memory cache (use Redis/Memcached in production)
    private final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();
    
    /**
     * Annotation to mark cacheable methods.
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Cacheable {
        String cacheName() default "default";
        long ttl() default 300000; // Time to live in milliseconds (5 minutes default)
        boolean evictOnUpdate() default true;
    }
    
    /**
     * Annotation to evict cache.
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface CacheEvict {
        String cacheName() default "default";
        boolean allEntries() default false;
    }
    
    /**
     * Pointcut for cacheable methods.
     */
    @Pointcut("@annotation(cacheable)")
    public void cacheableMethods(Cacheable cacheable) {}
    
    /**
     * Pointcut for cache evict methods.
     */
    @Pointcut("@annotation(cacheEvict)")
    public void cacheEvictMethods(CacheEvict cacheEvict) {}
    
    /**
     * Around advice for caching.
     */
    @Around("cacheableMethods(cacheable)")
    public Object cacheMethodResult(ProceedingJoinPoint pjp, Cacheable cacheable) throws Throwable {
        String methodName = pjp.getSignature().toShortString();
        Object[] args = pjp.getArgs();
        String cacheName = cacheable.cacheName();
        
        // Create cache key from method name and arguments
        String cacheKey = createCacheKey(cacheName, methodName, args);
        
        System.out.println("[CACHE] Checking cache for key: " + cacheKey);
        
        // Check cache
        Object cachedValue = cache.get(cacheKey);
        if (cachedValue != null) {
            System.out.println("[CACHE] Cache HIT for method: " + methodName);
            return cachedValue;
        }
        
        System.out.println("[CACHE] Cache MISS for method: " + methodName);
        
        // Execute method
        Object result = pjp.proceed();
        
        // Store in cache
        if (result != null) {
            System.out.println("[CACHE] Storing result in cache for key: " + cacheKey);
            cache.put(cacheKey, result);
            
            // Schedule TTL eviction (simplified - use proper scheduler in production)
            if (cacheable.ttl() > 0) {
                scheduleEviction(cacheKey, cacheable.ttl());
            }
        }
        
        return result;
    }
    
    /**
     * Around advice for cache eviction.
     */
    @Around("cacheEvictMethods(cacheEvict)")
    public Object evictCache(ProceedingJoinPoint pjp, CacheEvict cacheEvict) throws Throwable {
        String methodName = pjp.getSignature().getName();
        String cacheName = cacheEvict.cacheName();
        
        System.out.println("[CACHE] Evicting cache for: " + cacheName);
        
        if (cacheEvict.allEntries()) {
            // Evict all entries for this cache
            evictAllForCache(cacheName);
        } else {
            // Evict specific entry based on method arguments
            Object[] args = pjp.getArgs();
            String cacheKey = createCacheKey(cacheName, methodName, args);
            cache.remove(cacheKey);
            System.out.println("[CACHE] Evicted cache entry: " + cacheKey);
        }
        
        // Execute the method (update operation)
        return pjp.proceed();
    }
    
    /**
     * Create cache key from method signature and arguments.
     */
    private String createCacheKey(String cacheName, String methodName, Object[] args) {
        StringBuilder keyBuilder = new StringBuilder(cacheName);
        keyBuilder.append("::").append(methodName);
        
        for (Object arg : args) {
            keyBuilder.append("::").append(arg != null ? arg.hashCode() : "null");
        }
        
        return keyBuilder.toString();
    }
    
    /**
     * Evict all entries for a cache.
     */
    private void evictAllForCache(String cacheName) {
        cache.entrySet().removeIf(entry -> entry.getKey().startsWith(cacheName + "::"));
        System.out.println("[CACHE] Evicted all entries for cache: " + cacheName);
    }
    
    /**
     * Schedule cache eviction after TTL (simplified example).
     */
    private void scheduleEviction(String cacheKey, long ttl) {
        new Thread(() -> {
            try {
                Thread.sleep(ttl);
                cache.remove(cacheKey);
                System.out.println("[CACHE] TTL expired, evicted: " + cacheKey);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
    
    /**
     * Get cache statistics (for monitoring).
     */
    public CacheStats getCacheStats() {
        return new CacheStats(cache.size());
    }
    
    public static class CacheStats {
        private final int size;
        
        public CacheStats(int size) {
            this.size = size;
        }
        
        public int getSize() {
            return size;
        }
    }
}

// Usage example
@Service
public class ProductService {
    
    @Cacheable(cacheName = "products", ttl = 600000) // 10 minutes TTL
    public Product getProductById(Long id) {
        System.out.println("Fetching product from database for ID: " + id);
        // Simulate database call
        return productRepository.findById(id).orElse(null);
    }
    
    @Cacheable(cacheName = "products")
    public List<Product> searchProducts(String keyword, int page, int size) {
        System.out.println("Searching products in database for: " + keyword);
        return productRepository.search(keyword, page, size);
    }
    
    @CacheEvict(cacheName = "products", allEntries = true)
    public Product updateProduct(Long id, Product product) {
        System.out.println("Updating product in database");
        // Update will evict all product caches
        return productRepository.save(product);
    }
    
    @CacheEvict(cacheName = "products")
    public void deleteProduct(Long id) {
        System.out.println("Deleting product from database");
        // Will evict cache for this specific product
        productRepository.deleteById(id);
    }
}
```

**Key Features:**
1. **Method-level caching**: Cache results based on method signature and arguments
2. **Configurable TTL**: Automatic eviction after time-to-live
3. **Cache eviction**: Clear cache on update/delete operations
4. **Multiple cache support**: Different caches for different data types

**Production Considerations:**
- Use distributed cache (Redis, Memcached)
- Implement cache serialization
- Add cache statistics and monitoring
- Handle cache stampede protection
- Implement cache warming

### Q10: What are the limitations of Spring AOP?

**Answer:**
While Spring AOP is powerful, it has several limitations:

1. **Method-level Only**: Can only advise method execution join points
   ```java
   // CANNOT intercept:
   - Field access: user.name = "John"
   - Constructor calls: new UserService()
   - Static initializers
   ```

2. **Spring Bean Limitation**: Only works with Spring-managed beans
   ```java
   // This won't be advised:
   UserService userService = new UserService();
   userService.createUser();  // No proxy, no advice
   
   // This will be advised:
   @Autowired
   UserService userService;  // Spring proxy
   ```

3. **Self-invocation Problem**: Methods calling other methods in same class
   ```java
   @Service
   public class UserService {
       public void methodA() {
           methodB();  // Won't be advised!
       }
       
       @Transactional
       public void methodB() {
           // Transaction advice won't apply
       }
   }
   ```

4. **Proxy-based Limitations**:
    - **Final methods**: Cannot be proxied (CGLIB)
    - **Final classes**: Cannot be proxied (CGLIB)
    - **Private methods**: Not intercepted
    - **Static methods**: Not intercepted

5. **Performance Overhead**: Each advice adds execution time
   ```java
   // Each advice adds call stack depth
   proxy.method() → Before advice → After advice → Around advice
   ```

6. **Configuration Complexity**: Multiple aspects can interact in unexpected ways
   ```java
   @Order(1)
   @Aspect class LoggingAspect { ... }
   
   @Order(2)
   @Aspect class SecurityAspect { ... }
   
   @Order(3)
   @Aspect class TransactionAspect { ... }
   // Order matters and can be confusing
   ```

7. **Limited Pointcut Expressions**: Cannot use all AspectJ pointcuts
   ```java
   // These AspectJ PCDs don't work in Spring AOP:
   - call(): Method calls (only execution works)
   - get(): Field get operations
   - set(): Field set operations
   - initialization(): Object initialization
   - preinitialization(): Before constructor
   ```

8. **No Compile-time Checking**: Pointcut expressions are evaluated at runtime
   ```java
   // This will fail at runtime, not compile time:
   @Pointcut("execution(* com.nonexistent.Service.*(..))")
   ```

9. **Circular Dependencies**: Can cause issues with proxy creation
   ```java
   @Service
   class ServiceA {
       @Autowired ServiceB b;
   }
   
   @Service
   class ServiceB {
       @Autowired ServiceA a;  // Circular dependency with proxies
   }
   ```

10. **Testing Complexity**: Aspects can make unit testing more difficult
    ```java
    @Test
    public void testService() {
        // Need to test with and without aspects
        // Mock aspects for isolation testing
    }
    ```

**Workarounds and Solutions:**

1. **Use AspectJ** for more advanced requirements
2. **Restructure code** to avoid limitations
3. **Use interface-based proxies** for better compatibility
4. **Careful ordering** of aspects
5. **Comprehensive testing** of advised methods

**When to consider alternatives:**
- Need field-level interception → Use AspectJ
- Need constructor advice → Use AspectJ
- High-performance requirements → Consider manual implementation
- Complex AOP requirements → Use full AspectJ

## Summary

Spring AOP is a powerful tool for implementing cross-cutting concerns in a clean, modular way. While it has limitations compared to full AspectJ, it provides sufficient functionality for most enterprise applications and integrates seamlessly with the Spring ecosystem.

**Key Takeaways:**
1. Use AOP for cross-cutting concerns (logging, security, transactions)
2. Choose the right type of advice for your use case
3. Write specific pointcut expressions for better performance
4. Be aware of limitations like self-invocation
5. Test aspects thoroughly
6. Consider AspectJ for advanced requirements

By mastering Spring AOP, you can write cleaner, more maintainable code that separates concerns effectively while leveraging the full power of the Spring framework.