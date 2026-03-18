package com.sqx.modules.decision.entity.event;

import io.swagger.annotations.*;
import lombok.*;

@Data
@ApiModel
public class EventAttentionParam  extends UserInfoBase{
    @ApiModelProperty(value = "事件id")
    private Integer eventId;
    @ApiModelProperty(value = "关注状态 0取消关注 1关注")
    private String attentionStatus;
    private String userId;
}

