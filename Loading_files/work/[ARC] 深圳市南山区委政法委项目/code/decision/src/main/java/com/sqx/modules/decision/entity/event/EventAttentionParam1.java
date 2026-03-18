package com.sqx.modules.decision.entity.event;

import com.sqx.modules.decision.entity.third.*;
import io.swagger.annotations.*;
import lombok.*;

@Data
@ApiModel
public class EventAttentionParam1 extends UserInfoBase{


    @ApiModelProperty(value = "辖区动态事项")
    private DynamicEvent dynamicEvent;

    @ApiModelProperty(value = "关注状态 0取消关注 1关注")
    private String attentionStatus;


}

