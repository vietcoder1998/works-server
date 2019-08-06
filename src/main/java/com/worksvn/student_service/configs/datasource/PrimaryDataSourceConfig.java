package com.worksvn.student_service.configs.datasource;

import com.worksvn.student_service.constants.ApplicationConstants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
                ApplicationConstants.BASE_PACKAGE_NAME + ".modules.candidate.repositories",
                ApplicationConstants.BASE_PACKAGE_NAME + ".modules.employer.repositories",
                ApplicationConstants.BASE_PACKAGE_NAME + ".modules.common.repositories"
        }
)
public class PrimaryDataSourceConfig {

    @Value("${spring.datasource.hibernate.ddl-auto}")
    private String hibernateDDLAuto;

    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Qualifier("primary")
    @Bean("entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                       @Qualifier("dataSource") DataSource dataSource) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", hibernateDDLAuto);
        return builder.dataSource(dataSource)
                .packages(new String[] {
                        ApplicationConstants.BASE_PACKAGE_NAME + ".modules.candidate.models.entities",
                        ApplicationConstants.BASE_PACKAGE_NAME + ".modules.employer.models.entities",
                        ApplicationConstants.BASE_PACKAGE_NAME + ".modules.common.models.entities"
                })
                .persistenceUnit("primary")
                .properties(properties)
                .build();
    }

    @Primary
    @Bean("transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
