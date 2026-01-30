# Spring Data REST - Complete Guide

## Table of Contents
1. [Introduction to Spring Data REST](#introduction)
2. [Core Concepts](#core-concepts)
3. [Basic Implementation](#basic-implementation)
4. [Advanced Features](#advanced-features)
5. [Customization and Configuration](#customization)
6. [Security Considerations](#security)
7. [Interview Questions & Answers](#interview-qa)
8. [Practical Examples](#examples)
9. [Best Practices](#best-practices)

---

## 1. Introduction to Spring Data REST <a name="introduction"></a>

### What is Spring Data REST?
Spring Data REST is part of the larger Spring Data family that automatically exposes Spring Data repositories as RESTful endpoints. It eliminates the need to write boilerplate controller code by automatically generating REST APIs based on your entity models and repository interfaces.

### Key Benefits:
- **Rapid Development**: Automatically creates REST endpoints from repositories
- **HATEOAS Compliance**: Implements Hypermedia as the Engine of Application State
- **Standardized API**: Follows REST conventions and standards
- **Reduced Boilerplate**: No need for @RestController or @RequestMapping annotations

---

## 2. Core Concepts <a name="core-concepts"></a>

### 2.1 Repository Resources
- **Collection Resource**: Represents all entities (e.g., `/api/users`)
- **Item Resource**: Represents a single entity (e.g., `/api/users/1`)
- **Association Resource**: Represents relationships between entities

### 2.2 HTTP Methods Mapping
```
GET    /api/users          → findAll()
GET    /api/users/{id}     → findById()
POST   /api/users          → save()
PUT    /api/users/{id}     → save() for update
PATCH  /api/users/{id}     → partial update
DELETE /api/users/{id}     → deleteById()
```

### 2.3 HATEOAS (Hypermedia)
Spring Data REST responses include links to related resources, making the API discoverable:
- `_links`: Contains links to self, related collections, and associations
- `_embedded`: Contains the actual data
- `page`: Pagination information

---

## 3. Basic Implementation <a name="basic-implementation"></a>

### 3.1 Setup and Configuration

**pom.xml Dependencies:**
```xml
<!-- Main dependencies needed -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-rest</artifactId>
</dependency>
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
<groupId>com.h2database</groupId>
<artifactId>h2</artifactId>
<scope>runtime</scope>
</dependency>
```

**application.properties:**
```properties
# Configure base path for all REST endpoints
spring.data.rest.base-path=/api

# Enable/disable default exposure
spring.data.rest.default-media-type=application/hal+json

# Show repository REST endpoints in actuator
management.endpoints.web.exposure.include=*

# H2 Database configuration (for example)
spring.datasource.url=jdbc:h2:mem:testdb
spring.h2.console.enabled=true
```

### 3.2 Basic Entity Example

```java
package com.example.demo.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * User Entity Class
 * This class represents the User table in the database.
 * Each instance of this class corresponds to a row in the User table.
 * 
 * Why we use @Entity: 
 * - Marks this class as a JPA entity (database table)
 * - Spring Data JPA will automatically create a table based on this class
 * 
 * @Entity: Indicates this is a JPA entity class
 * @Table: Specifies the table name in database (optional, defaults to class name)
 */
@Entity
@Table(name = "users")  // Explicitly names the table as "users" in database
public class User {
    
    /**
     * Primary Key Field
     * 
     * Why @Id: 
     * - Marks this field as the primary key of the table
     * - Primary key uniquely identifies each record in the table
     * 
     * Why @GeneratedValue: 
     * - Configures how the primary key should be generated
     * - Strategy=IDENTITY: Database automatically generates the ID (auto-increment)
     * 
     * @Id: Marks this field as primary key
     * @GeneratedValue: Defines how the ID is generated
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * User's First Name Field
     * 
     * Why @Column:
     * - Maps Java field to database column
     * - nullable=false: Database column cannot be null
     * - length=50: Database column maximum length is 50 characters
     * 
     * Constraints ensure data integrity at database level
     */
    @Column(nullable = false, length = 50)
    private String firstName;
    
    @Column(nullable = false, length = 50)
    private String lastName;
    
    /**
     * Email Field with Unique Constraint
     * 
     * Why unique=true:
     * - Ensures no two users can have the same email
     * - Creates unique index in database for performance
     */
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    /**
     * Role Field with Default Value
     * 
     * Why @Enumerated(EnumType.STRING):
     * - Stores enum value as string in database (e.g., "USER", "ADMIN")
     * - More readable in database than storing ordinal numbers
     * 
     * Why Role.USER as default:
     * - Ensures new users get USER role by default
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;
    
    /**
     * Audit Fields - Created Date
     * 
     * Why @CreationTimestamp:
     * - Automatically sets field to current timestamp when entity is created
     * - Managed by Hibernate, no need to set manually
     * 
     * Why updatable=false:
     * - Once set, this field cannot be updated
     * - Preserves original creation time
     */
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * Audit Fields - Updated Date
     * 
     * Why @UpdateTimestamp:
     * - Automatically updates field to current timestamp when entity is modified
     * - Tracks last modification time
     */
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    // Getters and Setters
    // (Required for JPA and JSON serialization/deserialization)
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

/**
 * Role Enumeration
 * 
 * Why use Enum:
 * - Defines fixed set of valid roles
 * - Type-safe: Compiler prevents invalid values
 * - Database stores string values, not magic numbers
 */
enum Role {
    USER,      // Regular application user
    ADMIN,     // Administrative user with full access
    MODERATOR  // User with limited administrative rights
}
```

### 3.3 Basic Repository Example

```java
package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * User Repository Interface
 * 
 * Why extend JpaRepository:
 * - Provides CRUD operations (Create, Read, Update, Delete)
 * - Provides pagination and sorting out of the box
 * - No need to implement methods, Spring Data generates implementation
 * 
 * @RepositoryRestResource: Customizes REST endpoint exposure
 * - path="users": Changes endpoint from /users to /users
 * - collectionResourceRel="users": Changes collection name in HAL responses
 * 
 * Spring Data REST will automatically expose endpoints at:
 * - GET /api/users → Get all users
 * - POST /api/users → Create new user
 * - GET /api/users/{id} → Get user by ID
 * - PUT /api/users/{id} → Update user
 * - DELETE /api/users/{id} → Delete user
 */
@RepositoryRestResource(
    path = "users",                     // Custom path for REST endpoint
    collectionResourceRel = "users"     // Name of collection in HAL JSON
)
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by email - Custom Query Method
     * 
     * Why this works without implementation:
     * - Spring Data JPA parses method name and creates query automatically
     * - Pattern: findBy{PropertyName}
     * - Automatically exposed as: GET /api/users/search/findByEmail?email=...
     * 
     * @param email User's email address to search for
     * @return User entity if found, null otherwise
     */
    User findByEmail(String email);
    
    /**
     * Find users by last name - Returns List
     * 
     * Why List<User>:
     * - Can return multiple users with same last name
     * - Automatically exposed as search endpoint
     * 
     * Generated Query: SELECT * FROM users WHERE last_name = ?
     */
    List<User> findByLastName(String lastName);
    
    /**
     * Find users by role with pagination
     * 
     * Why Page<User>:
     * - Returns paginated results
     * - Includes page metadata (total elements, total pages, etc.)
     * - Automatically handles sorting and pagination parameters
     * 
     * Exposed as: GET /api/users/search/findByRole?role=USER&page=0&size=10
     */
    Page<User> findByRole(Role role, Pageable pageable);
    
    /**
     * Custom query using @Query annotation
     * 
     * Why @Query:
     * - More control over the JPQL/SQL query
     * - Complex queries that can't be expressed by method name
     * - Performance optimizations
     * 
     * JPQL (Java Persistence Query Language) works with entities, not tables
     */
    @Query("SELECT u FROM User u WHERE u.firstName LIKE %:name% OR u.lastName LIKE %:name%")
    List<User> findByNameContaining(@Param("name") String name);
}
```

### 3.4 Testing the Basic Implementation

**Access the REST API:**
```
GET http://localhost:8080/api/users
GET http://localhost:8080/api/users/1
GET http://localhost:8080/api/users/search/findByEmail?email=test@example.com
POST http://localhost:8080/api/users
    Content-Type: application/json
    {
        "firstName": "John",
        "lastName": "Doe",
        "email": "john@example.com"
    }
```

---

## 4. Advanced Features <a name="advanced-features"></a>

### 4.1 Associations and Relationships

```java
package com.example.demo.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Department Entity with One-to-Many Relationship
 * 
 * One Department can have Many Employees
 * This demonstrates bidirectional relationship in JPA
 */
@Entity
@Table(name = "departments")
public class Department {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    /**
     * One-to-Many Relationship with Employee
     * 
     * Why @OneToMany:
     * - Defines one department to many employees relationship
     * - mappedBy="department": Employee entity owns the relationship
     * - cascade=CascadeType.ALL: Operations cascade to employees
     * - orphanRemoval=true: Removes employees when removed from collection
     * 
     * Effect: Deleting department will delete all its employees
     * Spring Data REST exposes: /api/departments/{id}/employees
     */
    @OneToMany(
        mappedBy = "department",        // Field name in Employee entity
        cascade = CascadeType.ALL,      // Cascade all operations
        orphanRemoval = true            // Remove orphaned employees
    )
    private List<Employee> employees = new ArrayList<>();
    
    // Helper method to manage bidirectional relationship
    public void addEmployee(Employee employee) {
        employees.add(employee);
        employee.setDepartment(this);  // Set back reference
    }
    
    // Getters and setters...
}

/**
 * Employee Entity with Many-to-One Relationship
 */
@Entity
@Table(name = "employees")
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    /**
     * Many-to-One Relationship with Department
     * 
     * Why @ManyToOne:
     * - Many employees can belong to one department
     * - @JoinColumn: Foreign key column in employees table
     * 
     * Effect: Employees can be accessed through department resource
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
    
    // Getters and setters...
}
```

### 4.2 Projections

```java
package com.example.demo.projection;

import com.example.demo.entity.User;
import org.springframework.data.rest.core.config.Projection;

/**
 * User Projection Interface
 * 
 * Why use Projections:
 * - Control which fields are exposed in REST responses
 * - Reduce payload size by excluding sensitive/unnecessary fields
 * - Create different views for different clients
 * - Customize nested object representations
 * 
 * @Projection: Marks interface as a Spring Data REST projection
 * - types={User.class}: Applies to User entity
 * - name="summary": Name used in URL (?projection=summary)
 */
@Projection(name = "summary", types = { User.class })
public interface UserSummary {
    
    /**
     * Only expose these fields in the projection
     * 
     * Why these fields only:
     * - id: For identification
     * - firstName and lastName: Basic user info
     * - role: User's permission level
     * - Exclude email (sensitive), timestamps (internal use)
     */
    Long getId();
    String getFirstName();
    String getLastName();
    String getRole();  // Note: Returns enum name as string
    
    /**
     * Virtual Property - Full Name
     * 
     * Why virtual property:
     * - Not stored in database
     * - Computed at runtime
     * - Convenient for clients
     * 
     * Spring Data REST will include this in JSON response
     */
    default String getFullName() {
        return getFirstName() + " " + getLastName();
    }
    
    /**
     * Virtual Property - Role Display Name
     * 
     * Custom formatting for role display
     */
    default String getRoleDisplay() {
        return "Role: " + getRole().toUpperCase();
    }
}

/**
 * Detailed Projection with Nested Objects
 * 
 * Shows advanced projection with complex data
 */
@Projection(name = "detailed", types = { User.class })
public interface UserDetailed {
    
    // Include all basic fields
    Long getId();
    String getFirstName();
    String getLastName();
    String getEmail();
    String getRole();
    
    /**
     * Formatted Dates
     * 
     * Why format dates:
     * - Better user experience
     * - Consistent date format across API
     * - Hide implementation details (LocalDateTime)
     */
    default String getCreatedDate() {
        // Implementation would format createdAt
        return "Formatted Date";
    }
}

/**
 * Usage in API calls:
 * GET /api/users/1?projection=summary
 * GET /api/users?projection=detailed
 */
```

### 4.3 Custom Controllers with Spring Data REST

```java
package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Custom Controller for User Repository
 * 
 * Why @RepositoryRestController:
 * - Integrates with Spring Data REST infrastructure
 * - Base path is automatically configured (/api)
 * - Works alongside auto-generated endpoints
 * 
 * Use cases for custom controllers:
 * 1. Custom business logic not covered by CRUD
 * 2. Complex validation
 * 3. Integration with external services
 * 4. Custom response formats
 */
@RepositoryRestController
@RequestMapping("/users")  // Maps to /api/users (base path + /users)
public class UserCustomController {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Custom Endpoint - Activate User
     * 
     * Why custom endpoint:
     * - Business logic not typical CRUD operation
     * - Requires additional validation
     * - Might trigger other actions (send email, audit log, etc.)
     * 
     * Endpoint: PATCH /api/users/{id}/activate
     */
    @PatchMapping("/{id}/activate")
    public ResponseEntity<?> activateUser(@PathVariable Long id) {
        return userRepository.findById(id)
            .map(user -> {
                // Custom business logic
                user.setActive(true);
                user.setActivatedAt(LocalDateTime.now());
                
                // Save changes
                User savedUser = userRepository.save(user);
                
                /**
                 * Why EntityModel:
                 * - Wraps entity with HATEOAS links
                 * - Maintains consistency with Spring Data REST responses
                 * - Automatically includes self link
                 */
                EntityModel<User> resource = EntityModel.of(savedUser);
                
                // Return 200 OK with resource
                return ResponseEntity.ok(resource);
            })
            .orElse(ResponseEntity.notFound().build());  // 404 if user not found
    }
    
    /**
     * Custom Search Endpoint
     * 
     * Why custom search:
     * - Complex search criteria
     * - Full-text search
     * - Aggregation queries
     * 
     * Endpoint: GET /api/users/search/custom?query=...
     */
    @GetMapping("/search/custom")
    public ResponseEntity<?> customSearch(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        // Implement custom search logic
        // Could use Specifications, QueryDSL, or native queries
        
        return ResponseEntity.ok(/* search results */);
    }
    
    /**
     * Custom Validation Endpoint
     * 
     * Why separate validation endpoint:
     * - Pre-flight validation before saving
     * - Check uniqueness, business rules
     * - Return specific validation messages
     */
    @PostMapping("/validate")
    public ResponseEntity<?> validateUser(@RequestBody User user) {
        Map<String, String> errors = new HashMap<>();
        
        // Custom validation logic
        if (userRepository.existsByEmail(user.getEmail())) {
            errors.put("email", "Email already exists");
        }
        
        if (errors.isEmpty()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body(errors);
        }
    }
}
```

### 4.4 Events and Auditing

```java
package com.example.demo.listener;

import com.example.demo.entity.User;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Repository Event Handler
 * 
 * Why use Repository Event Handlers:
 * - Execute logic before/after repository operations
 * - Audit logging
 * - Data validation
 * - Business rule enforcement
 * - Send notifications
 * 
 * @RepositoryEventHandler: Marks class as event handler for repositories
 * - User.class: Only handles events for User entity
 */
@Component
@RepositoryEventHandler(User.class)
public class UserEventHandler {
    
    /**
     * Handle Before Create Event
     * 
     * Why @HandleBeforeCreate:
     * - Executes before user is saved to database
     * - Good for setting defaults, validation
     * - Can modify entity before persistence
     * 
     * Use cases:
     * - Set audit fields
     * - Encrypt passwords
     * - Generate unique identifiers
     */
    @HandleBeforeCreate
    public void handleUserBeforeCreate(User user) {
        System.out.println("About to create user: " + user.getEmail());
        
        // Set default values
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        
        // Generate username if not provided
        if (user.getUsername() == null) {
            user.setUsername(generateUsername(user.getEmail()));
        }
        
        // Audit logging
        logAudit("CREATE", user.getEmail(), "Before creation");
    }
    
    /**
     * Handle After Create Event
     * 
     * Why @HandleAfterCreate:
     * - Executes after user is saved to database
     * - Entity has ID generated
     * - Good for post-processing, notifications
     * 
     * Use cases:
     * - Send welcome email
     * - Update search indexes
     * - Cache population
     */
    @HandleAfterCreate
    public void handleUserAfterCreate(User user) {
        System.out.println("Created user with ID: " + user.getId());
        
        // Send welcome email
        sendWelcomeEmail(user);
        
        // Update audit log
        logAudit("CREATE", user.getEmail(), "After creation");
    }
    
    /**
     * Handle Before Delete Event
     * 
     * Why @HandleBeforeDelete:
     * - Executes before user is deleted
     * - Good for validation, cascading actions
     * - Can prevent deletion based on business rules
     */
    @HandleBeforeDelete
    public void handleUserBeforeDelete(User user) {
        System.out.println("About to delete user: " + user.getId());
        
        // Check if user can be deleted
        if (user.getRole() == Role.ADMIN) {
            throw new RuntimeException("Cannot delete admin users");
        }
        
        // Archive user data before deletion
        archiveUserData(user);
    }
    
    /**
     * Handle Before Link Save (for associations)
     * 
     * Why @HandleBeforeLinkSave:
     * - Executes before association is saved
     * - Good for validating relationships
     */
    @HandleBeforeLinkSave
    public void handleBeforeLinkSave(User user, Object linked) {
        System.out.println("About to link: " + linked + " to user: " + user.getId());
    }
    
    private String generateUsername(String email) {
        return email.split("@")[0];
    }
    
    private void sendWelcomeEmail(User user) {
        // Implementation for sending email
    }
    
    private void logAudit(String action, String identifier, String message) {
        // Implementation for audit logging
    }
    
    private void archiveUserData(User user) {
        // Implementation for data archiving
    }
}
```

---

## 5. Customization and Configuration <a name="customization"></a>

### 5.1 Global Configuration

```java
package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 * Global REST Configuration Class
 * 
 * Why RepositoryRestConfigurer:
 * - Centralized configuration for all repositories
 * - Customize default behaviors
 * - Configure CORS, exposure, metadata
 * 
 * This configuration applies to ALL Spring Data REST repositories
 */
@Configuration
public class RestConfig implements RepositoryRestConfigurer {
    
    /**
     * Configure Repository REST Configuration
     * 
     * Why configure here:
     * - Set global defaults
     * - Control which IDs are exposed
     * - Configure pagination defaults
     * - Set base path for all endpoints
     */
    @Override
    public void configureRepositoryRestConfiguration(
            RepositoryRestConfiguration config,
            CorsRegistry cors) {
        
        /**
         * Expose Entity IDs
         * 
         * Why expose IDs:
         * - By default, IDs are not included in HAL responses
         * - Clients might need IDs for custom operations
         * - Useful for debugging and logging
         */
        config.exposeIdsFor(
            User.class,
            Department.class,
            Employee.class
        );
        
        /**
         * Configure Base Path
         * 
         * Why set base path:
         * - Version your API
         * - Separate from other endpoints
         * - Follow RESTful naming conventions
         */
        config.setBasePath("/api/v1");
        
        /**
         * Configure Default Page Size
         * 
         * Why set default page size:
         * - Prevent too large responses
         * - Improve performance
         * - Better user experience
         */
        config.setDefaultPageSize(20);
        config.setMaxPageSize(100);
        
        /**
         * Configure Default Media Type
         * 
         * Why HAL JSON:
         * - Standard for hypermedia APIs
         * - Self-descriptive
         * - Includes links to related resources
         */
        config.setDefaultMediaType(org.springframework.http.MediaType.APPLICATION_JSON);
        
        /**
         * Configure CORS (Cross-Origin Resource Sharing)
         * 
         * Why configure CORS:
         * - Control which domains can access API
         * - Security measure
         * - Required for web applications on different domains
         */
        cors.addMapping("/api/**")
            .allowedOrigins("http://localhost:3000", "https://example.com")
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600);
        
        /**
         * Configure Repository Exposure
         * 
         * Why control exposure:
         * - Hide internal repositories
         * - Control API surface area
         * - Security through obscurity
         */
        config.getExposureConfiguration()
            .forDomainType(User.class)
            .withItemExposure((metadata, httpMethods) -> 
                httpMethods.disable(HttpMethod.PATCH))  // Disable PATCH for users
            .withCollectionExposure((metadata, httpMethods) -> 
                httpMethods.disable(HttpMethod.DELETE)); // Disable bulk delete
    }
    
    /**
     * Configure HAL Explorer (API Browser)
     * 
     * Why HAL Explorer:
     * - Interactive API documentation
     * - Test endpoints in browser
     * - Discover available operations
     */
    @Override
    public void configureHalExplorer(HalExplorerConfiguration config) {
        config.withAutoRedirect(false)  // Don't auto-redirect to HAL browser
              .withMediaType(org.springframework.http.MediaType.APPLICATION_JSON);
    }
}
```

### 5.2 Resource Processors

```java
package com.example.demo.processor;

import com.example.demo.entity.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

/**
 * User Resource Processor
 * 
 * Why Resource Processors:
 * - Modify HAL representations before they're sent to client
 * - Add custom links
 * - Add custom properties
 * - Transform data
 * 
 * This runs for every User resource returned by the API
 */
@Component
public class UserResourceProcessor 
        implements RepresentationModelProcessor<EntityModel<User>> {
    
    /**
     * Process User Resource
     * 
     * Why process resources:
     * - Add custom actions/links
     * - Include computed properties
     * - Add metadata
     * - Transform sensitive data
     */
    @Override
    public EntityModel<User> process(EntityModel<User> resource) {
        User user = resource.getContent();
        
        if (user != null) {
            /**
             * Add Custom Links
             * 
             * Why add custom links:
             * - Expose custom operations
             * - Guide clients through workflow
             * - Improve API discoverability
             */
            resource.add(
                linkTo(methodOn(UserCustomController.class)
                    .activateUser(user.getId()))
                    .withRel("activate")  // Link rel for activation
                    .withTitle("Activate User")  // Human-readable title
            );
            
            /**
             * Add Custom Properties
             * 
             * Why add properties:
             * - Include computed values
             * - Add metadata not in entity
             * - Improve client experience
             */
            resource.getContent().setAdditionalProperty(
                "profileUrl", 
                "/profiles/" + user.getId()
            );
            
            /**
             * Transform Sensitive Data
             * 
             * Why transform data:
             * - Hide internal implementation
             * - Format for client consumption
             * - Remove sensitive information
             */
            if (user.getEmail() != null) {
                // Mask email for non-admin users (example)
                String maskedEmail = maskEmail(user.getEmail());
                resource.getContent().setEmail(maskedEmail);
            }
        }
        
        return resource;
    }
    
    private String maskEmail(String email) {
        // Simple email masking for demonstration
        String[] parts = email.split("@");
        if (parts.length == 2) {
            String username = parts[0];
            String domain = parts[1];
            
            if (username.length() > 2) {
                return username.substring(0, 2) + "***@" + domain;
            }
        }
        return "***@***";
    }
}
```

---

## 6. Security Considerations <a name="security"></a>

### 6.1 Basic Security Configuration

```java
package com.example.demo.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Security Configuration for Spring Data REST
 * 
 * Why security configuration:
 * - Protect sensitive data
 * - Control access to endpoints
 * - Prevent unauthorized operations
 * - Implement business rules
 * 
 * @EnableWebSecurity: Enables Spring Security
 * @EnableGlobalMethodSecurity: Enables method-level security
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            /**
             * Disable CSRF for REST API
             * 
             * Why disable CSRF:
             * - REST APIs are stateless
             * - Typically used by non-browser clients
             * - Use tokens (JWT) instead for security
             * 
             * Note: Enable if API is used by browser clients
             */
            .csrf().disable()
            
            /**
             * Configure Authorization Rules
             * 
             * Why configure authorization:
             * - Different roles have different access
             * - Protect sensitive operations
             * - Implement business logic in security layer
             */
            .authorizeRequests()
                // Public endpoints
                .antMatchers(HttpMethod.GET, "/api/**").permitAll()
                .antMatchers("/api/public/**").permitAll()
                
                // User registration - allow without authentication
                .antMatchers(HttpMethod.POST, "/api/users").permitAll()
                
                // User-specific operations - require authentication
                .antMatchers(HttpMethod.PUT, "/api/users/**").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/users/**").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                
                // Admin-only endpoints
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                
                // Search endpoints
                .antMatchers("/api/users/search/**").authenticated()
                
                // All other API endpoints require authentication
                .antMatchers("/api/**").authenticated()
                
                // Allow access to H2 console (for development only)
                .antMatchers("/h2-console/**").permitAll()
                
            .and()
            
            /**
             * Configure HTTP Basic Authentication
             * 
             * Why HTTP Basic:
             * - Simple to implement
             * - Good for internal APIs
             * - Combine with HTTPS for security
             * 
             * For production, consider OAuth2 or JWT
             */
            .httpBasic()
            
            .and()
            
            /**
             * Configure Headers for H2 Console
             * 
             * Why disable frame options:
             * - H2 console uses frames
             * - Development only feature
             */
            .headers().frameOptions().disable();
    }
}
```

### 6.2 Method-Level Security

```java
package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Secure User Repository
 * 
 * Demonstrates method-level security annotations
 * Combined with Spring Data REST
 */
@RepositoryRestResource(path = "users")
public interface SecureUserRepository extends JpaRepository<User, Long> {
    
    /**
     * Method-Level Security - PreAuthorize
     * 
     * Why @PreAuthorize:
     * - Check permissions before method execution
     * - Can access method parameters
     * - SpEL (Spring Expression Language) for complex rules
     * 
     * Only admins can delete users by email
     */
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    void deleteById(Long id);
    
    /**
     * Method-Level Security - PostAuthorize
     * 
     * Why @PostAuthorize:
     * - Check permissions after method execution
     * - Can access return value
     * - Useful for filtering sensitive data
     * 
     * Users can only see their own details, admins can see all
     */
    @RestResource(path = "byEmail", rel = "byEmail")
    @PostAuthorize("hasRole('ADMIN') or returnObject.email == authentication.name")
    User findByEmail(String email);
    
    /**
     * Custom Query with Security
     * 
     * Why secure custom queries:
     * - Control data visibility
     * - Implement row-level security
     * - Business-specific rules
     */
    @Query("SELECT u FROM User u WHERE u.department.name = :deptName")
    @PreAuthorize("hasPermission(#deptName, 'READ')")
    List<User> findByDepartmentName(@Param("deptName") String deptName);
    
    /**
     * Hide Sensitive Endpoint
     * 
     * Why @RestResource(exported = false):
     * - Hide from REST API
     * - Internal use only
     * - Security through obscurity
     */
    @RestResource(exported = false)
    List<User> findBySensitiveField(String sensitiveField);
}
```

---

## 7. Interview Questions & Answers <a name="interview-qa"></a>

### Basic Level Questions (For Freshers)

#### Q1: What is Spring Data REST and how does it differ from creating REST controllers manually?

**Answer:**
Spring Data REST automatically exposes Spring Data repositories as RESTful web services, whereas manual REST controllers require you to write each endpoint explicitly.

**Key Differences:**

1. **Development Speed:**
   ```java
   // Spring Data REST - Auto-generated
   // Just create repository interface
   public interface UserRepository extends JpaRepository<User, Long> {}
   // Automatically gets: GET, POST, PUT, DELETE endpoints
   
   // Manual Controller - Code each endpoint
   @RestController
   public class UserController {
       @GetMapping("/users") public List<User> getAll() { /* implementation */ }
       @PostMapping("/users") public User create() { /* implementation */ }
       // ... and so on for each endpoint
   }
   ```

2. **HATEOAS Compliance:**
    - Spring Data REST: Built-in HATEOAS with `_links` and `_embedded`
    - Manual: Need to implement HATEOAS manually

3. **Consistency:**
    - Spring Data REST: Standardized endpoints across all entities
    - Manual: Can vary between developers

**Example - Manual vs Auto-generated:**

```java
// MANUAL APPROACH - More control, more code
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    // ... 10+ more endpoints
}

// SPRING DATA REST APPROACH - Less code, standardized
@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long> {
    // That's it! All endpoints auto-generated
}
```

#### Q2: Explain the automatic endpoint generation in Spring Data REST with an example.

**Answer:**
Spring Data REST inspects repository interfaces and automatically creates REST endpoints based on the entity structure and repository methods.

**Endpoint Generation Rules:**

1. **Collection Resource:** `GET /api/{repository-name}`
2. **Item Resource:** `GET /api/{repository-name}/{id}`
3. **Search Resources:** `GET /api/{repository-name}/search/{query-method}`

**Example Entity and Repository:**
```java
@Entity
public class Product {
    @Id
    private Long id;
    private String name;
    private BigDecimal price;
    // getters/setters
}

@RepositoryRestResource(path = "products")
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByName(String name);
    List<Product> findByPriceLessThan(BigDecimal price);
}
```

**Auto-generated Endpoints:**
```
GET    /api/products           → findAll() 
POST   /api/products           → save()
GET    /api/products/{id}      → findById()
PUT    /api/products/{id}      → save() for update
DELETE /api/products/{id}      → deleteById()
GET    /api/products/search/findByName?name=...
GET    /api/products/search/findByPriceLessThan?price=...
```

**HTTP Response Example:**
```json
{
  "_embedded": {
    "products": [
      {
        "name": "Laptop",
        "price": 999.99,
        "_links": {
          "self": { "href": "http://localhost:8080/api/products/1" },
          "product": { "href": "http://localhost:8080/api/products/1" }
        }
      }
    ]
  },
  "_links": {
    "self": { "href": "http://localhost:8080/api/products" },
    "profile": { "href": "http://localhost:8080/api/profile/products" }
  },
  "page": {
    "size": 20,
    "totalElements": 1,
    "totalPages": 1,
    "number": 0
  }
}
```

#### Q3: What is HATEOAS and how does Spring Data REST implement it?

**Answer:**
HATEOAS (Hypermedia as the Engine of Application State) is a constraint of REST that makes APIs discoverable by including links to related resources in responses.

**How Spring Data REST Implements HATEOAS:**

1. **Links in Responses:**
   ```json
   {
     "id": 1,
     "name": "John",
     "_links": {
       "self": { "href": "/api/users/1" },
       "users": { "href": "/api/users" },
       "address": { "href": "/api/users/1/address" }
     }
   }
   ```

2. **Collection Resources with Pagination:**
   ```json
   {
     "_embedded": { "users": [...] },
     "_links": {
       "first": { "href": "/api/users?page=0" },
       "self": { "href": "/api/users?page=1" },
       "next": { "href": "/api/users?page=2" },
       "last": { "href": "/api/users?page=5" }
     }
   }
   ```

3. **Search Resources:**
   ```json
   {
     "_links": {
       "findByName": { "href": "/api/users/search/findByName{?name}" },
       "findByEmail": { "href": "/api/users/search/findByEmail{?email}" }
     }
   }
   ```

**Benefits for Clients:**
- No need to hardcode URLs
- Can navigate API dynamically
- Discover new endpoints at runtime
- Self-documenting API structure

#### Q4: How do you customize the base path of Spring Data REST endpoints?

**Answer:**
There are multiple ways to customize the base path:

**Method 1: application.properties**
```properties
# Set base path for all REST endpoints
spring.data.rest.base-path=/api/v1

# Result: All endpoints will be under /api/v1
# Example: /api/v1/users, /api/v1/products
```

**Method 2: Java Configuration**
```java
@Configuration
public class RestConfig implements RepositoryRestConfigurer {
    @Override
    public void configureRepositoryRestConfiguration(
            RepositoryRestConfiguration config) {
        config.setBasePath("/api/v2");
    }
}
```

**Method 3: Per-Repository Customization**
```java
@RepositoryRestResource(
    path = "custom-users",          // Changes path from "users" to "custom-users"
    collectionResourceRel = "users" // But keeps relation name as "users"
)
public interface UserRepository extends JpaRepository<User, Long> {
}

// Result: /api/custom-users (instead of /api/users)
```

**Complete Example:**
```java
// Entity
@Entity
public class Product { /* ... */ }

// Repository with custom path
@RepositoryRestResource(
    path = "items",                    // URL path
    collectionResourceRel = "products", // JSON relation name
    itemResourceRel = "product"        // Single item relation name
)
public interface ProductRepository extends JpaRepository<Product, Long> {
}

// Access at: GET /api/items
// Response includes: "_links": { "products": { "href": "/api/items" } }
```

#### Q5: Explain how pagination works in Spring Data REST with examples.

**Answer:**
Spring Data REST automatically supports pagination for collection resources. Clients can control page size and page number through query parameters.

**Default Pagination Parameters:**
- `page`: Page number (0-indexed)
- `size`: Number of items per page
- `sort`: Sorting criteria

**Example Requests:**
```
GET /api/users?page=0&size=10
GET /api/users?page=2&size=20&sort=name,asc
GET /api/users?sort=createdAt,desc&page=0
```

**Response Structure with Pagination:**
```json
{
  "_embedded": {
    "users": [
      { /* user 1 */ },
      { /* user 2 */ },
      // ... up to 'size' users
    ]
  },
  "_links": {
    "first": { "href": "/api/users?page=0&size=10" },
    "self": { "href": "/api/users?page=1&size=10" },
    "next": { "href": "/api/users?page=2&size=10" },
    "prev": { "href": "/api/users?page=0&size=10" },
    "last": { "href": "/api/users?page=5&size=10" }
  },
  "page": {
    "size": 10,
    "totalElements": 56,
    "totalPages": 6,
    "number": 1
  }
}
```

**Configuration Options:**
```java
@Configuration
public class RestConfig implements RepositoryRestConfigurer {
    @Override
    public void configureRepositoryRestConfiguration(
            RepositoryRestConfiguration config) {
        // Set global defaults
        config.setDefaultPageSize(20);
        config.setMaxPageSize(100);
        
        // Set page param name (default is "page")
        config.setPageParamName("pageNumber");
        
        // Set size param name (default is "size")
        config.setLimitParamName("pageSize");
    }
}
```

**Repository with Custom Pagination:**
```java
@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long> {
    // Method returning Page automatically supports pagination
    Page<User> findByActiveTrue(Pageable pageable);
    
    // Usage: GET /api/users/search/findByActiveTrue?page=0&size=5
}
```

**Benefits:**
- Efficient database queries (LIMIT/OFFSET)
- Reduced memory consumption
- Better user experience for large datasets
- Built-in metadata (total pages, total elements)

### Intermediate Level Questions

#### Q6: How do you handle relationships (associations) in Spring Data REST?

**Answer:**
Spring Data REST automatically exposes entity relationships as linkable resources. You can navigate from one resource to its related resources.

**Types of Relationships:**

1. **One-to-One:** `@OneToOne`
2. **One-to-Many:** `@OneToMany`
3. **Many-to-One:** `@ManyToOne`
4. **Many-to-Many:** `@ManyToMany`

**Example - Department and Employees:**
```java
@Entity
public class Department {
    @Id
    private Long id;
    private String name;
    
    @OneToMany(mappedBy = "department")
    private List<Employee> employees;
}

@Entity
public class Employee {
    @Id
    private Long id;
    private String name;
    
    @ManyToOne
    private Department department;
}
```

**Auto-generated Relationship Endpoints:**
```
# Department resource includes employee links
GET /api/departments/1
Response:
{
  "name": "Engineering",
  "_links": {
    "self": { "href": "/api/departments/1" },
    "employees": { "href": "/api/departments/1/employees" }
  }
}

# Access department's employees
GET /api/departments/1/employees
Response:
{
  "_embedded": {
    "employees": [
      { "name": "John", "_links": { ... } }
    ]
  }
}

# Create employee for department
POST /api/departments/1/employees
Body: { "name": "Jane" }
```

**Managing Relationships through Links:**
```json
// When creating/updating employee, link to department
POST /api/employees
{
  "name": "Alice",
  "department": "/api/departments/1"  // Link to department
}

// Or use PATCH to update relationship
PATCH /api/employees/1
{
  "department": "/api/departments/2"
}
```

**Customizing Relationship Exposure:**
```java
@Entity
public class User {
    @Id
    private Long id;
    
    // Expose as REST resource
    @OneToOne
    private Profile profile;
    
    // Hide from REST API
    @OneToMany
    @RestResource(exported = false)
    private List<AuditLog> auditLogs;
}

// Access profile: GET /api/users/1/profile
// auditLogs is not exposed
```

#### Q7: What are projections in Spring Data REST and when would you use them?

**Answer:**
Projections define which fields of an entity should be exposed in the REST API. They allow creating different views of the same entity for different clients or use cases.

**Why Use Projections:**
1. **Security:** Hide sensitive fields
2. **Performance:** Reduce payload size
3. **Customization:** Different views for different clients
4. **Transformation:** Format or compute derived values

**Example - User Entity with Projections:**
```java
@Entity
public class User {
    private Long id;
    private String username;
    private String email;
    private String passwordHash;  // Sensitive!
    private LocalDateTime createdAt;
    private String address;
    // ... other fields
}

// Projection 1: Public view (hides sensitive data)
@Projection(name = "public", types = User.class)
public interface UserPublic {
    Long getId();
    String getUsername();
    
    default String getDisplayName() {
        return "User: " + getUsername();
    }
}

// Projection 2: Admin view (full details)
@Projection(name = "admin", types = User.class)
public interface UserAdmin {
    Long getId();
    String getUsername();
    String getEmail();
    LocalDateTime getCreatedAt();
}

// Projection 3: Summary view (minimal data)
@Projection(name = "summary", types = User.class)
public interface UserSummary {
    Long getId();
    String getUsername();
}
```

**Using Projections:**
```
GET /api/users/1                     → Full entity (all fields)
GET /api/users/1?projection=public   → Only public fields
GET /api/users?projection=summary    → Summary for all users
```

**Response with Projection:**
```json
// Without projection
{
  "id": 1,
  "username": "john",
  "email": "john@example.com",
  "passwordHash": "...",  // Sensitive!
  "createdAt": "...",
  "address": "..."
}

// With projection=public
{
  "id": 1,
  "username": "john",
  "displayName": "User: john",  // Computed field
  "_links": { ... }
}
```

**Configuring Default Projection:**
```java
@Configuration
public class RestConfig implements RepositoryRestConfigurer {
    @Override
    public void configureRepositoryRestConfiguration(
            RepositoryRestConfiguration config) {
        // Register projections
        config.getProjectionConfiguration()
            .addProjection(UserPublic.class)
            .addProjection(UserAdmin.class)
            .addProjection(UserSummary.class);
    }
}
```

**Advanced Projection with Nested Objects:**
```java
@Entity
public class Order {
    private Long id;
    private BigDecimal total;
    
    @ManyToOne
    private User user;
}

@Projection(name = "withUser", types = Order.class)
public interface OrderWithUser {
    Long getId();
    BigDecimal getTotal();
    
    // Include user projection
    UserSummary getUser();
    
    default String getFormattedTotal() {
        return "$" + getTotal().toString();
    }
}

// Response includes nested user with summary projection
{
  "id": 1,
  "total": 99.99,
  "formattedTotal": "$99.99",
  "user": {
    "id": 1,
    "username": "john"
  }
}
```

#### Q8: How do you add custom endpoints to a Spring Data REST application?

**Answer:**
You can extend Spring Data REST functionality by adding custom controllers using `@RepositoryRestController`. These controllers integrate with the existing Spring Data REST infrastructure.

**Approach 1: RepositoryRestController**
```java
@RepositoryRestController  // Integrates with Spring Data REST
@RequestMapping("/users")  // Base path relative to spring.data.rest.base-path
public class UserCustomController {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Custom search endpoint
     * URL: /api/users/search/customSearch
     */
    @GetMapping("/search/customSearch")
    public ResponseEntity<?> customSearch(@RequestParam String query) {
        // Custom logic
        List<User> users = userRepository.findByCustomQuery(query);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Custom action on user resource
     * URL: /api/users/{id}/activate
     */
    @PostMapping("/{id}/activate")
    public ResponseEntity<?> activateUser(@PathVariable Long id) {
        return userRepository.findById(id)
            .map(user -> {
                user.setActive(true);
                userRepository.save(user);
                return ResponseEntity.ok().build();
            })
            .orElse(ResponseEntity.notFound().build());
    }
}
```

**Approach 2: Custom Repository Implementation**
```java
// 1. Custom interface with additional methods
public interface UserRepositoryCustom {
    User activateUser(Long id);
    List<User> findActiveUsers();
}

// 2. Implement custom methods
@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public User activateUser(Long id) {
        User user = entityManager.find(User.class, id);
        user.setActive(true);
        return entityManager.merge(user);
    }
    
    @Override
    public List<User> findActiveUsers() {
        return entityManager.createQuery(
            "SELECT u FROM User u WHERE u.active = true", User.class)
            .getResultList();
    }
}

// 3. Extend both interfaces
@RepositoryRestResource
public interface UserRepository 
        extends JpaRepository<User, Long>, UserRepositoryCustom {
    // Auto-exposes custom methods as search endpoints
    // GET /api/users/search/findActiveUsers
}
```

**Approach 3: Resource Processors**
```java
@Component
public class UserResourceProcessor 
        implements RepresentationModelProcessor<EntityModel<User>> {
    
    @Override
    public EntityModel<User> process(EntityModel<User> resource) {
        // Add custom links
        resource.add(
            Link.of("/api/users/" + resource.getContent().getId() + "/activate")
                .withRel("activate")
        );
        
        // Add custom properties
        if (resource.getContent().getEmail() != null) {
            resource.getContent().setAdditionalProperty(
                "emailDomain", 
                extractDomain(resource.getContent().getEmail())
            );
        }
        
        return resource;
    }
}
```

**Best Practices for Custom Endpoints:**
1. Use `@RepositoryRestController` for consistency
2. Follow REST conventions for URL structure
3. Return proper HTTP status codes
4. Include HATEOAS links in responses
5. Document custom endpoints clearly

#### Q9: Explain event handlers in Spring Data REST with examples.

**Answer:**
Event handlers allow you to execute custom logic before or after repository operations. They're useful for auditing, validation, notifications, and business rules.

**Types of Events:**
- `@HandleBeforeCreate`: Before entity is created
- `@HandleAfterCreate`: After entity is created
- `@HandleBeforeSave`: Before entity is saved (create or update)
- `@HandleAfterSave`: After entity is saved
- `@HandleBeforeDelete`: Before entity is deleted
- `@HandleAfterDelete`: After entity is deleted
- `@HandleBeforeLinkSave`: Before association is saved
- `@HandleAfterLinkSave`: After association is saved

**Example: User Event Handler**
```java
@Component
@RepositoryEventHandler(User.class)  // Handle events for User entity
public class UserEventHandler {
    
    private final AuditService auditService;
    private final EmailService emailService;
    
    public UserEventHandler(AuditService auditService, EmailService emailService) {
        this.auditService = auditService;
        this.emailService = emailService;
    }
    
    /**
     * Before creating user - validation and defaults
     */
    @HandleBeforeCreate
    public void handleUserBeforeCreate(User user) {
        System.out.println("About to create user: " + user.getEmail());
        
        // Validate email format
        if (!isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        // Set default values
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        
        // Generate username from email
        if (user.getUsername() == null) {
            user.setUsername(user.getEmail().split("@")[0]);
        }
        
        // Set creation timestamp
        user.setCreatedAt(LocalDateTime.now());
    }
    
    /**
     * After creating user - notifications
     */
    @HandleAfterCreate
    public void handleUserAfterCreate(User user) {
        System.out.println("Created user with ID: " + user.getId());
        
        // Send welcome email
        emailService.sendWelcomeEmail(user.getEmail());
        
        // Log to audit trail
        auditService.logEvent(
            "USER_CREATED", 
            user.getId(), 
            "User created via REST API"
        );
        
        // Update search index
        updateSearchIndex(user);
    }
    
    /**
     * Before updating user - validation
     */
    @HandleBeforeSave
    public void handleUserBeforeSave(User user) {
        System.out.println("About to save user: " + user.getId());
        
        // Prevent modification of certain fields
        if (user.getCreatedAt() == null) {
            throw new IllegalStateException("Created date cannot be null");
        }
        
        // Update modification timestamp
        user.setUpdatedAt(LocalDateTime.now());
        
        // Validate business rules
        if (user.getRole() == Role.ADMIN && !user.isVerified()) {
            throw new IllegalStateException("Admin users must be verified");
        }
    }
    
    /**
     * Before deleting user - check constraints
     */
    @HandleBeforeDelete
    public void handleUserBeforeDelete(User user) {
        System.out.println("About to delete user: " + user.getId());
        
        // Check if user can be deleted
        if (user.getRole() == Role.ADMIN) {
            throw new IllegalStateException("Cannot delete admin users");
        }
        
        // Check for active orders
        if (hasActiveOrders(user)) {
            throw new IllegalStateException("User has active orders");
        }
        
        // Archive user data before deletion
        archiveUserData(user);
    }
    
    /**
     * After deleting user - cleanup
     */
    @HandleAfterDelete
    public void handleUserAfterDelete(User user) {
        System.out.println("Deleted user: " + user.getId());
        
        // Remove from cache
        userCache.evict(user.getId());
        
        // Log deletion
        auditService.logEvent(
            "USER_DELETED",
            user.getId(),
            "User deleted via REST API"
        );
    }
    
    /**
     * Handle relationship events
     */
    @HandleBeforeLinkSave
    public void handleBeforeLinkSave(User user, Object linked) {
        System.out.println("Linking " + linked + " to user " + user.getId());
        
        // Validate relationship
        if (linked instanceof Department) {
            Department dept = (Department) linked;
            if (!dept.isActive()) {
                throw new IllegalStateException("Cannot link to inactive department");
            }
        }
    }
    
    // Helper methods
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }
    
    private boolean hasActiveOrders(User user) {
        // Check database for active orders
        return false; // simplified
    }
    
    private void archiveUserData(User user) {
        // Archive to separate storage
    }
    
    private void updateSearchIndex(User user) {
        // Update Elasticsearch/Solr index
    }
}
```

**Registering Event Handlers:**
```java
@Configuration
public class EventHandlerConfig {
    
    @Bean
    public UserEventHandler userEventHandler() {
        return new UserEventHandler();
    }
    
    // OR use @ComponentScan to auto-detect @RepositoryEventHandler
}
```

**Common Use Cases for Event Handlers:**
1. **Auditing:** Log all CRUD operations
2. **Validation:** Enforce business rules
3. **Notifications:** Send emails on user creation
4. **Derived Values:** Calculate and set computed fields
5. **Caching:** Update cache on data changes
6. **Search Indexing:** Update search engine indices
7. **Data Transformation:** Encrypt sensitive fields
8. **Default Values:** Set creation timestamps

**Important Considerations:**
- Event handlers are synchronous (blocking)
- Exceptions in handlers will rollback transaction
- Order of multiple handlers is not guaranteed
- Use `@Order` annotation to control execution order
- Keep handler logic lightweight to avoid performance issues

#### Q10: How do you secure Spring Data REST endpoints?

**Answer:**
Spring Data REST can be secured using Spring Security. Security can be applied at different levels: URL patterns, repository methods, or individual entities.

**Comprehensive Security Configuration:**
```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for API (enable if browser clients)
            .csrf().disable()
            
            // Configure authorization rules
            .authorizeRequests()
                // Public endpoints
                .antMatchers(
                    "/api/public/**",
                    "/api/users/search/findByEmail**",
                    "/api/profile/**"
                ).permitAll()
                
                // Read operations - authenticated users
                .antMatchers(HttpMethod.GET, "/api/**").authenticated()
                
                // Write operations - specific roles
                .antMatchers(HttpMethod.POST, "/api/users").permitAll()  // Registration
                .antMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/**").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.PATCH, "/api/**").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
                
                // Specific entity security
                .antMatchers("/api/orders/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers("/api/reports/**").hasRole("MANAGER")
                
                // Repository search endpoints
                .antMatchers("/api/users/search/**").authenticated()
                
                // Default - deny all other API access
                .antMatchers("/api/**").denyAll()
                
            .and()
            
            // Authentication methods
            .httpBasic()  // Basic auth for simplicity
            .and()
            
            // Session management
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);  // REST is stateless
    }
}
```

**Method-Level Security:**
```java
@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Only admins can delete users
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    void delete(User user);
    
    // Users can only see their own profile
    @RestResource(path = "myProfile", rel = "myProfile")
    @PostAuthorize("returnObject.username == authentication.name or hasRole('ADMIN')")
    User findByUsername(String username);
    
    // Custom query with security
    @Query("SELECT u FROM User u WHERE u.department = :dept")
    @PreAuthorize("hasPermission(#dept, 'READ')")
    List<User> findByDepartment(@Param("dept") Department department);
}
```

**Entity-Based Security:**
```java
@Entity
public class Document {
    @Id
    private Long id;
    private String content;
    
    // Owner can access
    private String ownerUsername;
    
    // Department restriction
    @ManyToOne
    private Department allowedDepartment;
    
    // Security level
    @Enumerated(EnumType.STRING)
    private SecurityLevel securityLevel;
}

// Security service to filter entities
@Component
public class DocumentSecurityService {
    
    @PostFilter("hasRole('ADMIN') or filterObject.ownerUsername == authentication.name")
    public List<Document> filterByOwnership(List<Document> documents) {
        return documents;
    }
}
```

**Custom Security Evaluator:**
```java
@Component("documentSecurity")
public class DocumentSecurityEvaluator {
    
    public boolean canReadDocument(Document doc, String username) {
        return doc.getOwnerUsername().equals(username) ||
               doc.getSecurityLevel() == SecurityLevel.PUBLIC;
    }
    
    public boolean canDeleteDocument(Document doc, String username) {
        return doc.getOwnerUsername().equals(username) ||
               hasAdminRole(username);
    }
}

// Usage in security expressions
@PreAuthorize("@documentSecurity.canReadDocument(#id, authentication.name)")
```

**Securing Associations:**
```java
@Entity
public class User {
    @Id
    private Long id;
    private String username;
    
    // Hide sensitive association from API
    @OneToMany
    @RestResource(exported = false)  // Not exposed via REST
    private List<PaymentMethod> paymentMethods;
    
    // Expose but secure
    @OneToMany
    @RestResource(path = "myOrders")
    @PreAuthorize("hasRole('ADMIN') or #entity?.owner?.username == authentication.name")
    private List<Order> orders;
}
```

**JWT Token Security (Advanced):**
```java
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                .antMatchers("/api/public/**").permitAll()
                .antMatchers("/api/**").authenticated();
    }
}

// JWT Configuration
@Configuration
public class JwtConfig {
    
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("secret-key");
        return converter;
    }
}
```

**Security Best Practices:**
1. **Principle of Least Privilege:** Grant minimum necessary permissions
2. **Input Validation:** Validate all inputs at multiple layers
3. **HTTPS:** Always use HTTPS in production
4. **Rate Limiting:** Prevent abuse of API endpoints
5. **Audit Logging:** Log all security-relevant events
6. **Regular Updates:** Keep dependencies updated
7. **Security Headers:** Add security headers (CSP, HSTS)
8. **API Keys:** For server-to-server communication
9. **OAuth2:** For third-party application access
10. **Monitoring:** Monitor for suspicious activities

### Advanced Level Questions

#### Q11: How do you implement versioning in Spring Data REST APIs?

**Answer:**
API versioning is crucial for maintaining backward compatibility while evolving your API. Here are multiple approaches:

**Approach 1: URI Versioning (Most Common)**
```java
// Configuration
@Configuration
public class RestConfig implements RepositoryRestConfigurer {
    @Override
    public void configureRepositoryRestConfiguration(
            RepositoryRestConfiguration config) {
        config.setBasePath("/api/v1");
    }
}

// Access endpoints:
// GET /api/v1/users
// GET /api/v1/products
```

**Approach 2: Media Type Versioning (Content Negotiation)**
```java
// Custom media type with version
public class ApiVersion {
    public static final String V1 = "application/vnd.company.app-v1+json";
    public static final String V2 = "application/vnd.company.app-v2+json";
}

// Controller handling different versions
@RepositoryRestController
@RequestMapping(value = "/users", produces = {
    ApiVersion.V1,
    ApiVersion.V2,
    MediaType.APPLICATION_JSON_VALUE
})
public class VersionedUserController {
    
    @GetMapping
    public ResponseEntity<?> getUsers(
            HttpServletRequest request,
            @RequestHeader("Accept") String acceptHeader) {
        
        if (acceptHeader.contains("v2")) {
            // Version 2 response
            return ResponseEntity.ok(getUsersV2());
        } else {
            // Version 1 (default) response
            return ResponseEntity.ok(getUsersV1());
        }
    }
}

// Client requests:
// GET /api/users
// Accept: application/vnd.company.app-v2+json
```

**Approach 3: Custom Header Versioning**
```java
@Configuration
public class ApiVersionConfig implements RepositoryRestConfigurer {
    
    @Override
    public void configureRepositoryRestConfiguration(
            RepositoryRestConfiguration config) {
        
        // Custom resolver for API version
        config.setReturnBodyOnCreate(true);
        config.setReturnBodyOnUpdate(true);
    }
    
    @Bean
    public Filter apiVersionFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    FilterChain filterChain) throws ServletException, IOException {
                
                String apiVersion = request.getHeader("X-API-Version");
                if ("v2".equals(apiVersion)) {
                    request.setAttribute("api.version", "v2");
                } else {
                    request.setAttribute("api.version", "v1");
                }
                
                filterChain.doFilter(request, response);
            }
        };
    }
}

// Custom processor based on version
@Component
public class VersionedResourceProcessor 
        implements RepresentationModelProcessor<EntityModel<User>> {
    
    @Override
    public EntityModel<User> process(EntityModel<User> resource) {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            String version = (String) attrs.getAttribute("api.version", 
                RequestAttributes.SCOPE_REQUEST);
            
            if ("v2".equals(version)) {
                // Add v2-specific links
                resource.add(Link.of("/api/v2/users/" + 
                    resource.getContent().getId() + "/details")
                    .withRel("details"));
            }
        }
        return resource;
    }
}
```

**Approach 4: Separate Repositories per Version**
```java
// Version 1 Repository
@RepositoryRestResource(path = "v1/users", exported = false)
public interface UserRepositoryV1 extends JpaRepository<UserV1, Long> {
    // V1 entity model and methods
}

// Version 2 Repository
@RepositoryRestResource(path = "v2/users")
public interface UserRepositoryV2 extends JpaRepository<UserV2, Long> {
    // V2 entity model with new fields
}

// Entity evolution
@Entity
@Table(name = "users")
public class UserV1 {
    private Long id;
    private String name;
    private String email;
    // V1 fields only
}

@Entity
@Table(name = "users")
public class UserV2 {
    private Long id;
    private String name;
    private String email;
    private String phone;      // New in V2
    private LocalDate birthDate; // New in V2
    // Additional V2 fields
}
```

**Approach 5: Proxy-Based Versioning**
```java
@RestController
@RequestMapping("/api")
public class ApiGatewayController {
    
    @Autowired
    private UserRepositoryV1 userRepositoryV1;
    
    @Autowired
    private UserRepositoryV2 userRepositoryV2;
    
    @GetMapping("/{version}/users")
    public ResponseEntity<?> getUsers(
            @PathVariable String version,
            @RequestParam Map<String, String> allParams) {
        
        if ("v1".equals(version)) {
            return ResponseEntity.ok(userRepositoryV1.findAll());
        } else if ("v2".equals(version)) {
            Pageable pageable = PageRequest.of(
                Integer.parseInt(allParams.getOrDefault("page", "0")),
                Integer.parseInt(allParams.getOrDefault("size", "20"))
            );
            return ResponseEntity.ok(userRepositoryV2.findAll(pageable));
        }
        
        return ResponseEntity.badRequest().body("Unsupported version");
    }
}
```

**Versioning Strategy Comparison:**
| Strategy | Pros | Cons | Best For |
|----------|------|------|----------|
| **URI Versioning** | Simple, Clear, Cacheable | URI pollution, Breaking browser links | Public APIs, Mobile apps |
| **Header Versioning** | Clean URIs, No breaking changes | Less discoverable, Harder to debug | Internal APIs, Microservices |
| **Media Type** | Standards-compliant, Flexible | Complex implementation | Hypermedia APIs, Content negotiation |
| **Parameter** | Simple to implement | Not RESTful, Cache issues | Simple APIs, Quick prototypes |

**Migration Strategy Example:**
```java
// Phase 1: Add new fields to entity
@Entity
public class User {
    @Id
    private Long id;
    
    // V1 fields
    private String name;
    private String email;
    
    // V2 new fields (nullable initially)
    @Column(nullable = true)  // Allow null during migration
    private String phone;
    
    @Column(nullable = true)
    private LocalDate birthDate;
}

// Phase 2: Create projection for V2
@Projection(name = "v2", types = User.class)
public interface UserV2Projection {
    Long getId();
    String getName();
    String getEmail();
    String getPhone();      // Only in V2 projection
    LocalDate getBirthDate(); // Only in V2 projection
}

// Phase 3: Clients can choose
// V1: GET /api/users (default)
// V2: GET /api/users?projection=v2

// Phase 4: Deprecate V1 (after all clients migrated)
@Deprecated
@RepositoryRestResource(path = "users")
public interface UserRepositoryV1 extends JpaRepository<User, Long> {
    // Mark as deprecated in documentation
}
```

#### Q12: How do you handle transactions in Spring Data REST?

**Answer:**
Spring Data REST repositories are transactional by default, but understanding and controlling transactions is important for data consistency.

**Default Transaction Behavior:**
```java
@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long> {
    // All repository methods are transactional by default
    // @Transactional is applied automatically with default settings
}

// Equivalent to:
@RepositoryRestResource
@Transactional(readOnly = true)  // Default for query methods
public interface UserRepository extends JpaRepository<User, Long> {
    
    @Transactional  // Override for modify operations
    <S extends User> S save(S entity);
    
    @Transactional
    void delete(User entity);
}
```

**Custom Transaction Configuration:**
```java
@Configuration
@EnableTransactionManagement
public class TransactionConfig {
    
    @Bean
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
    
    @Bean
    public TransactionTemplate transactionTemplate(
            PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }
}
```

**Service Layer with Transactions:**
```java
@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuditService auditService;
    
    @Autowired
    private EmailService emailService;
    
    /**
     * Create user with multiple operations in one transaction
     * If any operation fails, all changes are rolled back
     */
    @Transactional
    public User createUserWithDependencies(User user) {
        // 1. Save user (in transaction)
        User savedUser = userRepository.save(user);
        
        // 2. Log audit (part of same transaction)
        auditService.logUserCreation(savedUser);
        
        // 3. Send email (outside transaction boundary)
        // If email fails, user creation is NOT rolled back
        try {
            emailService.sendWelcomeEmail(savedUser.getEmail());
        } catch (Exception e) {
            // Log but don't rollback user creation
            log.error("Failed to send welcome email", e);
        }
        
        return savedUser;
    }
    
    /**
     * Transfer balance between users
     * Requires atomic operation (both succeed or both fail)
     */
    @Transactional(rollbackFor = {InsufficientBalanceException.class})
    public void transferBalance(Long fromUserId, Long toUserId, BigDecimal amount) {
        User fromUser = userRepository.findById(fromUserId)
            .orElseThrow(() -> new UserNotFoundException(fromUserId));
        
        User toUser = userRepository.findById(toUserId)
            .orElseThrow(() -> new UserNotFoundException(toUserId));
        
        // Check balance
        if (fromUser.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                "User " + fromUserId + " has insufficient balance");
        }
        
        // Deduct from sender
        fromUser.setBalance(fromUser.getBalance().subtract(amount));
        userRepository.save(fromUser);
        
        // Add to receiver
        toUser.setBalance(toUser.getBalance().add(amount));
        userRepository.save(toUser);
        
        // Both saves are in same transaction
        // If exception occurs here, both changes are rolled back
    }
    
    /**
     * Read-only transaction for reporting
     */
    @Transactional(readOnly = true)
    public Report generateUserReport() {
        // Multiple read operations
        List<User> activeUsers = userRepository.findByActiveTrue();
        List<User> newUsers = userRepository.findByCreatedAfter(
            LocalDate.now().minusDays(30));
        
        // Generate report (read-only ensures no accidental writes)
        return new Report(activeUsers, newUsers);
    }
}
```

**Event Handlers with Transactions:**
```java
@Component
@RepositoryEventHandler(User.class)
@Transactional  // Apply to all handler methods
public class UserEventHandler {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuditLogRepository auditLogRepository;
    
    /**
     * Event handler with transaction
     * Runs in same transaction as the repository operation
     */
    @HandleBeforeCreate
    public void handleBeforeCreate(User user) {
        // This runs in the transaction that saves the user
        user.setCreatedAt(LocalDateTime.now());
        
        // Any exception here will rollback the entire transaction
        if (user.getEmail() == null) {
            throw new IllegalArgumentException("Email is required");
        }
    }
    
    /**
     * After create handler runs in NEW transaction
     * Use @Transactional(propagation = Propagation.REQUIRES_NEW)
     * to separate from main transaction
     */
    @HandleAfterCreate
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleAfterCreate(User user) {
        // Runs in separate transaction
        // If this fails, user creation is NOT rolled back
        
        AuditLog log = new AuditLog();
        log.setAction("USER_CREATED");
        log.setUserId(user.getId());
        log.setTimestamp(LocalDateTime.now());
        
        auditLogRepository.save(log);  // In separate transaction
    }
}
```

**Transaction Isolation Levels:**
```java
@Service
public class FinancialService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    /**
     * READ_COMMITTED: Default for most operations
     * Prevents dirty reads but allows non-repeatable reads
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void processPayment(Long accountId, BigDecimal amount) {
        // Standard isolation for payment processing
    }
    
    /**
     * SERIALIZABLE: Highest isolation, prevents all anomalies
     * Use for critical financial operations
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void executeFinancialTransaction(FinancialTransaction transaction) {
        // Guarantees complete isolation
        // Performance impact - use sparingly
    }
    
    /**
     * READ_UNCOMMITTED: Lowest isolation, allows dirty reads
     * Use for non-critical reports where performance matters
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public Report generateQuickReport() {
        // Fast but may see uncommitted data
        return new Report();
    }
}
```

**Programmatic Transaction Management:**
```java
@Service
public class BatchUserService {
    
    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Programmatic transaction control
     * For complex transaction boundaries
     */
    public void batchCreateUsers(List<User> users) {
        TransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(definition);
        
        try {
            for (User user : users) {
                userRepository.save(user);
                
                // Commit every 100 users to avoid huge transaction
                if (users.indexOf(user) % 100 == 0) {
                    transactionManager.commit(status);
                    status = transactionManager.getTransaction(definition);
                }
            }
            
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }
}
```

**Transaction Best Practices:**
1. **Keep Transactions Short:** Release database locks quickly
2. **Read-Only When Possible:** Improves performance
3. **Handle Exceptions Properly:** Know which exceptions cause rollback
4. **Avoid External Calls:** Don't call external services in transactions
5. **Use Appropriate Isolation:** Balance consistency vs performance
6. **Test Transaction Behavior:** Verify rollback scenarios
7. **Monitor Deadlocks:** Implement retry logic for deadlocks
8. **Use @Transactional Carefully:** Understand propagation behaviors

#### Q13: How do you implement caching with Spring Data REST?

**Answer:**
Caching improves performance by reducing database load. Spring provides multiple caching options that work well with Spring Data REST.

**Basic Cache Configuration:**
```java
// Enable caching in main application
@SpringBootApplication
@EnableCaching  // Enable Spring's caching abstraction
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// Cache configuration
@Configuration
public class CacheConfig {
    
    @Bean
    public CacheManager cacheManager() {
        // Using ConcurrentMapCacheManager for simplicity
        // For production, use Redis, Ehcache, or Hazelcast
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        cacheManager.setCacheNames(Arrays.asList(
            "users", 
            "products", 
            "departments"
        ));
        return cacheManager;
    }
    
    // For production with Redis
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(10))
            .serializeValuesWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new GenericJackson2JsonRedisSerializer()));
        
        return RedisCacheManager.builder(factory)
            .cacheDefaults(config)
            .build();
    }
}
```

**Entity-Level Caching:**
```java
@Entity
@Table(name = "users")
@Cacheable  // Mark entity as cacheable
@org.hibernate.annotations.Cache(
    usage = CacheConcurrencyStrategy.READ_WRITE,  // Hibernate second-level cache
    region = "users"  // Cache region name
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    private String email;
    
    @OneToMany(mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  // Cache relationships
    private List<Order> orders;
    
    // Getters and setters
}
```

**Repository Method Caching:**
```java
@RepositoryRestResource
@CacheConfig(cacheNames = "users")  // Default cache for all methods
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Cache individual user by ID
     * Cache key: userId parameter
     */
    @Override
    @Cacheable(key = "#p0")  // p0 refers to first parameter (id)
    Optional<User> findById(Long id);
    
    /**
     * Cache user by email
     * Custom cache key using SpEL
     */
    @Cacheable(key = "'user_email_' + #email")
    User findByEmail(String email);
    
    /**
     * Cache with condition
     * Only cache if username length > 3
     */
    @Cacheable(
        key = "#username",
        condition = "#username.length() > 3"
    )
    User findByUsername(String username);
    
    /**
     * Evict cache when updating
     * Remove cached entry for this user
     */
    @Override
    @CacheEvict(key = "#p0.id")  // Evict by user id
    <S extends User> S save(S entity);
    
    /**
     * Evict all users cache
     * When bulk operation changes many users
     */
    @CacheEvict(allEntries = true)
    void deleteInBatch(Iterable<User> users);
    
    /**
     * Cache with TTL (Time To Live)
     * Using Redis cache with expiration
     */
    @Cacheable(
        value = "user_search",
        key = "#lastName + '_' + #pageable.pageNumber",
        unless = "#result.getTotalElements() == 0"  // Don't cache empty results
    )
    Page<User> findByLastName(String lastName, Pageable pageable);
}
```

**Service Layer Caching:**
```java
@Service
@CacheConfig(cacheNames = "userService")
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuditService auditService;
    
    /**
     * Cache complex query results
     * Cache key includes all parameters
     */
    @Cacheable(key = "{#active, #role, #page, #size}")
    public Page<User> findActiveUsersByRole(
            boolean active, 
            Role role, 
            int page, 
            int size) {
        
        // Complex query logic
        Specification<User> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (active) {
                predicates.add(cb.isTrue(root.get("active")));
            }
            
            if (role != null) {
                predicates.add(cb.equal(root.get("role"), role));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(spec, pageable);
    }
    
    /**
     * Cache with refresh
     * Use @CachePut to update cache with new value
     */
    @CachePut(key = "#user.id")
    public User updateUser(User user) {
        User updated = userRepository.save(user);
        
        // Update audit log
        auditService.logUpdate(updated);
        
        return updated;  // This value replaces cache
    }
    
    /**
     * Conditional caching based on result
     * Only cache if user has orders
     */
    @Cacheable(
        key = "#userId",
        unless = "#result.orders.isEmpty()"  // Don't cache if no orders
    )
    public User getUserWithOrders(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
        
        // Force loading of orders (avoid lazy loading issues)
        user.getOrders().size();
        
        return user;
    }
    
    /**
     * Clear multiple cache entries
     * When operation affects multiple cache keys
     */
    @Caching(
        evict = {
            @CacheEvict(value = "users", key = "#userId"),
            @CacheEvict(value = "user_emails", allEntries = true),
            @CacheEvict(value = "user_search", allEntries = true)
        }
    )
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
        auditService.logDeletion(userId);
    }
}
```

**Cache Configuration with Profiles:**
```java
@Configuration
@Profile("dev")  // Development profile
public class DevCacheConfig {
    
    @Bean
    public CacheManager cacheManager() {
        // Simple in-memory cache for development
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
            new ConcurrentMapCache("users"),
            new ConcurrentMapCache("products")
        ));
        return cacheManager;
    }
}

@Configuration
@Profile("prod")  // Production profile
public class ProdCacheConfig {
    
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("redis.example.com");
        config.setPort(6379);
        config.setPassword(RedisPassword.of("secret"));
        
        return new JedisConnectionFactory(config);
    }
    
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        // Redis cache with different TTL per cache
        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
        
        cacheConfigs.put("users", RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(30))
            .serializeValuesWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new GenericJackson2JsonRedisSerializer())));
        
        cacheConfigs.put("products", RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofHours(2))
            .serializeValuesWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new GenericJackson2JsonRedisSerializer())));
        
        return RedisCacheManager.builder(factory)
            .withInitialCacheConfigurations(cacheConfigs)
            .build();
    }
}
```

**Cache Monitoring and Management:**
```java
@RestController
@RequestMapping("/api/admin/cache")
public class CacheManagementController {
    
    @Autowired
    private CacheManager cacheManager;
    
    /**
     * Get cache statistics
     */
    @GetMapping("/stats")
    public Map<String, Object> getCacheStats() {
        Map<String, Object> stats = new HashMap<>();
        
        cacheManager.getCacheNames().forEach(cacheName -> {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache instanceof RedisCache) {
                RedisCache redisCache = (RedisCache) cache;
                // Get Redis-specific statistics
                stats.put(cacheName, getRedisStats(redisCache));
            } else if (cache instanceof ConcurrentMapCache) {
                ConcurrentMapCache mapCache = (ConcurrentMapCache) cache;
                stats.put(cacheName, mapCache.getNativeCache().size());
            }
        });
        
        return stats;
    }
    
    /**
     * Clear specific cache
     */
    @PostMapping("/{cacheName}/clear")
    public ResponseEntity<?> clearCache(@PathVariable String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Clear all caches
     */
    @PostMapping("/clear-all")
    public ResponseEntity<?> clearAllCaches() {
        cacheManager.getCacheNames().forEach(name -> {
            Cache cache = cacheManager.getCache(name);
            if (cache != null) {
                cache.clear();
            }
        });
        return ResponseEntity.ok().build();
    }
}
```

**Cache Considerations for REST APIs:**
1. **HTTP Caching:** Use ETags and Last-Modified headers
2. **Cache Invalidation:** Implement proper invalidation strategies
3. **Cache-Aside Pattern:** Application manages cache population
4. **Write-Through:** Write to cache and database simultaneously
5. **Cache Stampede:** Prevent multiple threads populating same cache
6. **Cache Warming:** Pre-populate cache on application startup
7. **Monitoring:** Monitor cache hit/miss ratios
8. **TTL Strategy:** Different TTLs for different data types

**HTTP Caching with Spring Data REST:**
```java
@Entity
public class Product {
    @Id
    private Long id;
    private String name;
    private BigDecimal price;
    
    @Version  // For optimistic locking and ETag generation
    private Long version;
    
    @LastModifiedDate  // For Last-Modified header
    private LocalDateTime lastModified;
}

// ETag support in responses
@Configuration
public class CacheControlConfig implements RepositoryRestConfigurer {
    
    @Override
    public void configureRepositoryRestConfiguration(
            RepositoryRestConfiguration config) {
        
        // Enable ETag support
        config.setDefaultMediaType(MediaTypes.HAL_JSON);
        config.useHalAsDefaultJsonMediaType(false);
    }
    
    @Bean
    public Filter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }
}

// Client requests with caching headers
// First request
GET /api/products/1
Response:
ETag: "123456"
Last-Modified: Wed, 21 Oct 2020 07:28:00 GMT

// Subsequent request with If-None-Match
GET /api/products/1
If-None-Match: "123456"
Response: 304 Not Modified (no body)
```

#### Q14: How do you implement search functionality with Spring Data REST?

**Answer:**
Spring Data REST provides powerful search capabilities through repository query methods, specifications, and custom search endpoints.

**Basic Search Methods:**
```java
@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Basic field searches (auto-exposed as search endpoints)
    
    // Exact match
    // GET /api/users/search/findByEmail?email=test@example.com
    User findByEmail(String email);
    
    // Collection search
    // GET /api/users/search/findByRoleIn?roles=USER,ADMIN
    List<User> findByRoleIn(List<Role> roles);
    
    // Null check
    // GET /api/users/search/findByLastNameIsNull
    List<User> findByLastNameIsNull();
    
    // Not null check
    // GET /api/users/search/findByFirstNameIsNotNull
    List<User> findByFirstNameIsNotNull();
    
    // Boolean search
    // GET /api/users/search/findByActiveTrue
    List<User> findByActiveTrue();
    
    // GET /api/users/search/findByActiveFalse
    List<User> findByActiveFalse();
    
    // Comparison operators
    // GET /api/users/search/findByAgeGreaterThan?age=18
    List<User> findByAgeGreaterThan(Integer age);
    
    // GET /api/users/search/findByAgeLessThanEqual?age=65
    List<User> findByAgeLessThanEqual(Integer age);
    
    // GET /api/users/search/findBySalaryBetween?min=30000&max=50000
    List<User> findBySalaryBetween(BigDecimal min, BigDecimal max);
    
    // Date comparisons
    // GET /api/users/search/findByCreatedAtAfter?date=2020-01-01T00:00:00
    List<User> findByCreatedAtAfter(LocalDateTime date);
    
    // GET /api/users/search/findByCreatedAtBefore?date=2020-12-31T23:59:59
    List<User> findByCreatedAtBefore(LocalDateTime date);
    
    // String pattern matching
    // GET /api/users/search/findByFirstNameLike?name=Joh%
    List<User> findByFirstNameLike(String name);
    
    // GET /api/users/search/findByLastNameContaining?name=Doe
    List<User> findByLastNameContaining(String name);
    
    // GET /api/users/search/findByEmailStartingWith?prefix=admin
    List<User> findByEmailStartingWith(String prefix);
    
    // GET /api/users/search/findByEmailEndingWith?suffix=example.com
    List<User> findByEmailEndingWith(String suffix);
    
    // Case insensitive
    // GET /api/users/search/findByFirstNameIgnoreCase?name=john
    List<User> findByFirstNameIgnoreCase(String name);
    
    // Ordering results
    // GET /api/users/search/findByActiveTrueOrderByLastNameAsc
    List<User> findByActiveTrueOrderByLastNameAsc();
    
    // GET /api/users/search/findByRoleOrderByCreatedAtDesc?role=USER
    List<User> findByRoleOrderByCreatedAtDesc(Role role);
    
    // Paginated search
    // GET /api/users/search/findByRole?role=USER&page=0&size=10&sort=lastName,asc
    Page<User> findByRole(Role role, Pageable pageable);
    
    // Multiple conditions
    // GET /api/users/search/findByActiveTrueAndRole?active=true&role=USER
    List<User> findByActiveTrueAndRole(Role role);
    
    // GET /api/users/search/findByFirstNameOrLastName?firstName=John&lastName=Doe
    List<User> findByFirstNameOrLastName(String firstName, String lastName);
}
```

**Advanced Search with @Query:**
```java
@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long> {
    
    // JPQL queries
    @Query("SELECT u FROM User u WHERE u.email LIKE %:domain")
    // GET /api/users/search/findByEmailDomain?domain=example.com
    List<User> findByEmailDomain(@Param("domain") String domain);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    // GET /api/users/search/searchByName?name=john
    List<User> searchByName(@Param("name") String name);
    
    @Query("SELECT u FROM User u WHERE u.createdAt BETWEEN :start AND :end")
    // GET /api/users/search/findByCreatedBetween?start=2020-01-01&end=2020-12-31
    List<User> findByCreatedBetween(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end);
    
    // Native SQL queries
    @Query(value = "SELECT * FROM users WHERE age > :minAge " +
                   "ORDER BY created_at DESC LIMIT :limit",
           nativeQuery = true)
    // GET /api/users/search/findOlderThan?minAge=18&limit=100
    List<User> findOlderThan(@Param("minAge") Integer minAge, 
                            @Param("limit") Integer limit);
    
    // Aggregation queries
    @Query("SELECT COUNT(u) FROM User u WHERE u.active = true")
    // GET /api/users/search/countActiveUsers
    Long countActiveUsers();
    
    @Query("SELECT u.role, COUNT(u) FROM User u GROUP BY u.role")
    // GET /api/users/search/countByRole
    List<Object[]> countByRole();
    
    // JOIN queries
    @Query("SELECT u FROM User u JOIN u.department d WHERE d.name = :deptName")
    // GET /api/users/search/findByDepartmentName?deptName=Engineering
    List<User> findByDepartmentName(@Param("deptName") String deptName);
}
```

**Specifications for Dynamic Queries:**
```java
// Specification interface implementation
public class UserSpecifications {
    
    public static Specification<User> hasFirstName(String firstName) {
        return (root, query, cb) -> 
            firstName == null ? null : cb.equal(root.get("firstName"), firstName);
    }
    
    public static Specification<User> hasLastName(String lastName) {
        return (root, query, cb) -> 
            lastName == null ? null : cb.equal(root.get("lastName"), lastName);
    }
    
    public static Specification<User> hasRole(Role role) {
        return (root, query, cb) -> 
            role == null ? null : cb.equal(root.get("role"), role);
    }
    
    public static Specification<User> isActive(boolean active) {
        return (root, query, cb) -> cb.equal(root.get("active"), active);
    }
    
    public static Specification<User> createdAfter(LocalDateTime date) {
        return (root, query, cb) -> 
            date == null ? null : cb.greaterThan(root.get("createdAt"), date);
    }
    
    public static Specification<User> salaryBetween(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;
            
            if (min != null && max != null) {
                return cb.between(root.get("salary"), min, max);
            } else if (min != null) {
                return cb.greaterThanOrEqualTo(root.get("salary"), min);
            } else {
                return cb.lessThanOrEqualTo(root.get("salary"), max);
            }
        };
    }
    
    public static Specification<User> nameContains(String searchTerm) {
        return (root, query, cb) -> {
            if (searchTerm == null || searchTerm.trim().isEmpty()) return null;
            
            String pattern = "%" + searchTerm.toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get("firstName")), pattern),
                cb.like(cb.lower(root.get("lastName")), pattern)
            );
        };
    }
}

// Repository supporting specifications
@RepositoryRestResource
public interface UserRepository 
        extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    // Now supports findAll(Specification, Pageable)
}

// Custom search controller
@RepositoryRestController
@RequestMapping("/users")
public class UserSearchController {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Advanced search endpoint
     * GET /api/users/search/advanced?firstName=John&role=USER&active=true
     */
    @GetMapping("/search/advanced")
    public ResponseEntity<?> advancedSearch(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) 
                LocalDateTime createdAfter,
            @RequestParam(required = false) BigDecimal minSalary,
            @RequestParam(required = false) BigDecimal maxSalary,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "lastName,asc") String sort) {
        
        // Build specification from parameters
        Specification<User> spec = Specification.where(null);
        
        if (firstName != null) {
            spec = spec.and(UserSpecifications.hasFirstName(firstName));
        }
        
        if (lastName != null) {
            spec = spec.and(UserSpecifications.hasLastName(lastName));
        }
        
        if (role != null) {
            spec = spec.and(UserSpecifications.hasRole(role));
        }
        
        if (active != null) {
            spec = spec.and(UserSpecifications.isActive(active));
        }
        
        if (createdAfter != null) {
            spec = spec.and(UserSpecifications.createdAfter(createdAfter));
        }
        
        if (minSalary != null || maxSalary != null) {
            spec = spec.and(UserSpecifications.salaryBetween(minSalary, maxSalary));
        }
        
        if (searchTerm != null) {
            spec = spec.and(UserSpecifications.nameContains(searchTerm));
        }
        
        // Parse sort parameter
        Sort sortObj = parseSort(sort);
        Pageable pageable = PageRequest.of(page, size, sortObj);
        
        // Execute query
        Page<User> result = userRepository.findAll(spec, pageable);
        
        // Return as HAL response
        return ResponseEntity.ok(result);
    }
    
    /**
     * Full-text search endpoint
     * GET /api/users/search/fulltext?q=john+doe+engineer
     */
    @GetMapping("/search/fulltext")
    public ResponseEntity<?> fullTextSearch(
            @RequestParam String q,
            Pageable pageable) {
        
        // For full-text search, you might use:
        // 1. Database full-text search (PostgreSQL tsvector, MySQL FULLTEXT)
        // 2. Elasticsearch integration
        // 3. Apache Lucene
        
        // Example with native full-text search
        String sql = """
            SELECT * FROM users 
            WHERE to_tsvector('english', 
                   coalesce(first_name, '') || ' ' || 
                   coalesce(last_name, '') || ' ' || 
                   coalesce(email, '') || ' ' || 
                   coalesce(bio, '')) 
            @@ plainto_tsquery('english', :query)
            """;
        
        Query nativeQuery = entityManager.createNativeQuery(sql, User.class)
            .setParameter("query", q)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize());
        
        List<User> users = nativeQuery.getResultList();
        
        // Get total count
        String countSql = """
            SELECT COUNT(*) FROM users 
            WHERE to_tsvector('english', 
                   coalesce(first_name, '') || ' ' || 
                   coalesce(last_name, '') || ' ' || 
                   coalesce(email, '') || ' ' || 
                   coalesce(bio, '')) 
            @@ plainto_tsquery('english', :query)
            """;
        
        Long total = (Long) entityManager.createNativeQuery(countSql)
            .setParameter("query", q)
            .getSingleResult();
        
        Page<User> page = new PageImpl<>(users, pageable, total);
        
        return ResponseEntity.ok(page);
    }
    
    private Sort parseSort(String sortString) {
        if (sortString == null || sortString.isEmpty()) {
            return Sort.unsorted();
        }
        
        String[] parts = sortString.split(",");
        if (parts.length != 2) {
            return Sort.by(parts[0]);
        }
        
        Sort.Direction direction = parts[1].equalsIgnoreCase("desc") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        
        return Sort.by(direction, parts[0]);
    }
}
```

**Elasticsearch Integration:**
```java
// Elasticsearch repository
public interface UserSearchRepository 
        extends ElasticsearchRepository<UserDocument, Long> {
    
    // Auto-implemented search methods
    List<UserDocument> findByFirstName(String firstName);
    List<UserDocument> findByLastNameContaining(String lastName);
    
    // Custom search queries
    @Query("{\"bool\": {\"must\": [{\"match\": {\"firstName\": \"?0\"}}]}}")
    Page<UserDocument> findByFirstNameCustom(String firstName, Pageable pageable);
    
    // Full-text search across multiple fields
    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"firstName\", \"lastName\", \"email\", \"bio\"]}}")
    Page<UserDocument> fullTextSearch(String query, Pageable pageable);
}

// Sync service between database and Elasticsearch
@Service
public class SearchSyncService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserSearchRepository userSearchRepository;
    
    @EventListener
    public void handleUserCreated(HandleAfterCreateEvent event) {
        if (event.getEntity() instanceof User) {
            User user = (User) event.getEntity();
            indexUser(user);
        }
    }
    
    @EventListener
    public void handleUserUpdated(HandleAfterSaveEvent event) {
        if (event.getEntity() instanceof User) {
            User user = (User) event.getEntity();
            indexUser(user);
        }
    }
    
    @EventListener
    public void handleUserDeleted(HandleAfterDeleteEvent event) {
        if (event.getEntity() instanceof User) {
            User user = (User) event.getEntity();
            userSearchRepository.deleteById(user.getId());
        }
    }
    
    private void indexUser(User user) {
        UserDocument doc = convertToDocument(user);
        userSearchRepository.save(doc);
    }
    
    private UserDocument convertToDocument(User user) {
        UserDocument doc = new UserDocument();
        doc.setId(user.getId());
        doc.setFirstName(user.getFirstName());
        doc.setLastName(user.getLastName());
        doc.setEmail(user.getEmail());
        doc.setBio(user.getBio());
        doc.setCreatedAt(user.getCreatedAt());
        return doc;
    }
}

// Search controller using Elasticsearch
@RepositoryRestController
@RequestMapping("/users")
public class ElasticsearchUserController {
    
    @Autowired
    private UserSearchRepository userSearchRepository;
    
    /**
     * Elasticsearch-powered search
     * GET /api/users/search/elastic?q=search+term&page=0&size=10
     */
    @GetMapping("/search/elastic")
    public ResponseEntity<?> elasticSearch(
            @RequestParam String q,
            Pageable pageable) {
        
        Page<UserDocument> results = userSearchRepository.fullTextSearch(q, pageable);
        
        // Convert to DTO if needed
        List<UserSearchResult> dtos = results.getContent().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
        
        Page<UserSearchResult> page = new PageImpl<>(dtos, pageable, results.getTotalElements());
        
        return ResponseEntity.ok(page);
    }
}
```

**Search Optimization Techniques:**
1. **Indexing:** Create database indexes on frequently searched columns
2. **Partial Indexes:** Index only relevant data subsets
3. **Composite Indexes:** Index multiple columns used together
4. **Query Optimization:** Analyze and optimize slow queries
5. **Caching:** Cache frequent search results
6. **Pagination:** Always paginate large result sets
7. **Asynchronous Search:** For long-running searches
8. **Search Suggestions:** Implement autocomplete

**Search Best Practices:**
1. **Validate Input:** Sanitize search terms to prevent injection
2. **Limit Results:** Prevent returning too many records
3. **Timeout:** Implement query timeouts
4. **Monitoring:** Log search queries and performance
5. **Analytics:** Track popular search terms
6. **Synonyms:** Handle synonymous search terms
7. **Stemming:** Support word stemming (running → run)
8. **Fuzzy Search:** Support for typos and near matches

#### Q15: How do you handle file uploads and binary data in Spring Data REST?

**Answer:**
Handling files and binary data requires special considerations in REST APIs. Here are multiple approaches:

**Approach 1: Entity with @Lob (Large Object)**
```java
@Entity
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String contentType;
    private Long fileSize;
    
    /**
     * Store file content in database
     * @Lob: Large Object field for binary data
     * byte[]: Store as byte array in database
     * 
     * Pros: Transactional, backup included
     * Cons: Database bloat, performance impact
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)  // Lazy loading for performance
    @Column(columnDefinition = "LONGBLOB")  // MySQL specific
    private byte[] content;
    
    // Audit fields
    private LocalDateTime uploadedAt;
    private String uploadedBy;
    
    // Getters and setters
    
    /**
     * Helper method to set file with metadata
     */
    public void setFile(MultipartFile file, String username) throws IOException {
        this.name = file.getOriginalFilename();
        this.contentType = file.getContentType();
        this.fileSize = file.getSize();
        this.content = file.getBytes();
        this.uploadedAt = LocalDateTime.now();
        this.uploadedBy = username;
    }
    
    /**
     * Get file as Resource for download
     */
    public Resource getFileAsResource() {
        return new ByteArrayResource(this.content);
    }
}

// Repository
@RepositoryRestResource
public interface DocumentRepository extends JpaRepository<Document, Long> {
    
    // Find by name
    List<Document> findByNameContaining(String name);
    
    // Find by content type
    List<Document> findByContentType(String contentType);
}
```

**Approach 2: Store files in filesystem with metadata in database**
```java
@Entity
public class FileMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String originalFilename;
    private String storedFilename;  // Name on filesystem
    private String contentType;
    private Long fileSize;
    private String storagePath;     // Path on filesystem
    
    // Metadata
    private String uploadedBy;
    private LocalDateTime uploadedAt;
    private LocalDateTime lastAccessed;
    
    // Business data
    private String description;
    private String tags;
    
    // Getters and setters
}

@Service
public class FileStorageService {
    
    @Value("${file.storage.path:/uploads}")
    private String storagePath;
    
    @Autowired
    private FileMetadataRepository metadataRepository;
    
    /**
     * Store file on filesystem
     */
    public FileMetadata storeFile(MultipartFile file, String username) throws IOException {
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String storedFilename = UUID.randomUUID().toString() + extension;
        
        // Create storage directory if not exists
        Path storageDir = Paths.get(storagePath);
        Files.createDirectories(storageDir);
        
        // Save file to filesystem
        Path filePath = storageDir.resolve(storedFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // Save metadata to database
        FileMetadata metadata = new FileMetadata();
        metadata.setOriginalFilename(originalFilename);
        metadata.setStoredFilename(storedFilename);
        metadata.setContentType(file.getContentType());
        metadata.setFileSize(file.getSize());
        metadata.setStoragePath(filePath.toString());
        metadata.setUploadedBy(username);
        metadata.setUploadedAt(LocalDateTime.now());
        
        return metadataRepository.save(metadata);
    }
    
    /**
     * Retrieve file as Resource
     */
    public Resource loadFileAsResource(Long fileId) throws IOException {
        FileMetadata metadata = metadataRepository.findById(fileId)
            .orElseThrow(() -> new FileNotFoundException("File not found: " + fileId));
        
        // Update last accessed time
        metadata.setLastAccessed(LocalDateTime.now());
        metadataRepository.save(metadata);
        
        Path filePath = Paths.get(metadata.getStoragePath());
        Resource resource = new UrlResource(filePath.toUri());
        
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new FileNotFoundException("File not found: " + fileId);
        }
    }
    
    /**
     * Delete file and metadata
     */
    public void deleteFile(Long fileId) throws IOException {
        FileMetadata metadata = metadataRepository.findById(fileId)
            .orElseThrow(() -> new FileNotFoundException("File not found: " + fileId));
        
        // Delete from filesystem
        Path filePath = Paths.get(metadata.getStoragePath());
        Files.deleteIfExists(filePath);
        
        // Delete metadata
        metadataRepository.delete(metadata);
    }
    
    private String getFileExtension(String filename) {
        if (filename == null) return "";
        int lastDot = filename.lastIndexOf('.');
        return lastDot > 0 ? filename.substring(lastDot) : "";
    }
}
```

**Approach 3: Cloud Storage (AWS S3, Google Cloud Storage)**
```java
// AWS S3 Configuration
@Configuration
public class S3Config {
    
    @Value("${aws.s3.bucket-name}")
    private String bucketName;
    
    @Value("${aws.s3.region}")
    private String region;
    
    @Bean
    public AmazonS3 s3Client() {
        return AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .withCredentials(new DefaultAWSCredentialsProviderChain())
            .build();
    }
    
    @Bean
    public TransferManager transferManager(AmazonS3 s3Client) {
        return TransferManagerBuilder.standard()
            .withS3Client(s3Client)
            .build();
    }
}

// Cloud storage service
@Service
public class CloudStorageService {
    
    @Autowired
    private AmazonS3 s3Client;
    
    @Autowired
    private TransferManager transferManager;
    
    @Value("${aws.s3.bucket-name}")
    private String bucketName;
    
    @Autowired
    private FileMetadataRepository metadataRepository;
    
    /**
     * Upload file to S3
     */
    public FileMetadata uploadToS3(MultipartFile file, String username) throws IOException {
        // Generate unique key
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String s3Key = "uploads/" + UUID.randomUUID().toString() + extension;
        
        // Upload to S3
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        
        PutObjectRequest request = new PutObjectRequest(
            bucketName, s3Key, file.getInputStream(), metadata);
        
        // Set public read access (adjust based on requirements)
        request.withCannedAcl(CannedAccessControlList.PublicRead);
        
        s3Client.putObject(request);
        
        // Generate URL
        String fileUrl = s3Client.getUrl(bucketName, s3Key).toString();
        
        // Save metadata
        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setOriginalFilename(originalFilename);
        fileMetadata.setStoredFilename(s3Key);
        fileMetadata.setContentType(file.getContentType());
        fileMetadata.setFileSize(file.getSize());
        fileMetadata.setStoragePath(fileUrl);
        fileMetadata.setUploadedBy(username);
        fileMetadata.setUploadedAt(LocalDateTime.now());
        
        return metadataRepository.save(fileMetadata);
    }
    
    /**
     * Get file URL for download
     */
    public String getFileUrl(Long fileId) {
        FileMetadata metadata = metadataRepository.findById(fileId)
            .orElseThrow(() -> new FileNotFoundException("File not found: " + fileId));
        
        // Update last accessed
        metadata.setLastAccessed(LocalDateTime.now());
        metadataRepository.save(metadata);
        
        return metadata.getStoragePath();  // S3 URL
    }
    
    /**
     * Delete file from S3
     */
    public void deleteFromS3(Long fileId) {
        FileMetadata metadata = metadataRepository.findById(fileId)
            .orElseThrow(() -> new FileNotFoundException("File not found: " + fileId));
        
        // Delete from S3
        s3Client.deleteObject(bucketName, metadata.getStoredFilename());
        
        // Delete metadata
        metadataRepository.delete(metadata);
    }
}
```

**REST Controller for File Operations:**
```java
@RepositoryRestController
@RequestMapping("/documents")
public class DocumentController {
    
    @Autowired
    private DocumentService documentService;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    /**
     * Upload file
     * POST /api/documents/upload
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description,
            Principal principal) {
        
        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body("File cannot be empty");
            }
            
            if (file.getSize() > 10 * 1024 * 1024) { // 10MB limit
                return ResponseEntity.badRequest()
                    .body("File size exceeds limit");
            }
            
            // Check content type
            String contentType = file.getContentType();
            if (!isAllowedContentType(contentType)) {
                return ResponseEntity.badRequest()
                    .body("File type not allowed");
            }
            
            // Store file
            String username = principal.getName();
            Document document = documentService.storeDocument(file, username, description);
            
            // Return document with download link
            EntityModel<Document> resource = EntityModel.of(document);
            resource.add(linkTo(methodOn(DocumentController.class)
                .downloadDocument(document.getId(), null))
                .withRel("download"));
            
            return ResponseEntity.created(
                URI.create("/api/documents/" + document.getId()))
                .body(resource);
            
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to upload file: " + e.getMessage());
        }
    }
    
    /**
     * Download file
     * GET /api/documents/{id}/download
     */
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadDocument(
            @PathVariable Long id,
            @RequestHeader(value = "Range", required = false) String rangeHeader) {
        
        try {
            Document document = documentService.getDocument(id);
            
            // Check permissions
            if (!documentService.canDownload(document)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            // Get file as resource
            Resource resource = documentService.getFileAsResource(document);
            
            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, 
                "attachment; filename=\"" + document.getName() + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, document.getContentType());
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(document.getFileSize()));
            headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
            headers.add(HttpHeaders.PRAGMA, "no-cache");
            headers.add(HttpHeaders.EXPIRES, "0");
            
            // Handle partial content (range requests) for large files
            if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
                return handleRangeRequest(resource, rangeHeader, document);
            }
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
            
        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get file metadata
     * GET /api/documents/{id}/metadata
     */
    @GetMapping("/{id}/metadata")
    public ResponseEntity<?> getDocumentMetadata(@PathVariable Long id) {
        return documentService.getDocument(id)
            .map(document -> {
                // Create DTO with metadata only (no file content)
                DocumentMetadataDto dto = new DocumentMetadataDto();
                dto.setId(document.getId());
                dto.setName(document.getName());
                dto.setContentType(document.getContentType());
                dto.setFileSize(document.getFileSize());
                dto.setUploadedAt(document.getUploadedAt());
                dto.setUploadedBy(document.getUploadedBy());
                dto.setDescription(document.getDescription());
                
                // Add download link
                EntityModel<DocumentMetadataDto> resource = EntityModel.of(dto);
                resource.add(linkTo(methodOn(DocumentController.class)
                    .downloadDocument(document.getId(), null))
                    .withRel("download"));
                
                return ResponseEntity.ok(resource);
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Stream large files
     * GET /api/documents/{id}/stream
     */
    @GetMapping(value = "/{id}/stream", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public StreamingResponseBody streamDocument(@PathVariable Long id) {
        return outputStream -> {
            try {
                Document document = documentService.getDocument(id);
                byte[] content = document.getContent();
                
                // Stream in chunks
                int chunkSize = 1024 * 8; // 8KB chunks
                int offset = 0;
                
                while (offset < content.length) {
                    int length = Math.min(chunkSize, content.length - offset);
                    outputStream.write(content, offset, length);
                    outputStream.flush();
                    offset += length;
                    
                    // Small delay to simulate streaming
                    Thread.sleep(10);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error streaming file", e);
            }
        };
    }
    
    /**
     * Handle range requests for partial content
     */
    private ResponseEntity<Resource> handleRangeRequest(
            Resource resource, 
            String rangeHeader, 
            Document document) throws IOException {
        
        long fileSize = document.getFileSize();
        String[] ranges = rangeHeader.substring("bytes=".length()).split("-");
        
        long rangeStart = Long.parseLong(ranges[0]);
        long rangeEnd = ranges.length > 1 ? Long.parseLong(ranges[1]) : fileSize - 1;
        
        if (rangeEnd >= fileSize) {
            rangeEnd = fileSize - 1;
        }
        
        long contentLength = rangeEnd - rangeStart + 1;
        
        // Read partial content
        byte[] partialContent = new byte[(int) contentLength];
        try (InputStream inputStream = resource.getInputStream()) {
            inputStream.skip(rangeStart);
            inputStream.read(partialContent, 0, (int) contentLength);
        }
        
        ByteArrayResource partialResource = new ByteArrayResource(partialContent);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_RANGE, 
            "bytes " + rangeStart + "-" + rangeEnd + "/" + fileSize);
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength));
        headers.add(HttpHeaders.CONTENT_TYPE, document.getContentType());
        headers.add(HttpHeaders.ACCEPT_RANGES, "bytes");
        
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
            .headers(headers)
            .body(partialResource);
    }
    
    private boolean isAllowedContentType(String contentType) {
        // Define allowed content types
        Set<String> allowedTypes = Set.of(
            "application/pdf",
            "image/jpeg",
            "image/png",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        );
        
        return allowedTypes.contains(contentType);
    }
}
```

**Security Considerations for File Uploads:**
```java
@Configuration
public class FileUploadSecurityConfig {
    
    /**
     * Configure file upload security
     */
    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(10 * 1024 * 1024); // 10MB
        resolver.setMaxUploadSizePerFile(5 * 1024 * 1024); // 5MB per file
        resolver.setMaxInMemorySize(1 * 1024 * 1024); // 1MB in memory
        return resolver;
    }
    
    /**
     * Security filter for file uploads
     */
    @Bean
    public Filter fileUploadSecurityFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    FilterChain filterChain) throws ServletException, IOException {
                
                // Check for file upload attacks
                if (isMultipartRequest(request)) {
                    // Validate file names
                    validateFileNames(request);
                    
                    // Check for malicious content
                    if (containsMaliciousContent(request)) {
                        response.sendError(HttpStatus.BAD_REQUEST.value(), 
                            "Potential malicious content detected");
                        return;
                    }
                }
                
                filterChain.doFilter(request, response);
            }
            
            private boolean isMultipartRequest(HttpServletRequest request) {
                return request.getContentType() != null && 
                       request.getContentType().startsWith("multipart/");
            }
            
            private void validateFileNames(HttpServletRequest request) {
                // Validate file names to prevent path traversal
                // Check for ../, ..\, etc.
            }
            
            private boolean containsMaliciousContent(HttpServletRequest request) {
                // Check for suspicious patterns
                // This is a simplified example
                return false;
            }
        };
    }
}
```

**File Upload Best Practices:**
1. **Validate File Size:** Set maximum file size limits
2. **Validate Content Type:** Whitelist allowed file types
3. **Scan for Malware:** Integrate antivirus scanning
4. **Secure File Names:** Prevent path traversal attacks
5. **Store Outside Web Root:** Prevent direct access
6. **Use Secure URLs:** Generate temporary, signed URLs
7. **Implement Quotas:** Limit storage per user
8. **Cleanup Old Files:** Implement retention policies
9. **Backup Strategy:** Regular backups of important files
10. **Monitor Storage:** Monitor disk space usage

**Response DTO for File Metadata:**
```java
public class DocumentMetadataDto {
    private Long id;
    private String name;
    private String contentType;
    private Long fileSize;
    private LocalDateTime uploadedAt;
    private String uploadedBy;
    private String description;
    private String downloadUrl;
    
    // Getters and setters
}
```

---

## 8. Practical Examples <a name="examples"></a>

### Complete CRUD Application Example

**Entity:**
```java
@Entity
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String sku;
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    @Column(nullable = false)
    private BigDecimal price;
    
    @Column(nullable = false)
    private Integer quantityInStock;
    
    @Enumerated(EnumType.STRING)
    private ProductCategory category;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    // Constructors
    public Product() {}
    
    public Product(String sku, String name, BigDecimal price, Integer quantityInStock) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.quantityInStock = quantityInStock;
    }
    
    // Getters and setters
    // Business methods
    public void reduceStock(Integer quantity) {
        if (quantity > this.quantityInStock) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        this.quantityInStock -= quantity;
    }
    
    public void increaseStock(Integer quantity) {
        this.quantityInStock += quantity;
    }
}

enum ProductCategory {
    ELECTRONICS,
    CLOTHING,
    BOOKS,
    HOME_APPLIANCES,
    SPORTS
}
```

**Repository:**
```java
@RepositoryRestResource(
    path = "products",
    collectionResourceRel = "products",
    itemResourceRel = "product"
)
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Find by SKU
    Optional<Product> findBySku(String sku);
    
    // Find by category with pagination
    Page<Product> findByCategory(ProductCategory category, Pageable pageable);
    
    // Find products with low stock
    List<Product> findByQuantityInStockLessThan(Integer threshold);
    
    // Search by name containing
    @RestResource(path = "searchByName", rel = "searchByName")
    List<Product> findByNameContainingIgnoreCase(@Param("name") String name);
    
    // Find products in price range
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceRange(
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice);
    
    // Custom update - increase stock
    @Modifying
    @Query("UPDATE Product p SET p.quantityInStock = p.quantityInStock + :quantity WHERE p.id = :id")
    @Transactional
    int increaseStock(@Param("id") Long id, @Param("quantity") Integer quantity);
}
```

**Custom Controller:**
```java
@RepositoryRestController
@RequestMapping("/products")
public class ProductCustomController {
    
    @Autowired
    private ProductRepository productRepository;
    
    /**
     * Custom endpoint to reduce stock
     * PATCH /api/products/{id}/reduce-stock
     */
    @PatchMapping("/{id}/reduce-stock")
    public ResponseEntity<?> reduceStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        
        return productRepository.findById(id)
            .map(product -> {
                try {
                    product.reduceStock(quantity);
                    Product updated = productRepository.save(product);
                    
                    // Return with HATEOAS links
                    EntityModel<Product> resource = EntityModel.of(updated);
                    resource.add(linkTo(methodOn(ProductCustomController.class)
                        .getProduct(id)).withSelfRel());
                    
                    return ResponseEntity.ok(resource);
                    
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest()
                        .body(Map.of("error", e.getMessage()));
                }
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get product with inventory status
     * GET /api/products/{id}/inventory-status
     */
    @GetMapping("/{id}/inventory-status")
    public ResponseEntity<?> getInventoryStatus(@PathVariable Long id) {
        return productRepository.findById(id)
            .map(product -> {
                Map<String, Object> status = new HashMap<>();
                status.put("productId", product.getId());
                status.put("productName", product.getName());
                status.put("currentStock", product.getQuantityInStock());
                status.put("status", getStockStatus(product.getQuantityInStock()));
                status.put("lastUpdated", product.getUpdatedAt());
                
                // Add links
                status.put("_links", Map.of(
                    "self", Map.of("href", "/api/products/" + id + "/inventory-status"),
                    "product", Map.of("href", "/api/products/" + id),
                    "restock", Map.of("href", "/api/products/" + id + "/increase-stock")
                ));
                
                return ResponseEntity.ok(status);
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Bulk update endpoint
     * POST /api/products/bulk-update
     */
    @PostMapping("/bulk-update")
    public ResponseEntity<?> bulkUpdate(@RequestBody List<ProductUpdateDto> updates) {
        List<Product> updatedProducts = new ArrayList<>();
        
        for (ProductUpdateDto update : updates) {
            productRepository.findById(update.getProductId())
                .ifPresent(product -> {
                    if (update.getPrice() != null) {
                        product.setPrice(update.getPrice());
                    }
                    if (update.getQuantity() != null) {
                        product.setQuantityInStock(update.getQuantity());
                    }
                    updatedProducts.add(productRepository.save(product));
                });
        }
        
        return ResponseEntity.ok(updatedProducts);
    }
    
    private String getStockStatus(Integer quantity) {
        if (quantity <= 0) return "OUT_OF_STOCK";
        if (quantity < 10) return "LOW_STOCK";
        if (quantity < 50) return "MEDIUM_STOCK";
        return "HIGH_STOCK";
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        // This method is referenced in link creation
        return productRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}

// DTO for bulk update
class ProductUpdateDto {
    private Long productId;
    private BigDecimal price;
    private Integer quantity;
    
    // Getters and setters
}
```

**Event Handlers:**
```java
@Component
@RepositoryEventHandler(Product.class)
public class ProductEventHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductEventHandler.class);
    
    /**
     * Before creating product - validation and defaults
     */
    @HandleBeforeCreate
    public void handleBeforeCreate(Product product) {
        logger.info("Creating product: {}", product.getSku());
        
        // Validate SKU format
        if (!isValidSku(product.getSku())) {
            throw new IllegalArgumentException("Invalid SKU format");
        }
        
        // Set default category if not provided
        if (product.getCategory() == null) {
            product.setCategory(ProductCategory.ELECTRONICS);
        }
        
        // Ensure price is positive
        if (product.getPrice() != null && product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
    }
    
    /**
     * After creating product - notifications
     */
    @HandleAfterCreate
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleAfterCreate(Product product) {
        logger.info("Created product with ID: {}", product.getId());
        
        // Update search index
        updateSearchIndex(product);
        
        // Send notification for new high-value products
        if (product.getPrice().compareTo(new BigDecimal("1000")) > 0) {
            sendHighValueProductNotification(product);
        }
    }
    
    /**
     * Before deleting product - check constraints
     */
    @HandleBeforeDelete
    public void handleBeforeDelete(Product product) {
        logger.info("Deleting product: {}", product.getId());
        
        // Check if product has associated orders
        if (hasActiveOrders(product)) {
            throw new IllegalStateException(
                "Cannot delete product with active orders");
        }
        
        // Archive product data
        archiveProductData(product);
    }
    
    /**
     * After updating product - audit logging
     */
    @HandleAfterSave
    public void handleAfterSave(Product product) {
        logger.info("Updated product: {}", product.getId());
        
        // Log price changes
        logPriceChangeIfNeeded(product);
        
        // Update cache
        updateProductCache(product);
    }
    
    private boolean isValidSku(String sku) {
        return sku != null && sku.matches("[A-Z0-9]{3,}-[A-Z0-9]{3,}");
    }
    
    private boolean hasActiveOrders(Product product) {
        // Check database for active orders
        return false;
    }
    
    private void archiveProductData(Product product) {
        // Archive to historical database
    }
    
    private void updateSearchIndex(Product product) {
        // Update Elasticsearch index
    }
    
    private void sendHighValueProductNotification(Product product) {
        // Send email/Slack notification
    }
    
    private void logPriceChangeIfNeeded(Product product) {
        // Compare with previous price and log if changed significantly
    }
    
    private void updateProductCache(Product product) {
        // Update Redis cache
    }
}
```

**Testing the Application:**

**1. Create a Product:**
```bash
# POST request to create product
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "ELEC-001",
    "name": "Smartphone",
    "description": "Latest smartphone model",
    "price": 999.99,
    "quantityInStock": 100,
    "category": "ELECTRONICS"
  }'
```

**2. Get All Products:**
```bash
# GET request with pagination
curl "http://localhost:8080/api/products?page=0&size=10&sort=name,asc"
```

**3. Search Products:**
```bash
# Search by name
curl "http://localhost:8080/api/products/search/searchByName?name=smart"

# Find by category
curl "http://localhost:8080/api/products/search/findByCategory?category=ELECTRONICS&page=0&size=5"
```

**4. Update Stock:**
```bash
# Reduce stock using custom endpoint
curl -X PATCH "http://localhost:8080/api/products/1/reduce-stock?quantity=5"
```

**5. Get Inventory Status:**
```bash
# Get inventory status
curl "http://localhost:8080/api/products/1/inventory-status"
```

---

## 9. Best Practices <a name="best-practices"></a>

### 9.1 Design Best Practices

1. **Use Proper HTTP Methods:**
    - GET: Retrieve resources
    - POST: Create new resources
    - PUT: Replace entire resource
    - PATCH: Partial updates
    - DELETE: Remove resources

2. **Implement Proper Status Codes:**
    - 200: Successful GET
    - 201: Resource created
    - 204: No content (successful DELETE)
    - 400: Bad request
    - 401: Unauthorized
    - 403: Forbidden
    - 404: Not found
    - 409: Conflict
    - 500: Server error

3. **Version Your API:**
   ```properties
   spring.data.rest.base-path=/api/v1
   ```

4. **Use HATEOAS:**
    - Include links to related resources
    - Make API discoverable
    - Follow REST level 3 maturity model

### 9.2 Performance Best Practices

1. **Implement Pagination:**
   ```java
   // Always return Page instead of List for collections
   Page<User> findAll(Pageable pageable);
   ```

2. **Use Projections:**
   ```java
   // Return only necessary fields
   @Projection(name = "summary", types = User.class)
   public interface UserSummary {
       String getFirstName();
       String getLastName();
   }
   ```

3. **Enable Caching:**
   ```java
   @Cacheable("users")
   User findById(Long id);
   ```

4. **Lazy Loading for Associations:**
   ```java
   @ManyToOne(fetch = FetchType.LAZY)
   private Department department;
   ```

### 9.3 Security Best Practices

1. **Use HTTPS:** Always in production
2. **Implement Authentication:** Use Spring Security
3. **Validate Input:** At multiple layers
4. **Use DTOs:** Don't expose entities directly
5. **Implement Rate Limiting:** Prevent abuse
6. **Log Security Events:** Monitor for attacks
7. **Regular Security Updates:** Keep dependencies updated

### 9.4 Testing Best Practices

1. **Unit Test Repositories:**
   ```java
   @DataJpaTest
   public class UserRepositoryTest {
       @Autowired
       private UserRepository userRepository;
       
       @Test
       public void testFindByEmail() {
           User user = new User("test@example.com");
           userRepository.save(user);
           
           User found = userRepository.findByEmail("test@example.com");
           assertThat(found).isNotNull();
       }
   }
   ```

2. **Integration Test REST Endpoints:**
   ```java
   @SpringBootTest
   @AutoConfigureMockMvc
   public class UserControllerTest {
       
       @Autowired
       private MockMvc mockMvc;
       
       @Test
       public void testGetUsers() throws Exception {
           mockMvc.perform(get("/api/users"))
                  .andExpect(status().isOk())
                  .andExpect(jsonPath("$._embedded.users").exists());
       }
   }
   ```

3. **Test Security:**
   ```java
   @WithMockUser(roles = "ADMIN")
   @Test
   public void testAdminEndpoint() throws Exception {
       mockMvc.perform(get("/api/admin/users"))
              .andExpect(status().isOk());
   }
   ```

### 9.5 Monitoring and Maintenance

1. **Enable Actuator Endpoints:**
   ```properties
   management.endpoints.web.exposure.include=*
   management.endpoint.health.show-details=always
   ```

2. **Monitor Database Performance:**
    - Use connection pooling
    - Monitor slow queries
    - Optimize indexes

3. **Logging Strategy:**
   ```properties
   logging.level.org.springframework.data.rest=DEBUG
   logging.level.org.hibernate.SQL=DEBUG
   logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
   ```

4. **Health Checks:**
   ```java
   @Component
   public class DatabaseHealthIndicator implements HealthIndicator {
       
       @Autowired
       private DataSource dataSource;
       
       @Override
       public Health health() {
           try (Connection connection = dataSource.getConnection()) {
               if (connection.isValid(1000)) {
                   return Health.up().build();
               }
               return Health.down().build();
           } catch (SQLException e) {
               return Health.down(e).build();
           }
       }
   }
   ```

### 9.6 Common Pitfalls to Avoid

1. **N+1 Query Problem:**
   ```java
   // BAD: Causes N+1 queries
   @OneToMany(mappedBy = "user")
   private List<Order> orders;
   
   // GOOD: Use JOIN FETCH
   @Query("SELECT u FROM User u JOIN FETCH u.orders")
   List<User> findAllWithOrders();
   ```

2. **Circular References:**
   ```java
   // BAD: Circular JSON serialization
   @Entity
   public class User {
       @OneToMany(mappedBy = "user")
       private List<Order> orders;
   }
   
   @Entity
   public class Order {
       @ManyToOne
       private User user;
   }
   
   // GOOD: Use @JsonIgnore or DTOs
   @JsonIgnore
   @OneToMany(mappedBy = "user")
   private List<Order> orders;
   ```

3. **Open Session in View:**
   ```properties
   # BAD: Can cause performance issues
   spring.jpa.open-in-view=true
   
   # GOOD: Disable it
   spring.jpa.open-in-view=false
   ```

4. **Not Using Transactions:**
   ```java
   // BAD: No transaction boundary
   public void updateUser(User user) {
       userRepository.save(user);
       auditService.logUpdate(user);  // If this fails, user is saved but not logged
   }
   
   // GOOD: Use @Transactional
   @Transactional
   public void updateUser(User user) {
       userRepository.save(user);
       auditService.logUpdate(user);  // Both succeed or both fail
   }
   ```

---

## Conclusion

Spring Data REST is a powerful framework that can significantly reduce development time for REST APIs. By understanding its core concepts, customization options, and best practices, you can build robust, scalable, and maintainable RESTful services. Remember to:

1. **Start Simple:** Use auto-generated endpoints for basic CRUD
2. **Customize Gradually:** Add projections, event handlers, and custom controllers as needed
3. **Focus on Security:** Implement proper authentication and authorization
4. **Monitor Performance:** Use pagination, caching, and proper indexing
5. **Follow REST Principles:** Embrace HATEOAS and proper HTTP semantics
