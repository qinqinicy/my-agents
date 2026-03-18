package com.sqx.modules.decision.entity.event;

import io.swagger.annotations.*;
import lombok.*;

import java.util.*;

@Data
@ApiModel
public class EventListOut {
    @ApiModelProperty(value = "近24小时")
    private List<Event> recently ;
    @ApiModelProperty(value = "事件列表 历史记录/首页5条")
    private List<Event> events ;
}
