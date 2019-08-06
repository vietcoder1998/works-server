package com.worksvn.student_service.configs.auth;

import org.springframework.http.HttpMethod;

import java.util.*;

public class NoAuthorizationRequiredRoutes {
    private Map<HttpMethod, Set<String>> apis = new HashMap<>();

    public NoAuthorizationRequiredRoutes() {
        addDefaultRoutes();
    }

    private void addDefaultRoutes() {
        // swagger
        addApi(HttpMethod.GET, "/webjars/**");
        addApi(HttpMethod.GET, "/swagger-ui.html");
        addApi(HttpMethod.GET, "/favicon.ico");
        addApi(HttpMethod.GET, "/swagger-resources/**");
        addApi(HttpMethod.GET, "/v2/api-docs");

        // oauth2
        addApi(HttpMethod.POST, "/oauth/token");

        // actuator
        addApi(HttpMethod.GET, "/actuator/health");
    }

    public Map<HttpMethod, Set<String>> getApis() {
        return apis;
    }

    public void addApi(HttpMethod method, String route) {
        Set<String> routes = apis.computeIfAbsent(method, k -> new HashSet<>());
        routes.add(route);
    }
}
