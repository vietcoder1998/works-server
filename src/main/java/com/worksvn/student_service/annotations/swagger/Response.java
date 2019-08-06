package com.worksvn.student_service.annotations.swagger;

import com.worksvn.student_service.constants.ResponseValue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Response {
    ResponseValue responseValue();
    Class<?> responseBody() default Void.class;
    String reference() default "";
    String responseContainer() default "";
}
