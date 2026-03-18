package com.sqx.modules.decision.entity.event;

import io.swagger.annotations.*;
import lombok.*;

import java.util.*;

@Data
@ApiModel
public class EventCommentOut {

    @ApiModelProperty(value = "反馈列表")
    private List<EventCommentDetail> eventCommentDetails;
}

