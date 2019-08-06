package com.worksvn.student_service.modules.auth.models.dtos;

import com.worksvn.student_service.annotations.validator.text.length.MinLength;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@ApiModel
public class NewUserEmailRegistrationDto {
    @ApiModelProperty(notes = "email của người dùng", example = "EMAIL")
    @Email
    private String email;
    @ApiModelProperty(notes = "password của người dùng", example = "NOT_EMPTY, MIN_LENGTH=6", position = 1)
    @NotEmpty
    @MinLength(6)
    private String password;

    public NewUserEmailRegistrationDto() {
    }

    public NewUserEmailRegistrationDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
