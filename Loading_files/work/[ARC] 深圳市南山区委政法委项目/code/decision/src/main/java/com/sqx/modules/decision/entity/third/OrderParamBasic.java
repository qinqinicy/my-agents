package com.sqx.modules.decision.entity.third;

import io.swagger.annotations.*;
import lombok.*;

@Data
@ApiModel
public class OrderParamBasic {

    private String orderNum;
    private String recAppCode;
    private String sendAppCode;
    private String newRecAppCode;
    private String dataPackage;
    private String userId;
    private String  userName;
    private String handleApp;
    private String orderState;
    private String processAction;



    private String senderId;
    private String sendTime;
    private String eventCode;
    private String applyTime;
    private String userType;
    private String name;


}

