package com.sqx.modules.decision.entity.event;

import io.swagger.annotations.*;
import lombok.*;

@Data
@ApiModel
public class EventAuditParam extends UserInfoBase{
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "审核状态 0 未审核， 1 通过 ，2不通过 ，默认0")
    private String examineStatus;
    @ApiModelProperty(value = "推送时间格式如： 2024-09-13 12:00:00")
    private String pushTime;
}
