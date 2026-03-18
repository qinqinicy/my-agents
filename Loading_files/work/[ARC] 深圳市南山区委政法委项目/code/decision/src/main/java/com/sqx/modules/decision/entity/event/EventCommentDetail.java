package com.sqx.modules.decision.entity.event;

import io.swagger.annotations.*;
import lombok.*;

@Data
@ApiModel
public class EventCommentDetail {

    @ApiModelProperty(value = "事件id")
    private Integer eventId;
    @ApiModelProperty(value = "反馈内容")
    private String comment;
    @ApiModelProperty(value = "批示单位名称")
    private String unitName;

    @ApiModelProperty(value = "批示单位code")
    private String unitCode;

    private String userId;
}

