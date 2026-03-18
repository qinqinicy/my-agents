package com.sqx.modules.decision.entity.key;

import io.swagger.annotations.*;
import lombok.*;

import java.util.*;

@Data
@ApiModel
public class EventCalOut {

    @ApiModelProperty(value = "今日事件总量")
    private Integer todayEventTotal;

    @ApiModelProperty(value = "今日维稳事件数量")
    private Integer todayMaintainEventTotal;

    @ApiModelProperty(value = "今日重点事件")
    private Integer todayKeyEventTotal;


}
