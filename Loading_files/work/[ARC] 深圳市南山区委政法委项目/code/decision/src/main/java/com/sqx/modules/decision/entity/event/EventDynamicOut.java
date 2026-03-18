package com.sqx.modules.decision.entity.event;

import com.sqx.modules.decision.entity.*;
import com.sqx.modules.decision.entity.third.*;
import io.swagger.annotations.*;
import lombok.*;

import java.util.*;

@Data
@ApiModel
public class EventDynamicOut {



    @ApiModelProperty(value = "辖区动态事项列表")
    private List<DynamicEvent> dynamicEvents ;




}
