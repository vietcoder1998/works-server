package com.worksvn.student_service.modules.common.models.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class LanguageDto {
    @ApiModelProperty(notes = "id ngôn ngữ")
    private int id;
    @ApiModelProperty(notes = "tên ngôn ngữ (en)", position = 1)
    private String name;

    public LanguageDto() {
    }

    public LanguageDto(Integer id, String name) {
        this.id = id == null? -1 : id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
