package com.example.edi_intconvert.cfg;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@Slf4j
@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

    @Value("${project.db.url}")
    private String url;

    @Value("${project.db.username}")
    private String getUsername;

    @Value("${project.db.password}")
    private String getPassword;

    @Value("${project.db.driver-class-name}")
    private String getDriverClass;

    @Value("${project.hikari.minimumIdle}")
    private String getMinimumIdle;

    @Value("${project.hikari.maximumPoolSize}")
    private String getMaximumPoolSize;

    @Value("${project.hikari.idleTimeout}")
    private String getIdleTimeout;

    @Value("${project.hikari.poolName}")
    private String getPoolName;

    @Value("${project.hikari.maxLifetime}")
    private String getMaxLifeTime;

    @Value("${project.hikari.connectionTimeout}")
    private String getConnectionTimeOut;

    @Value("${project.hikari.testQuery}")
    private String getTestQuery;

    @Value("${project.hibernate.bytecode.use_reflection_optimizer}")
    private String useReflectionOptimizer;

    @Value("${project.hibernate.default_schema}")
    private String defaultSchema;

    @Value("${project.hibernate.format_sql}")
    private String formatSql;

    @Value("${project.hibernate.generate_statistics}")
    private String generateStatistics;

    @Value("${project.hibernate.id.new_generator_mappings}")
    private String newGeneratorMappings;

    @Value("${project.hibernate.jdbc.lob.non_contextual_creation}")
    private String lobNonContextualCreation;

    @Value("${project.hibernate.search.autoregister_listeners}")
    private String searchAutoregisterListeners;

    @Value("${project.hibernate.show_sql}")
    private String showSql;

    @Value("${project.hibernate.use_sql}")
    private String useSql;


    @Bean
    public DataSource dataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(url);
        hikariDataSource.setUsername(getUsername);
        hikariDataSource.setPassword(getPassword);
        hikariDataSource.setDriverClassName(getDriverClass);

        hikariDataSource.setMinimumIdle(Integer.parseInt(Objects.requireNonNull(getMinimumIdle)));
        hikariDataSource.setMaximumPoolSize(Integer.parseInt(Objects.requireNonNull(getMaximumPoolSize)));
        hikariDataSource.setIdleTimeout(Integer.parseInt(Objects.requireNonNull(getIdleTimeout)));
        hikariDataSource.setPoolName(getPoolName);
        hikariDataSource.setMaxLifetime(Integer.parseInt(Objects.requireNonNull(getMaxLifeTime)));
        hikariDataSource.setConnectionTimeout(Integer.parseInt(Objects.requireNonNull(getConnectionTimeOut)));
        hikariDataSource.setConnectionTestQuery(getTestQuery);
        return hikariDataSource;
    }

    private Properties getJpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.show_sql", showSql);
        properties.setProperty("hibernate.format_sql", formatSql);
        properties.setProperty("hibernate.use_sql", useSql);
        properties.setProperty("hibernate.generate_statistics", generateStatistics);
        properties.setProperty("hibernate.id.new_generator_mappings", newGeneratorMappings);
        properties.setProperty("hibernate.default_schema", defaultSchema);
        properties.setProperty("hibernate.search.autoregister_listeners", searchAutoregisterListeners);
        properties.setProperty("hibernate.bytecode.use_reflection_optimizer", useReflectionOptimizer);
        properties.setProperty("hibernate.jdbc.lob.non_contextual_creation", lobNonContextualCreation);
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");
        return properties;
    }

    @Bean("TM_EDI_INT")
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
