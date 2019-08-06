package com.worksvn.student_service.configs.datasource;

import com.worksvn.student_service.constants.ApplicationConstants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {
                ApplicationConstants.BASE_PACKAGE_NAME + ".modules.oauth2.repositories",
                ApplicationConstants.BASE_PACKAGE_NAME + ".modules.auth.repositories"
        },
        entityManagerFactoryRef = "oauth2EntityManagerFactory",
        transactionManagerRef = "oauth2TransactionManager"
)
public class OAuth2DataSourceConfig {
    @Value("${spring.oauth2-datasource.hibernate.ddl-auto}")
    private String hibernateDDLAuto;

    @Bean("oauth2DataSource")
    @ConfigurationProperties(prefix = "spring.oauth2-datasource")
    public DataSource oauth2DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("oauth2EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean oauth2EntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                             @Qualifier("oauth2DataSource") DataSource dataSource) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", hibernateDDLAuto);
        return builder.dataSource(dataSource)
                .packages(new String[]{
                        ApplicationConstants.BASE_PACKAGE_NAME + ".modules.oauth2.models.entities",
                        ApplicationConstants.BASE_PACKAGE_NAME + ".modules.auth.models.entities"
                })
                .persistenceUnit("oauth2")
                .properties(properties)
                .build();
    }

    @Bean("oauth2TransactionManager")
    public PlatformTransactionManager oauth2TransactionManager(@Qualifier("oauth2EntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
