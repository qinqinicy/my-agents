package com.sqx.modules.decision.entity.key;

import com.sqx.modules.decision.entity.event.*;
import io.swagger.annotations.*;
import lombok.*;

import java.util.*;

@Data
@ApiModel
public class EventClassCalOut {

    @ApiModelProperty(value = "事件五种分类统计")
    private List<EventClassNum> eventClassNums ;

}
