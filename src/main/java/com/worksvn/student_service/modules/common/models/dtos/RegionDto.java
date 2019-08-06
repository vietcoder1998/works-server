package com.worksvn.student_service.modules.common.models.dtos;

import com.worksvn.student_service.modules.common.models.entities.Region;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class RegionDto {
    @ApiModelProperty(notes = "id tỉnh/thành phố")
    private int id;
    @ApiModelProperty(notes = "tên tỉnh/thành phố", position = 1)
    private String name;

    public RegionDto() {
    }

    public RegionDto(Integer id, String name) {
        this.id = id == null? -1 : id;
        this.name = name;
    }

    public RegionDto(Region region) {
        this.id = region.getId();
        this.name = region.getName();
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
