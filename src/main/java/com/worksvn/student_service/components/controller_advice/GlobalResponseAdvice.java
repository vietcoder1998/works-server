package com.worksvn.student_service.components.controller_advice;

import com.worksvn.common.base.models.BaseResponseBody;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.student_service.constants.ApplicationConstants;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice(ApplicationConstants.BASE_PACKAGE_NAME)
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public BaseResponseBody<?> beforeBodyWrite(Object data,
                                               MethodParameter returnType, MediaType selectedContentType,
                                               Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                               ServerHttpRequest request, ServerHttpResponse response) {
        if (data instanceof BaseResponseBody) {
            return (BaseResponseBody<?>) data;
        }
        return new BaseResponseBody<>(ResponseValue.SUCCESS, data);
    }
}
