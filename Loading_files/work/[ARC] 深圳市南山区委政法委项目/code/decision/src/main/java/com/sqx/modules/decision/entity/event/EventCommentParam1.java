package com.sqx.modules.decision.entity.event;

import com.sqx.modules.decision.entity.third.*;
import io.swagger.annotations.*;
import lombok.*;

@Data
@ApiModel
public class EventCommentParam1 {

    private UserInfo userInfo;

    private DynamicEvent dynamicEvent;

    @ApiModelProperty(value = "批示还是反馈 1批示 2反馈")
    private int commentOrFeedBack;

    @ApiModelProperty(value = "反馈内容")
    private String comment;


}

