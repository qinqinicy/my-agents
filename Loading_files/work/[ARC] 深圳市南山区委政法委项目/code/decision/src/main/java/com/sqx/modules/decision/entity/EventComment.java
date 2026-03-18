package com.sqx.modules.decision.entity;

import io.swagger.annotations.*;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Table(name = "event_comment")
@Data
public class EventComment {
    @Id
    private Integer id;

    @Column(name = "event_uuid")
    private String eventUuid;

    /**
     * 创建时间
     */
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 更新时间
     */
    @Column(name = "updated_time")
    private Date updatedTime;

    /**
     * 事件信息主键
     */
    @Column(name = "event_id")
    private Integer eventId;

    /**
     * 关注人员列表
     */
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    /**
     * 批示内容
     */
    @ApiModelProperty(value = "批示反馈内容")
    private String comment;

    /**
     * 接收单位
     */
    @Column(name = "receive_unit")
    @ApiModelProperty(value = "接收单位")
    private String receiveUnit;
    @ApiModelProperty(value = "接收单位code")
    @Column(name = "receive_code")
    private  String receiveCode;
    @ApiModelProperty(value = "批示反馈单位名称")
    private String unitCode;
    @ApiModelProperty(value = "批示反馈单位code")
    private String unitName;
    @Column(name = "matter_number")
    @ApiModelProperty(value = "批示批次号(默认1）")
    private String matterNumber;

    @Column(name = "matter_uuid")
    @ApiModelProperty(value = "工单ID")
    private String matter_uuid;

    @ApiModelProperty(value = "批示类型")
    @Column(name = "decision_comment_type")
    private String decisionCommentType;

    @ApiModelProperty(value = "反馈批示类型")
    @Column(name = "feedback_comment_type")
    private String feedbackCommentType;


    @ApiModelProperty(value = "工单编号")
    @Column(name = "order_num")
    private String orderNum;



}