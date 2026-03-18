package com.sqx.modules.decision.entity.event;

import io.swagger.annotations.*;
import lombok.*;

@Data
@ApiModel
public class District {

    @ApiModelProperty(value = "编号")
    private String departmentCode;
    @ApiModelProperty(value = "名称")
    private String departmentName;


    private String district;

    private String districtName;



}
