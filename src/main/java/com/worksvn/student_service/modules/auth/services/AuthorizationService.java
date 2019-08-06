package com.worksvn.student_service.modules.auth.services;

import com.worksvn.student_service.constants.ResponseValue;
import com.worksvn.student_service.exceptions.auth.AuthorizationException;
import com.worksvn.student_service.modules.auth.models.dtos.AuthorizedUser;
import com.worksvn.student_service.modules.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Primary
public class AuthorizationService implements IAuthorization {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void authorizeUser(String method, String route,
                              AuthorizedUser authorizedUser, Collection<? extends GrantedAuthority> authorities)
            throws AuthorizationException {
        boolean authorizationResult = userRepository.isUserBanned(authorizedUser.getUserID());
        if (authorizationResult) {
            throw new AuthorizationException(ResponseValue.USER_BANNED);
        }
    }
}
