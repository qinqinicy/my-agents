package com.sqx.modules.decision.entity.event;

import io.swagger.annotations.*;
import lombok.*;

import java.util.*;

@Data
@ApiModel
public class AttentionSum {

    @ApiModelProperty(value = "领导批示")
    private Integer leaderNum ;

    @ApiModelProperty(value = "我的批示")
    private Integer myCommentNum;

    @ApiModelProperty(value = "我的关注")
    private Integer myAttentionNum;
}
