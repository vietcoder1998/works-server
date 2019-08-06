package com.worksvn.student_service.base.controllers;

import com.worksvn.student_service.base.models.BaseResponse;
import com.worksvn.student_service.base.models.FieldValidationError;
import com.worksvn.student_service.constants.ResponseValue;
import com.worksvn.student_service.modules.auth.models.dtos.AuthorizedUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRESTController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseResponse onDtoValidationError(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        List<ObjectError> errors = result.getAllErrors();
        return handleFieldErrors(errors);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseResponse onBindValueDtoError(BindException e) {
        BindingResult result = e.getBindingResult();
        List<ObjectError> errors = result.getAllErrors();
        return handleFieldErrors(errors);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseResponse onRequestParamMissingError(MissingServletRequestParameterException e) {
        return new BaseResponse<>(ResponseValue.MISSING_REQUEST_PARAMS);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseResponse onRequestBodyError(HttpMessageNotReadableException e) {
        return new BaseResponse<>(ResponseValue.INVALID_OR_MISSING_REQUEST_BODY);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public BaseResponse onInternalServerError(Exception e) {
        e.printStackTrace();
        return new BaseResponse(ResponseValue.UNEXPECTED_ERROR_OCCURRED);
    }

    private BaseResponse handleFieldErrors(List<ObjectError> errors) {
        List<FieldValidationError> errorFields = new ArrayList<>();
        for (ObjectError error : errors) {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
                errorFields.add(new FieldValidationError(fieldError.getField(), localizedErrorMessage));
            } else {
                errorFields.add(new FieldValidationError(error.getObjectName(), error.getDefaultMessage()));
            }
        }
        return new BaseResponse<>(ResponseValue.FIELD_VALIDATION_ERROR, errorFields);
    }

    private String resolveLocalizedErrorMessage(FieldError fieldError) {
        return fieldError.getDefaultMessage();
    }

    protected AuthorizedUser getAuthorizedUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return (AuthorizedUser) authentication.getPrincipal();
        } catch (Exception e) {
            return new AuthorizedUser();
        }
    }
}
