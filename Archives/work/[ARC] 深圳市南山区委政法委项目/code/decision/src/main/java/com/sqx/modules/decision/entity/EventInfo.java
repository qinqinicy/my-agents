package com.sqx.modules.decision.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sqx.modules.decision.entity.third.*;
import io.swagger.annotations.*;
import lombok.*;

import java.util.*;
import javax.persistence.*;

@Table(name = "event_info")
@ApiModel
@Data
public class EventInfo {
    @Id
    private Integer id;

    @Column(name = "event_uuid")
    @ApiModelProperty(value = "事件信息的唯一序列号")
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
     * 事件名称
     */
    @ApiModelProperty(value = "事件名称")
    private String name;

    /**
     * 事件标签
     */
    @ApiModelProperty(value = "事件标签")
    private String lable;

    /**
     * 发生地
     */
    @ApiModelProperty(value = "发生地")
    @Column(name = "take_place")
    private String takePlace;

    /**
     * 发生时间
     */
    @ApiModelProperty(value = "发生时间")
    @Column(name = "appear_time")
    private Date appearTime;

    /**
     * 推送时间
     */
    @ApiModelProperty(value = "推送时间")
    @Column(name = "push_time")
    private Date pushTime;

    @ApiModelProperty(value = "审核推送时间")
    @Column(name = "audit_push_time")
    private Date auditPushTime;

    /**
     * 审核状态 0 未审核 1 通过 2不通过 默认 0
     */
    @ApiModelProperty(value = "审核状态 0 未审核 1 通过 2不通过 默认 0")
    @Column(name = "examine_status")
    private String examineStatus;

    /**
     * 工单类型
     */
    @ApiModelProperty(value = "工单类型")
    @Column(name = "work_order_type")
    private String workOrderType;

    /**
     * 诉求来源
     */
    @ApiModelProperty(value = "诉求来源")
    @Column(name = "appeal_source")
    private String appealSource;

    /**
     * 事件编号
     */
    @ApiModelProperty(value = "事件编号")
    @Column(name = "event_number")
    private String eventNumber;

    /**
     * 事项分类
     */
    @ApiModelProperty(value = "事项分类")
    @Column(name = "event_class")
    private String eventClass;

    /**
     * 当前处置部门
     */
    @ApiModelProperty(value = "当前处置部门")
    @Column(name = "current_disposal_dep")
    private String currentDisposalDep;

    /**
     * 当前处置部门code
     */
    @ApiModelProperty(value = "当前处置部门code")
    @Column(name = "current_disposal_dep_code")
    private String currentDisposalDepCode;

    /**
     * 流转环节
     */
    @ApiModelProperty(value = "流转环节")
    @Column(name = "circulation_link")
    private String circulationLink;

    /**
     * 事发地址
     */
    @ApiModelProperty(value = "事发地址")
    @Column(name = "take_address")
    private String takeAddress;

    /**
     * 涉事主体
     */
    @ApiModelProperty(value = "事件标签")
    @Column(name = "subject_involved")
    private String subjectInvolved;

    /**
     * 涉事主体编码
     */
    @ApiModelProperty(value = "涉事主体编码")
    @Column(name = "subject_code")
    private String subjectCode;

    /**
     * 涉事主体地址
     */
    @ApiModelProperty(value = "涉事主体地址")
    @Column(name = "subject_address")
    private String subjectAddress;

    /**
     * 事件/诉求内容
     */
    @ApiModelProperty(value = "事件/诉求内容")
    @Column(name = "event_content")
    private String eventContent;

    /**
     * 补充信息
     */
    @ApiModelProperty(value = "补充信息")
    @Column(name = "supplementary_information")
    private String supplementaryInformation;

    /**
     * 事发区域
     */
    @ApiModelProperty(value = "事发区域")
    @Column(name = "incident_area")
    private String incidentArea;

    /**
     * 事发区域编码
     */
    @ApiModelProperty(value = "事发区域编码")
    @Column(name = "incident_area_code")
    private String incidentAreaCode;

    /**
     * 所属社区
     */
    @ApiModelProperty(value = "所属社区")
    private String community;

    /**
     * 所属社区编码
     */
    @ApiModelProperty(value = "所属社区编码")
    @Column(name = "community_code")
    private String communityCode;

    /**
     * 所属网格
     */
    @ApiModelProperty(value = "所属网格")
    @Column(name = "owning_grid")
    private String owningGrid;

    /**
     * 隐患等级
     */
    @ApiModelProperty(value = "隐患等级")
    @Column(name = "danger_level")
    private String dangerLevel;

    /**
     * 事件来源
     */
    @ApiModelProperty(value = "事件来源")
    @Column(name = "event_source")
    private String eventSource;

    /**
     * 处理状态  0未处理 1处理中 2已处理 默认0
     */
    @ApiModelProperty(value = "处理状态  0未处理 1处理中 2已处理 默认0")
    @Column(name = "handle_status")
    private String handleStatus;

    private String citizenuuid;

    @ApiModelProperty(value = "事件信息详情")
    @Column(name = "event_info_detail")
    private String eventInfoDetail;

    @ApiModelProperty(value = "是否参与审核")
    @Column(name = "is_audit")
    private Boolean isAudit;

    @TableField(exist = false)
    private String attentionStatus;

    @ApiModelProperty(value = "事件五种分类：维稳事件、重点事件、矛盾纠纷、网格事件、民生诉求")
    @Column(name = "event_source_class")
    private String eventSourceClass;

    @ApiModelProperty(value = "涉及领域：劳资纠纷、涉房地产、涉教育...")
    @Column(name = "related_field")
    private String relatedField;

    @ApiModelProperty(value = "区域分布：区内，区外")
    @Column(name = "area_distribution")
    private String areaDistribution;

    @ApiModelProperty(value = "主办单位")
    private String organizer;

    @ApiModelProperty(value = "事件类型：一般事件、即采即办")
    @Column(name = "event_type")
    private String eventType;

    @ApiModelProperty(value = "紧急标签：一般件、紧急件、督办件")
    @Column(name = "emergency_lable")
    private String emergencyLable;

    @ApiModelProperty(value = "所属街道")
    private String street;

    @Column(name = "street_code")
    @ApiModelProperty(value = "所属街道code")
    private String streetCode;


    @ApiModelProperty(value = "所属网格编码")
    @Column(name = "owning_grid_code")
    private String owningGridCode;


    @ApiModelProperty(value = "事件概览 基本信息")
    @Column(name = "event_summarize")
    private String eventSummarize;

    @ApiModelProperty(value = "事项来源业务系统(0-分拨;1-网格;2-政法平安;4-区民意速办)")
    @Column(name = "matter_source")
    private String matterSource;

    @ApiModelProperty(value = "关联的主事件ID（用于查询业务系统的事件详情及状态）")
    @Column(name = "event_mid")
    private String eventMid;

    @ApiModelProperty(value = "关联其他次要事件ID（用于查询业务系统的事件详情及状态）")
    @Column(name = "event_fid")
    private String eventFid;

    @ApiModelProperty(value = "附件 URL")
    private String attachment;

    @ApiModelProperty(value = "事项类型(默认值0,预留字段)")
    @Column(name = "matter_type")
    private String matterType;

    @ApiModelProperty(value = "来源单位(街道或社区单位主体,暂不做要求）")
    @Column(name = "unit_source")
    private String unitSource;



    @ApiModelProperty(value = "主批示人ID")
    @Column(name = "decision_person_id")
    private String decisionPersonId;

    @Column(name = "decision_person_name")
    @ApiModelProperty(value = "主批示人名称")
    private String decisionPersonName;

    @Column(name = "read_person_id")
    @ApiModelProperty(value = "抄送查阅人ID,用英文;分割")
    private String readPersonId;

    @Column(name = "read_person_name")
    @ApiModelProperty(value = "抄送查阅人,用英文;分割")
    private String readPersonName;

    @ApiModelProperty(value = "0-事项已上报;1-事项审核通过;2-事项审核失败(不进入今日领导关注事项):3-事项已批示待反馈;4-事项批示已反馈5-事项结项")
    private String status;

    @ApiModelProperty(value = "工单信息唯一序列号")
    @Column(name = "matter_uuid")
    private String matterUuid;

    @ApiModelProperty(value = "工单号")
    @Column(name = "order_num")
    private String orderNum;

    private String systemid;

    @Column(name = "system_source")
    @ApiModelProperty(value = "分拨;网格;政法平安;区民意速办;")
    private String systemSource;

    @Column(name = "unit_username")
    @ApiModelProperty(value = "上报人名称")
    private String unitUsername;

    @Column(name = "unit_user_id")
    @ApiModelProperty(value = "上报人id")
    private String unitUserId;

    @ApiModelProperty(value = "附件")
    @TableField(select = false)
    List<AttachmentInfo> attachmentInfos;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "audit_user_id")
    private  String auditUserId;

    @Column(name = "audit_user_name")
    private String auditUserName;

    @Column(name = "data_source")
    @ApiModelProperty(value = "数据来源 1业务方推过来 2 领导主动批示")
    private String dataSource;

    @ApiModelProperty(value = "上报时间")
    private String sendTime;

    @ApiModelProperty(value = "上报人ID")
    @TableField(select = false)
    private String senderId;

    @ApiModelProperty(value = "上报人名称")
    @TableField(select = false)
    private String senderName;




}