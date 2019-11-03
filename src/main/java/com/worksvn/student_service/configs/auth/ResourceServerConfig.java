package com.worksvn.student_service.configs.auth;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.base.models.BaseResponseBody;
import com.worksvn.common.components.auth.NoAuthorizationRequiredRoutes;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.modules.auth.core.IAuthorization;
import com.worksvn.common.utils.auth.RouteScannerUtils;
import com.worksvn.common.utils.core.JacksonObjectMapper;
import com.worksvn.student_service.constants.ApplicationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    public static final Logger logger = LoggerFactory.getLogger(ResourceServerConfig.class);

    @Value("${application.oauth2.resource-server.id}")
    private String resourceID;
    @Value("${application.modules-package.name:modules}")
    private String rootModulePackageName;
    @Value("${application.modules-package.modules}")
    private Set<String> allModules;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ResourceServerTokenServices resourceServerTokenServices;
    @Autowired
    private AuthenticationEntryPoint customAuthenticationEntryPoint;
    @Autowired
    private AccessDeniedHandler customAccessDeniedHandler;
    @Autowired
    private IAuthorization authorization;

    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            logger.error("customAccessDeniedHandler", accessDeniedException);
            com.worksvn.common.constants.ResponseValue responseValue = com.worksvn.common.constants.ResponseValue.UNEXPECTED_ERROR_OCCURRED;
            String responseBodyJson = com.worksvn.common.utils.core.JacksonObjectMapper.getInstance()
                    .writeValueAsString(new BaseResponseBody<>(responseValue, null));
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(responseValue.httpStatus().value());
            response.getWriter().write(responseBodyJson);
        };
    }

    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            com.worksvn.common.constants.ResponseValue responseValue = null;
            String message = authException.getMessage();
            if (message.contains("Full authentication is required to access this resource")) {
                responseValue = com.worksvn.common.constants.ResponseValue.AUTHENTICATION_REQUIRED;
            } else if (message.contains("Access token expired")) {
                responseValue = com.worksvn.common.constants.ResponseValue.EXPIRED_TOKEN;
            } else if (message.contains("Cannot convert access token to JSON")) {
                responseValue = com.worksvn.common.constants.ResponseValue.INVALID_TOKEN;
            } else if (message.contains("Invalid token does not contain resource id")) {
                responseValue = com.worksvn.common.constants.ResponseValue.CANNOT_ACCESS_THIS_RESOURCE_SERVER;
            } else {
                logger.error("customAuthenticationEntryPoint", authException);
                responseValue = ResponseValue.UNEXPECTED_ERROR_OCCURRED;
            }
            String responseBodyJson = JacksonObjectMapper.getInstance()
                    .writeValueAsString(new BaseResponseBody<>(responseValue, null));
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(responseValue.httpStatus().value());
            response.getWriter().write(responseBodyJson);
        };
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        NoAuthorizationRequiredRoutes noAuthorizationRequiredRoutes = createNoAuthorizationRequiredRoutes();
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry =
                http.cors().and()
                        .csrf().disable()
                        .authorizeRequests();
        for (Map.Entry<HttpMethod, Set<String>> apiEntry : noAuthorizationRequiredRoutes.getApis().entrySet()) {
            expressionInterceptUrlRegistry = expressionInterceptUrlRegistry
                    .antMatchers(apiEntry.getKey(), apiEntry.getValue().toArray(new String[0]))
                    .permitAll();
        }
        expressionInterceptUrlRegistry.anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(new JWTAuthorizationFilter(authorization), RequestCacheAwareFilter.class);
    }

    public NoAuthorizationRequiredRoutes createNoAuthorizationRequiredRoutes() {
        logger.info("Start scanning no authorization required routes...");
        NoAuthorizationRequiredRoutes noAuthorizationRequiredRoutes = new NoAuthorizationRequiredRoutes();
        String rootModulePackage = ApplicationConstants.BASE_PACKAGE_NAME + "." + rootModulePackageName;
        Set<Class<? extends Annotation>> excludeAnnotations = new HashSet<>();
        excludeAnnotations.add(AuthorizationRequired.class);
        for (String moduleName : allModules) {
            int apiFound = 0;
            com.worksvn.common.utils.auth.RouteScannerUtils.scanRoutes(rootModulePackage + "." + moduleName + ".controllers",
                    null, excludeAnnotations,
                    (containClass, method) -> method.getDeclaredAnnotation(AuthorizationRequired.class) == null,
                    new RouteScannerUtils.RouteFetched() {
                        @Override
                        public void onNewContainerClass(Class<?> containerClass) {

                        }

                        @Override
                        public void onNewMethod(Method method) {

                        }

                        @Override
                        public void onNewRouteFetched(RequestMethod method, String route) {
                            noAuthorizationRequiredRoutes.addApi(HttpMethod.valueOf(method.name()), route);
                        }
                    }, true);
            logger.info("module [" + moduleName + "] scanned - found " +
                    apiFound + " no authorization required route(s)");
        }
        logger.info("Scanning no authorization required routes...DONE");
        return noAuthorizationRequiredRoutes;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(resourceID)
                .tokenServices(resourceServerTokenServices)
                .authenticationManager(authenticationManager)
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler);
    }
}
