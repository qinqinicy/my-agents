package com.sqx.modules.decision.entity.event;

import io.swagger.annotations.*;
import lombok.*;

@Data
@ApiModel
public class UserCalParam extends UserInfoBase{

    @ApiModelProperty(value = "区域code")
    private String departmentCode;

    private String userId;
}

