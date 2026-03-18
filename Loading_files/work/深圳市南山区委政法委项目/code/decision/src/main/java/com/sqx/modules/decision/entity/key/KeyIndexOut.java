package com.sqx.modules.decision.entity.key;

import io.swagger.annotations.*;
import lombok.*;

import java.util.*;

@Data
@ApiModel
public class KeyIndexOut {

    @ApiModelProperty(value = "风险条目")
    private Integer fxNum;

    @ApiModelProperty(value = "风险提醒")
    private List<MonitorClass> detailList;

}
