package com.sqx.modules.decision.entity.third;

import io.swagger.annotations.*;
import lombok.*;

import java.util.*;

@Data
@ApiModel
public class OrderCreateParamBasic {

    private String orderNum;
    private String recAppCode;
    private String sendAppCode;
    private String dataPackage;
    private String copyApp;
    private String userId;
    private String senderId;
    private String sendTime;
    private String eventCode;
    private String applyTime;
    private String userType;
    private String name;
    private String contactNo;
    private String eventContent ;
    private String addressCode ;
    private Integer orderLevel;
    private String originalOrderNum;
    private List<Attachment> attachment;



}

