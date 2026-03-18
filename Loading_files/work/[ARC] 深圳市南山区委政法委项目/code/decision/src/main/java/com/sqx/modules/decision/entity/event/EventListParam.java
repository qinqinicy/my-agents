package com.sqx.modules.decision.entity.event;

import com.sqx.modules.decision.entity.*;
import io.swagger.annotations.*;
import lombok.*;

@Data
@ApiModel
public class EventListParam  extends UserInfoBase {
    @ApiModelProperty(value = "审核状态 0 未审核 1 通过 2不通过 默认 0")
    private String examineStatus;
    @ApiModelProperty(value = "事件来源")
    private String appealSource;
    @ApiModelProperty(value = "处理状态 0未处理 1处理中 2已处理 默认0")
    private String handleStatus;
    @ApiModelProperty(value = "区域")
    private String incidentArea;
    @ApiModelProperty(value = "发生时间")
    private String beginDate ;
    @ApiModelProperty(value = "发生时间")
    private String endDate ;
    @ApiModelProperty(value = "事件名称")
    private String name ;
    @ApiModelProperty(value = "风险等级")
    private String dangerLevel;
    @ApiModelProperty(value = "事件分类")
    private String eventClass;

    @ApiModelProperty(value = "推送时间")
    private String pushBeginDate ;
    @ApiModelProperty(value = "推送时间")
    private String pushEndDate ;

    @ApiModelProperty(value = "首页显示5条传一个任何值")
    private String today;


    private Integer area;

    private String departmentCode;

    private String isAudit;

    private String streetName;

    private String userId;

    private String auditUserId;


    private String createDate;

    private String createEndDate;
}
