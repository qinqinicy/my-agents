package com.sqx.modules.decision.entity.key;

import io.swagger.annotations.*;
import lombok.*;

import java.util.*;

@Data
@ApiModel
public class EventSelectOut {

    @ApiModelProperty(value = "涉及领域下拉框")
    private List<String> relatedField ;

    @ApiModelProperty(value = "主办单位下拉框")
    private List<String> organizer ;

    @ApiModelProperty(value = "事件分类下拉框")
    private List<String> eventClass ;

    @ApiModelProperty(value = "事件来源下拉框")
    private List<String> eventSource ;


}
