package com.sqx.modules.decision.entity.event;

import io.swagger.annotations.*;
import lombok.*;

@Data
@ApiModel
public class EventDetailParam extends UserInfoBase{

    @ApiModelProperty(value = "id")
    private Integer id;

}
