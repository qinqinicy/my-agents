package com.sqx.modules.decision.entity.third;

import io.swagger.annotations.*;
import lombok.*;

@Data
@ApiModel
public class OrderEventProcessBasic {


    @ApiModelProperty(value = "工单号")
    private String orderNum;

    @ApiModelProperty(value = "发送方系统编码")
    private String sendAppCode;

    @ApiModelProperty(value = "接收方系统编码")
    private String recAppCode;

    @ApiModelProperty(value = "发送人ID")
    private String senderId;

    @ApiModelProperty(value = "发送时间")
    private String sendTime;

    @ApiModelProperty(value = "批示对象json数据包")
    private String dataPackage;


}

