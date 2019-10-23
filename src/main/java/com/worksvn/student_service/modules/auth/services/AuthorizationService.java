package com.worksvn.student_service.modules.auth.services;

import com.worksvn.common.exceptions.auth.AuthorizationException;
import com.worksvn.common.modules.auth.core.CustomUserDetail;
import com.worksvn.common.modules.auth.core.IAuthorization;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Primary
public class AuthorizationService implements IAuthorization {

    @Override
    public void authorizeUser(String method, String route, CustomUserDetail userDetail, Collection<? extends GrantedAuthority> authorities) throws AuthorizationException {
        // TODO ...
    }
}
