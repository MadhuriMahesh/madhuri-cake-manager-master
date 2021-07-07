package com.madhuri.cake.manager.master.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.H2Dialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

@Slf4j
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.madhuri.cake.manager.master.dao")
public class LocalDataBaseConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClass;
    @Value("${spring.datasource.url}")
    private String dataSourceUrl;
    @Value("${spring.datasource.username}")
    private String dataSourceUsername;
    @Value("${spring.datasource.password}")
    private String dataSourcePassword;
    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private Integer hikariPoolSize;
    @Value("${spring.datasource.hikari.minimum-idle}")
    private Integer hikariIdle;



    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setPackagesToScan("com.madhuri.cake.manager.master.dao");
        emf.setJpaVendorAdapter(jpaVendorAdapter());

        return emf;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter(){
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(true);
        jpaVendorAdapter.setGenerateDdl(false);
        jpaVendorAdapter.setDatabasePlatform(H2Dialect.class.getName());
        return jpaVendorAdapter;
    }

    @Bean
    public DataSource dataSource() {
        log.info("DataSource Loading");

        final HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClass);
        config.setJdbcUrl(dataSourceUrl);
        config.setUsername(dataSourceUsername);
        config.setPassword(dataSourcePassword);
        config.setConnectionTestQuery("SELECT 1");
        config.setMaximumPoolSize(hikariPoolSize);
        config.setMinimumIdle(hikariIdle);
        DataSource dataSource = new HikariDataSource(config);

        Resource initSchema = new ClassPathResource("schema.sql");
        DatabasePopulator populator = new ResourceDatabasePopulator(initSchema);
        DatabasePopulatorUtils.execute(populator, dataSource);

        return dataSource;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }


}
