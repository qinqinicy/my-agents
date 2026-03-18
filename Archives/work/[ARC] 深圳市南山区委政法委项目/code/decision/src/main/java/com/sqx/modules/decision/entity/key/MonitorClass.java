package com.sqx.modules.decision.entity.key;

import io.swagger.annotations.*;
import lombok.*;

import java.util.*;

@Data
@ApiModel(description = "维稳事件、重点事件")
public class MonitorClass {

    @ApiModelProperty(value = "最近一个月重复/件")
    private String title;

    @ApiModelProperty(value = "满意度")
    private List<MonitorItem> list;



}
