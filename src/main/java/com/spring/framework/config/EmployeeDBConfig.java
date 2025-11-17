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

@Configuration
@EnableJpaRepositories(
        basePackages = "com.spring.framework.repository",
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = com.spring.framework.repository.EmployeeRepository.class   // 👈 Only Employee repo
        ),
        entityManagerFactoryRef = "employeeEntityManagerFactory",
        transactionManagerRef = "employeeTransactionManager"
)
public class EmployeeDBConfig {

    @Bean
    @ConfigurationProperties(prefix = "employee.datasource")
    public DataSourceProperties employeeDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "employeeDataSource")
    public DataSource employeeDataSource() {
        return employeeDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean(name = "employeeEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean employeeEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("employeeDataSource") DataSource employeeDataSource
    ) {
        return builder
                .dataSource(employeeDataSource)
                .packages(com.spring.framework.model.Employee.class) // 👈 Only Employee entity
                .persistenceUnit("employee")
                .build();
    }

    @Bean(name = "employeeTransactionManager")
    public JpaTransactionManager employeeTransactionManager(
            @Qualifier("employeeEntityManagerFactory")
            LocalContainerEntityManagerFactoryBean factory
    ) {
        return new JpaTransactionManager(factory.getObject());
    }
}
