package com.sqx.modules.decision.entity.key;

import com.sqx.modules.decision.entity.event.*;
import io.swagger.annotations.*;
import lombok.*;

@Data
@ApiModel
public class EventCalParam extends UserInfoBase {
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
    @ApiModelProperty(value = "事件分类：纠纷化解、投诉建议、心理服务、法律服务、帮扶救助")
    private String eventClass;


    @ApiModelProperty(value = "事件五种分类：维稳事件、重点事件、矛盾纠纷、网格事件、民生诉求")
    private String eventSourceClass;

    @ApiModelProperty(value = "紧急标签：一般件、紧急件、督办件")
    private String emergencyLable;

    @ApiModelProperty(value = "涉及领域：劳资纠纷、涉房地产、涉教育...")
    private String relatedField;

    @ApiModelProperty(value = "区域分布：区内，区外")
    private String areaDistribution;

    @ApiModelProperty(value = "主办单位")
    private String organizer;



    @ApiModelProperty(value = "事件类型：一般事件、即采即办")
    private String eventType;

    private Integer area;

    private String departmentCode;





}
