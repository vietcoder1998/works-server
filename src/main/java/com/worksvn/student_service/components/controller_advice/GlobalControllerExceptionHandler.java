package com.worksvn.student_service.components.controller_advice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.worksvn.common.base.models.*;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.exceptions.ISResponseException;
import com.worksvn.common.exceptions.ResponseException;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @ExceptionHandler(ISResponseException.class)
    @ResponseBody
    public BaseResponse onISResponseError(ISResponseException e) {
        BaseResponseBody<?> responseBody;
        BaseResponseBody<?> internalBody = e.getBody();
        if (activeProfile.equalsIgnoreCase("dev")) {
            responseBody = new ISResponseBody<>(internalBody.getCode(), internalBody.getMsg(),
                    internalBody.getData(),
                    new ISResponseDto(e.getApi(), e.getHttpStatus(), e.getBody()));
        } else {
            responseBody = new BaseResponseBody<>(internalBody.getCode(), internalBody.getMsg(),
                    internalBody);
        }
        return new BaseResponse<>(e.getHttpStatus(), responseBody);
    }

    @ExceptionHandler(ResponseException.class)
    @ResponseBody
    public BaseResponse onResponseError(ResponseException e) {
        return new BaseResponse<>(e.getHttpStatus(), e.getBody());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public BaseResponse onAccessDenied(Exception e) {
        if (activeProfile.equalsIgnoreCase("dev")) {
            e.printStackTrace();
        }
        return new BaseResponse(ResponseValue.ACCESS_DENIED);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public BaseResponse onInternalServerError(Exception e) {
        e.printStackTrace();
        return new BaseResponse(ResponseValue.UNEXPECTED_ERROR_OCCURRED);
    }

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
        if (activeProfile.equalsIgnoreCase("dev")) {
            e.printStackTrace();
        }
        return new BaseResponse<>(ResponseValue.INVALID_OR_MISSING_REQUEST_PARAMETERS);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseResponse onRequestBodyError(HttpMessageNotReadableException e) {
        if (activeProfile.equalsIgnoreCase("dev")) {
            e.printStackTrace();
        }
        return new BaseResponse<>(ResponseValue.INVALID_OR_MISSING_REQUEST_BODY);
    }

    private BaseResponse handleFieldErrors(List<ObjectError> fieldErrors) {
        List<FieldValidationError> invalidFieldDtos = new ArrayList<>();
        for (ObjectError objectError : fieldErrors) {
            if (objectError instanceof FieldError) {
                FieldError fieldError = (FieldError) objectError;
                String fieldName = fieldError.getField();
                ConstraintViolationImpl constraintViolation = fieldError.unwrap(ConstraintViolationImpl.class);
                try {
                    Field invalidField = constraintViolation.getRootBeanClass().getDeclaredField(fieldError.getField());
                    JsonProperty jsonProperty = invalidField.getAnnotation(JsonProperty.class);
                    if (jsonProperty != null) {
                        fieldName = jsonProperty.value();
                    }
                } catch (NoSuchFieldException e) {
                    continue;
                }
                String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
                invalidFieldDtos.add(new FieldValidationError(fieldName, localizedErrorMessage));
            } else {
                invalidFieldDtos.add(new FieldValidationError(objectError.getObjectName(), objectError.getDefaultMessage()));
            }
        }
        return new BaseResponse<>(ResponseValue.INVALID_FIELDS, invalidFieldDtos);
    }

    private String resolveLocalizedErrorMessage(FieldError fieldError) {
        return fieldError.getDefaultMessage();
    }
}
