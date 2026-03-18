package com.sqx.modules.decision.entity.third;

import com.sqx.modules.decision.entity.*;
import io.swagger.annotations.*;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Data
@ApiModel
public class EventBasic {

    @ApiModelProperty(value = "工单信息唯一序列号")
    private String matterUuid;

    @ApiModelProperty(value = "事件名称")
    private String name;

    @ApiModelProperty(value = "发生地点")
    private String takePlace;

    @ApiModelProperty(value = "事发时间")
    private String appearTime;

    @ApiModelProperty(value = "事项概括")
    private String matterSummarize;

    @ApiModelProperty(value = "事项来源业务系统(0-分拨;1-网格;2-政法平安;4-区民意速办)")
    private String matterSource;

    @ApiModelProperty(value = "关联的主事件ID（用于查询业务系统的事件")
    private String eventMID;

    @ApiModelProperty(value = "关联其他次要事件ID（用于查询业务系统的事件详情及状态）")
    private String eventFID;

    @ApiModelProperty(value = "附件地址 ")
    private List<Attachment> attachment;

    @ApiModelProperty(value = "事项类型(默认值1事件) 2简报")
    private Integer matterType;

    @ApiModelProperty(value = "来源单位(街道或社区单位主体,暂不做要求）")
    private String unitSource;

    @ApiModelProperty(value = "主批示人ID")
    private String decisionPersonID;
    @ApiModelProperty(value = "主批示人名称")
    private String decisionPersonName;
    @ApiModelProperty(value = "抄送查阅人ID,用英文;分割")
    private String readPersonID;
    @ApiModelProperty(value = "抄送查阅人,用英文;分割")
    private String readPersonName;

    @ApiModelProperty(value = "0-事项已上报;1-事项审核通过;2-事项审核失败(不进入今日领导关注事项):3-事项已批示待反馈;4-事项批示已反馈5-事项结项")
    private Integer status;

    @ApiModelProperty(value = "批示批次号(默认1）")
    private String matterNumber;

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

    @ApiModelProperty(value = "批示创建时间")
    private String createTime;

    @ApiModelProperty(value = "批示类型")
    private String decisionCommentType;

    @ApiModelProperty(value = "反馈批示类型")
    private String feedbackCommentType;


    private String systemid;


    @ApiModelProperty(value = "默认0批示后反馈；1未批示反馈")
    private Integer fbCommentType;

    @ApiModelProperty(value = "上报单位")
    private String userDpt;

    @ApiModelProperty(value = "街道名称 用于审核员查询数据")
    private String streetName;

}

