package com.worksvn.student_service.configs.datasource;

//import com.worksvn.authorization_server.constants.ApplicationConstants;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(
//        basePackages = ApplicationConstants.BASE_PACKAGE_NAME + ".modules.oauth2_admin.repositories",
//        entityManagerFactoryRef = "secondaryEntityManagerFactory",
//        transactionManagerRef = "secondaryTransactionManager"
//)
//public class SecondaryDataSourceConfig {
//    @Value("${spring.oauth2-admin-datasource.hibernate.ddl-auto}")
//    private String hibernateDDLAuto;
//
//    @Bean(name = "secondaryDataSource")
//    @ConfigurationProperties(prefix = "spring.oauth2-admin-datasource")
//    public DataSource secondaryDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "secondaryEntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(EntityManagerFactoryBuilder builder,
//                                                                             @Qualifier("secondaryDataSource") DataSource dataSource) {
//        Map<String, Object> properties = new HashMap<String, Object>();
//        properties.put("hibernate.hbm2ddl.auto", hibernateDDLAuto);
//        return builder.dataSource(dataSource)
//                .packages(ApplicationConstants.BASE_PACKAGE_NAME  + ".modules.oauth2_admin.models.entities")
//                .persistenceUnit("oauth2-admin")
//                .properties(properties)
//                .build();
//    }
//
//    @Bean(name = "secondaryTransactionManager")
//    public PlatformTransactionManager secondaryTransactionManager(@Qualifier("secondaryEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
//        return new JpaTransactionManager(entityManagerFactory);
//    }
//}
