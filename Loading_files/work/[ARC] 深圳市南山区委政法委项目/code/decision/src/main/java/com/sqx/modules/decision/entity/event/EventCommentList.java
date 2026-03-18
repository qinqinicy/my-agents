package com.sqx.modules.decision.entity.event;

import com.sqx.modules.decision.entity.*;
import io.swagger.annotations.*;
import lombok.*;

import java.util.*;

@Data
@ApiModel
public class EventCommentList {

    @ApiModelProperty(value = "批示列表")
    private List<EventComment> eventCommentList;
}

