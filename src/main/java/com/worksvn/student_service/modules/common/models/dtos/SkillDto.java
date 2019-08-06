package com.worksvn.student_service.modules.common.models.dtos;

import com.worksvn.student_service.modules.common.models.entities.Skill;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class SkillDto {
    @ApiModelProperty(notes = "id kỹ năng")
    private int id;
    @ApiModelProperty(notes = "tên kỹ năng", position = 1)
    private String name;

    public SkillDto() {
    }

    public SkillDto(Integer id, String name) {
        this.id = id == null? -1 : id;
        this.name = name;
    }

    public SkillDto(Skill skill) {
        this.id = skill.getId();
        this.name = skill.getName();
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
