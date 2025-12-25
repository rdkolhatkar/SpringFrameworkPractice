package com.spring.framework.config;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
@Configuration
@EnableJpaRepositories(
        basePackages = "com.spring.framework.repository.student",
        entityManagerFactoryRef = "studentEntityManagerFactory",
        transactionManagerRef = "studentTransactionManager"
)
public class StudentDBConfig {

    // Reads spring.datasource.* (MySQL)
    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSourceProperties studentDataSourceProperties() {
        return new DataSourceProperties();
    }

    // MySQL DataSource (PRIMARY)
    @Primary
    @Bean(name = "studentDataSource")
    public DataSource studentDataSource() {
        return studentDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    // EntityManager for Student DB
    @Primary
    @Bean(name = "studentEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean studentEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("studentDataSource") DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages("com.spring.framework.model.student")
                .persistenceUnit("student")
                .build();
    }

    // Transaction manager for Student DB
    @Primary
    @Bean(name = "studentTransactionManager")
    public JpaTransactionManager studentTransactionManager(
            @Qualifier("studentEntityManagerFactory")
            LocalContainerEntityManagerFactoryBean factory
    ) {
        return new JpaTransactionManager(factory.getObject());
    }
}
