package com.worksvn.student_service.exceptions.auth;

import com.worksvn.student_service.constants.ResponseValue;

public class AuthorizationException extends Exception {
    private ResponseValue responseValue;

    public AuthorizationException(ResponseValue responseValue) {
        super(responseValue.message());
        this.responseValue = responseValue;
    }

    public ResponseValue getResponseValue() {
        return responseValue;
    }
}
