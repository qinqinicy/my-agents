package com.sqx.modules.decision.entity.event;

import com.sqx.modules.decision.entity.*;
import com.sqx.modules.decision.entity.third.*;
import io.swagger.annotations.*;
import lombok.*;

import java.util.*;

@Data
@ApiModel
public class EventDynamicCommentOut {

    @ApiModelProperty(value = "关注状态")
    private String attentionStatus;

    @ApiModelProperty(value = "批示反馈列表")
    private List<EventComment> eventCommentList;



}
