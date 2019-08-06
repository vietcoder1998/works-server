package com.worksvn.student_service.configs.auth;

import com.worksvn.student_service.components.auth.CustomSwaggerOperationAuthReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger.readers.operation.OperationAuthReader;

@Configuration
public class AuthSwaggerConfig {
    @Bean
    public OperationAuthReader operationAuthReader() {
        return new CustomSwaggerOperationAuthReader();
    }
}
