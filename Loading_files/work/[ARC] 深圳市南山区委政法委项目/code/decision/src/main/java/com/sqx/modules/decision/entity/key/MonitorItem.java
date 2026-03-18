package com.sqx.modules.decision.entity.key;

import io.swagger.annotations.*;
import lombok.*;

@Data
@ApiModel(description = "条目")
public class MonitorItem {

    @ApiModelProperty(value = "条目名称")
    private String label;

    @ApiModelProperty(value = "值")
    private String value;

    @ApiModelProperty(value = "是/否预警")
    private String isError;

    @ApiModelProperty(value = "关联人名称")
    private String detail;

    @ApiModelProperty(value = "预警规则")
    private String ruler;

    @ApiModelProperty(value = "数量单位")
    private String unit;

    @ApiModelProperty(value = "跳转链接")
    private String url;


}
