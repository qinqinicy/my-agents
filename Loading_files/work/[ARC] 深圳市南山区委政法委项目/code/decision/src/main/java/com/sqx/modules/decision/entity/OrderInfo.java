package com.sqx.modules.decision.entity;

import io.swagger.annotations.*;
import lombok.*;
import org.springframework.validation.annotation.*;

import javax.persistence.*;
import java.util.*;

@Table(name = "order_info")
@Data
public class OrderInfo {
    @Id
    private Integer id;

    /**
     * 事项id
     */
    @Column(name = "event_uuid")
    private String eventUuid;

    /**
     * 业务主ID
     */
    @Column(name = "cata_id")
    private String cataId;

    /**
     * 工单编号
     */
    @Column(name = "order_num")
    private String orderNum;

    @Column(name = "matter_uuid")
    private String matterUuid;

    @Column(name = "create_time")
    private Date createTime;

    /**
     * 发送方系统编码
     */
    @Column(name = "send_app_code")
    private String sendAppCode;

    /**
     * 接收系统编码
     */
    @Column(name = "rec_app_code")
    private String recAppCode;

    /**
     * 发送人ID
     */
    @Column(name = "sender_id")
    private String senderId;

    /**
     * 发送时间
     */
    @Column(name = "send_time")
    private String sendTime;

    /**
     * 事项编码
     */
    @Column(name = "event_code")
    private String eventCode;

    @Column(name = "apply_time")
    private String applyTime;

    /**
     * 人员类型编码01：政务人员 02：平安员 03：楼栋长
     */
    @Column(name = "user_type")
    private String userType;

    /**
     * 用户 id, 指用户在业务系统中的 ID
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 电话号码
     */
    @Column(name = "contact_no")
    private String contactNo;

    /**
     * 事件描述
     */
    @Column(name = "event_content")
    private String eventContent;

    /**
     * 标准地址
     */
    @Column(name = "address_code")
    private String addressCode;

    /**
     * 数据包
     */
    @Column(name = "data_package")
    private String dataPackage;

    /**
     * 工单类型1事项 2批示
     */
    @Column(name = "order_type")
    private String orderType;


    @Column(name = "order_state")
    private String orderState;

    private String catalog;

    @Column(name = "data_status")
    private String data_status;


    @Column(name = "order_json")
    private String orderJson;

    @Column(name = "in_or_out")
    private String inOrOut;


}