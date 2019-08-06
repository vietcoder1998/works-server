package com.worksvn.student_service.components.auth;

import com.worksvn.student_service.annotations.auth.AuthorizationRequired;
import com.worksvn.student_service.utils.auth.RouteScannerUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class AuthApiFilter implements RouteScannerUtils.ApiFilter {
    @Override
    public boolean allowApi(Class<?> containClass, Method method) {
        return containClass.getDeclaredAnnotation(AuthorizationRequired.class) != null ||
                method.getDeclaredAnnotation(AuthorizationRequired.class) != null;
    }
}
