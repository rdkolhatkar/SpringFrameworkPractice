package com.spring.framework.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

/**
 * This class configures a custom database connection for Employee-related tables.
 * It sets up:
 *  - DataSource           (actual DB connection)
 *  - EntityManagerFactory (manages JPA entities)
 *  - TransactionManager   (handles transactions)
 *
 * This is required when your project uses multiple databases or multiple DB connections.
 */
@Configuration
// @Configuration → Tells Spring that this class contains bean definitions
// and should be processed during application startup.
@EnableJpaRepositories(
        basePackages = "com.spring.framework.repository",
        // @EnableJpaRepositories → Tells Spring where to find JPA Repository interfaces.
        // basePackages            → Folder that contains repository classes.
        includeFilters = @ComponentScan.Filter(
                // includeFilters → Only include specific repository classes instead of scanning all.
                type = FilterType.ASSIGNABLE_TYPE,
                // FilterType.ASSIGNABLE_TYPE → Select a class by matching its type.
                classes = com.spring.framework.repository.EmployeeRepository.class
                // Only EmployeeRepository will use this DB configuration.
        ),
        entityManagerFactoryRef = "employeeEntityManagerFactory",
        // entityManagerFactoryRef → Which EntityManagerFactory bean to use for these repositories.
        transactionManagerRef = "employeeTransactionManager"
        // transactionManagerRef → Which TransactionManager to use for DB transactions.
)
public class EmployeeDBConfig {

    /**
     * Creates a DataSourceProperties object for the employee database.
     *
     * @ConfigurationProperties(prefix = "employee.datasource")
     * → Reads properties from application.properties like:
     *      employee.datasource.url=...
     *      employee.datasource.username=...
     *      employee.datasource.password=...
     *
     * @Bean → Spring will create and manage this method's return object as a Spring Bean.
     */
    @Bean
    @ConfigurationProperties(prefix = "employee.datasource")
    public DataSourceProperties employeeDataSourceProperties() {
        return new DataSourceProperties(); // Holds DB URL, username, password, driver, etc.
    }

    /**
     * Creates the actual DataSource object for connecting to the database.
     *
     * A DataSource = Connection pool to the database.
     *
     * @Bean(name = "employeeDataSource")
     * → Creates a bean with a custom name, so Spring knows which DataSource belongs to employees.
     */
    @Bean(name = "employeeDataSource")
    public DataSource employeeDataSource() {
        return employeeDataSourceProperties()    // Load DB properties
                .initializeDataSourceBuilder()  // Build DataSource
                .build();                       // Create DB connection pool
    }

    /**
     * Creates the EntityManagerFactory for the employee database.
     *
     * EntityManagerFactory:
     *  - Handles @Entity classes
     *  - Creates EntityManager objects for CRUD operations
     *
     * @Bean(name = "employeeEntityManagerFactory")
     * → Defines a factory for managing JPA Entities for Employee DB.
     *
     * @Qualifier("employeeDataSource")
     * → Tells Spring to inject the correct DataSource bean
     *   because multiple DataSources might exist.
     */
    @Bean(name = "employeeEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean employeeEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("employeeDataSource") DataSource employeeDataSource
            // @Qualifier → Used when multiple beans of same type exist.
            // It ensures the correct bean is injected.
    ) {

        return builder
                .dataSource(employeeDataSource) // Assign the employee DB connection
                .packages(com.spring.framework.model.Employee.class)
                // .packages() tells JPA where to find @Entity classes.
                // Here only Employee entity will be scanned.
                .persistenceUnit("employee")
                // persistenceUnit name (used internally by JPA)
                .build();
    }

    /**
     * Defines the TransactionManager for the employee database.
     *
     * TransactionManager:
     *   - Controls commit() and rollback()
     *   - Ensures consistency when performing save/update/delete
     *
     * @Bean(name = "employeeTransactionManager")
     * → Creates a separate transaction manager for Employee DB.
     *
     * @Qualifier("employeeEntityManagerFactory")
     * → Injects the correct EntityManagerFactory.
     */
    @Bean(name = "employeeTransactionManager")
    public JpaTransactionManager employeeTransactionManager(
            @Qualifier("employeeEntityManagerFactory")
            LocalContainerEntityManagerFactoryBean factory
    ) {
        return new JpaTransactionManager(factory.getObject());
        // factory.getObject() returns EntityManagerFactory instance.
    }
}
