package com.sqx.modules.decision.entity.third;

import io.swagger.annotations.*;
import lombok.*;

import java.util.*;

@Data
@ApiModel
public class OrderMessageBasic {

    @ApiModelProperty(value = "工单号")
    private String orderNum;

    @ApiModelProperty(value = "发送方系统编码")
    private String sendAppCode;

    @ApiModelProperty(value = "接收方系统编码")
    private String recAppCode;

    private String msgNode;

    private String messages;
    private String processAction;
    private String orderNumThird;


    private String orderState;

    private String catalog;

    private String cataId;

    @ApiModelProperty(value = "发送人ID")
    private String senderId;

    @ApiModelProperty(value = "发送时间")
    private String sendTime;

    private String eventCode;
    private String applyTime;
    private String userType;
    private String userId;
    private String name;
    private String contactNo;
    private String eventContent;
    private String addressCode;
    private String userDept;

    @ApiModelProperty(value = "事项对象json数据包")
    private String dataPackage;

    @ApiModelProperty(value = "附件地址 ")
    private List<Attachment> attachment;

}

