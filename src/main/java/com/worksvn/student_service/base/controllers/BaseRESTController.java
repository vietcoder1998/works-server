package com.worksvn.student_service.base.controllers;

import com.worksvn.common.modules.auth.core.CustomUserDetail;
import com.worksvn.common.modules.auth.requests.AuthorizedUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseRESTController {

    protected AuthorizedUser getAuthorizedUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
            return new AuthorizedUser(userDetail.getUserID(), userDetail.getUsername(), userDetail.getAuthType());
        } catch (Exception e) {
            return new AuthorizedUser();
        }
    }
}
