package com.worksvn.student_service.configs.swagger;

import com.worksvn.common.components.core.JSONProcessor;
import com.worksvn.common.components.swagger.SwaggerApiGroupBuilder;
import com.worksvn.student_service.constants.ApplicationConstants;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.*;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Value("${application.swagger.info.path}")
    private String swaggerInfoPath;
    @Value("${application.modules-package.name:modules}")
    private String rootModulePackageName;
    @Value("${application.swagger.excluded-modules}")
    private Set<String> swaggerExcludedModules;
    @Value("${application.modules-package.modules}")
    private Set<String> allModules;

    @Autowired
    private JSONProcessor jsonProcessor;
    @Autowired(required = false)
    private BuildProperties buildProperties;
    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private SwaggerApiGroupBuilder swaggerApiGroupBuilder;

    @Bean
    public ApiInfo projectApiInfo() throws IOException {
        String version = buildProperties == null ? "dev" : buildProperties.getVersion();
        Map<String, String> apiInfo = jsonProcessor.parseFromResourceFiles(swaggerInfoPath);
        return swaggerApiGroupBuilder.newApiInfo(apiInfo.get("title"), apiInfo.get("description"),
                apiInfo.get("licenseUrl"), version, apiInfo.get("developBy"),
                apiInfo.get("contactEmail"));
    }

    @PostConstruct
    public void init() {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
        String modulesPackageName = ApplicationConstants.BASE_PACKAGE_NAME + "." + rootModulePackageName;
        for (String moduleName : allModules) {
            if (!swaggerExcludedModules.contains(moduleName)) {
                Docket moduleApiGroup = swaggerApiGroupBuilder.newSwaggerApiGroup(moduleName, modulesPackageName + "." + moduleName + ".controllers");
                configurableBeanFactory.registerSingleton("swaggerApiGroup" + moduleName, moduleApiGroup);
            }
        }
    }
}
