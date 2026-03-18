package com.sqx.modules.decision.entity.event;

import io.swagger.annotations.*;
import lombok.*;

@Data
@ApiModel
public class EventCommentParam  extends UserInfoBase{

    @ApiModelProperty(value = "批示还是反馈 1批示 2反馈")
    private int commentOrFeedBack;

    @ApiModelProperty(value = "事件id")
    private Integer eventId;
    @ApiModelProperty(value = "反馈内容")
    private String comment;
    @ApiModelProperty(value = "接收单位")
    private String receiveUnit;
    @ApiModelProperty(value = "接收单位CODE")
    private String receiveCode;

    private String userId;

    @ApiModelProperty("辖区动态事项列表id")
    private String id;

}

