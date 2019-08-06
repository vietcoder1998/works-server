package com.worksvn.student_service.configs.auth;

import com.worksvn.student_service.exceptions.auth.CustomOAuth2Exception;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

@Configuration
public class OAuth2CustomResponseConfig {
    @Bean
    public WebResponseExceptionTranslator authExceptionTranslator() {
        return e -> {
            if (e instanceof OAuth2Exception) {
                CustomOAuth2Exception customOAuth2Exception = new CustomOAuth2Exception((OAuth2Exception) e);
                return ResponseEntity
                        .status(customOAuth2Exception.getResponseValue().httpStatus())
                        .body(customOAuth2Exception);
            } else {
                throw e;
            }
        };
    }
}
