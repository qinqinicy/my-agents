package com.sqx.modules.decision.entity.third;

import io.swagger.annotations.*;
import lombok.*;

@Data
@ApiModel
public class PushEventParam {

    @ApiModelProperty(value = "事件详情")
    private EventBasic eventBasic;




}

