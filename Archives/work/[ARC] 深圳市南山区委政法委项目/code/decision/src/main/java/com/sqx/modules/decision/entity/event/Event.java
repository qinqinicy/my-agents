package com.sqx.modules.decision.entity.event;

import io.swagger.annotations.*;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Data
public class Event {
    private Integer id;
    @ApiModelProperty(value = "事件名称")
    private String name;
    @ApiModelProperty(value = "事件编码")
    private String eventNumber;
    @ApiModelProperty(value = "发生时间")
    private Date appearTime;
    @ApiModelProperty(value = "发生地址")
    private String subjectAddress;
    @ApiModelProperty(value = "处理状态")
    private String handleStatus;
    @ApiModelProperty(value = "事件来源")
    private String eventSource;
    @ApiModelProperty(value = "事发区域")
    private String incidentArea;
    @ApiModelProperty(value = "诉求来源")
    private String appealSource;
    @ApiModelProperty(value = "工单类型")
    private String workOrderType;
    @ApiModelProperty(value = "事项分类")
    private String eventClass;
    @ApiModelProperty(value = "当前处置部门")
    private String currentDisposalDep;
    @ApiModelProperty(value = "流转环节")
    private String circulationLink;
    @ApiModelProperty(value = "事发地址")
    private String takeAddress;
    @ApiModelProperty(value = "涉事主体")
    private String subjectInvolved;
    @ApiModelProperty(value = "事发地点")
    private String takePlace;
    @ApiModelProperty(value = "事件/诉求内容")
    private String eventContent;
    @ApiModelProperty(value = "事发区域")
    private String supplementaryInformation;
    @ApiModelProperty(value = "所属社区")
    private String community;
    @ApiModelProperty(value = "所属社区编码")
    private String communityCode;
    @ApiModelProperty(value = "隐患等级")
    private String dangerLevel;

    @ApiModelProperty(value = "标签")
    private String lable;

    private String sendTime;
    private Date pushTime;
    private Date auditPushTime;

    private String examineStatus;


    @ApiModelProperty(value = "事件五种分类：维稳事件、重点事件、矛盾纠纷、网格事件、民生诉求")
    private String eventSourceClass;

    @ApiModelProperty(value = "涉及领域：劳资纠纷、涉房地产、涉教育...")
    private String relatedField;

    @ApiModelProperty(value = "区域分布：区内，区外")
    private String areaDistribution;

    @ApiModelProperty(value = "主办单位")
    private String organizer;

    @ApiModelProperty(value = "事件类型：一般事件、即采即办")
    private String eventType;

    @ApiModelProperty(value = "紧急标签：一般件、紧急件、督办件")
    private String emergencyLable;

    @ApiModelProperty(value = "所属街道")
    private String street;

    @ApiModelProperty(value = "所属街道code")
    private String streetCode;


    @ApiModelProperty(value = "所属网格编码")
    private String owningGridCode;

    @ApiModelProperty(value = "事项来源业务系统(0-分拨;1-网格;2-政法平安;4-区民意速办")
    private String systemSource;

    @ApiModelProperty(value = "上报单位")
    private String unitSource;

    @ApiModelProperty(value = "上报人名称")
    private String unitUsername;
    @ApiModelProperty(value = "主批示人名称")
    private String decisionPersonName;
    @ApiModelProperty(value = "抄送查阅人,用英文;分割")
    private String readPersonName;

    @ApiModelProperty(value = "审核人ID")
    private String auditUserId;


}
