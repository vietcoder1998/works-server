package com.worksvn.student_service.configs.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.worksvn.common.base.models.BaseResponseBody;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.exceptions.auth.AuthorizationException;
import com.worksvn.common.modules.auth.core.CustomUserDetail;
import com.worksvn.common.modules.auth.core.IAuthorization;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private com.worksvn.common.modules.auth.core.IAuthorization authorization;

    public JWTAuthorizationFilter(IAuthorization authorization) {
        this.authorization = authorization;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        Authentication authentication = null;
        CustomUserDetail userDetail = null;
        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
            userDetail = (CustomUserDetail) authentication.getPrincipal();
        } catch (Exception ignore) {
        }

        if (authentication != null && userDetail != null) {
            try {
                authorization.authorizeUser(method, uri,
                        userDetail, authentication.getAuthorities());
                filterChain.doFilter(request, response);
            } catch (AuthorizationException e) {
                responseErrorMessage(response, e.getResponseValue());
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private void responseErrorMessage(HttpServletResponse response,
                                      ResponseValue responseValue) throws IOException {
        response.setStatus(responseValue.httpStatus().value());
        response.setHeader("Content-Type", "application/json; charset=UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(new BaseResponseBody<>(responseValue, null)));
    }
}
