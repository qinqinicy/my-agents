package com.sqx.modules.decision.entity.event;

import com.sqx.modules.decision.entity.third.*;
import io.swagger.annotations.*;
import lombok.*;

@Data
@ApiModel
public class EventProcessBasic {

    @ApiModelProperty(value = "工单信息唯一序列号")
    private String matterUuid;

    @ApiModelProperty(value = "批示批次号(默认1）")
    private String matterNumber;

    @ApiModelProperty(value = "关联的主事件ID（用于查询业务系统的事件")
    private String eventMID;

    @ApiModelProperty(value = "批示内容")
    private String decisionComment;

    @ApiModelProperty(value = "返回内容")
    private String feedbackComment;

    @ApiModelProperty(value = "批示人ID")
    private String userId;

    @ApiModelProperty(value = "批示人名称 如:张三")
    private String userName;

    @ApiModelProperty(value = "批示人单位code")
    private String unitCode;

    @ApiModelProperty(value = "批示人单位名称 如:南山区")
    private String unitName;

    @ApiModelProperty(value = "批示创建时间")
    private String createdTime;

    @ApiModelProperty(value = "批示类型")
    private String decisionCommentType;

    @ApiModelProperty(value = "反馈批示类型")
    private String feedbackCommentType;

    @ApiModelProperty(value = "事项已上报;1-事项审核通过;2-事项审核失败(不进入今日领导关注事项):3-事项已批示待反馈;4-事项批示已反馈5-事项结项")
    private Integer status;

    private String orderNum;

    @ApiModelProperty(value = "类型：1领导关注 2领导批示")
    private Integer replyType;

    @ApiModelProperty(value = "类型：1关注 0取消关注")
    private Integer attentionStatus;

    @ApiModelProperty(value = "默认0批示后反馈；1未批示反馈")
    private Integer fbCommentType;


    @ApiModelProperty(value = "事件详情")
    private DynamicEvent eventDetail;





}

