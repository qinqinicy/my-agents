package com.sqx.modules.decision.entity.key;

import io.swagger.annotations.*;
import lombok.*;

import java.util.*;

@Data
@ApiModel
public class EventClassNum {

    @ApiModelProperty(value = "事件数量")
    private Integer num;

    @ApiModelProperty(value = "事件分类")
    private String eventSourceClass;

}
