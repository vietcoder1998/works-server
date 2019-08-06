package com.worksvn.student_service.modules.auth.services;

import com.worksvn.student_service.exceptions.auth.AuthorizationException;
import com.worksvn.student_service.modules.auth.models.dtos.AuthorizedUser;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface IAuthorization {
    void authorizeUser(String method, String route, AuthorizedUser authorizedUser,
                       Collection<? extends GrantedAuthority> authorities) throws AuthorizationException;
}
