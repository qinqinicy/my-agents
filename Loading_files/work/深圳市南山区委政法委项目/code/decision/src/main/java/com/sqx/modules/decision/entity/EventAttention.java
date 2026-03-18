package com.sqx.modules.decision.entity;

import lombok.*;

import java.util.Date;
import javax.persistence.*;

@Table(name = "event_attention")
@Data
public class EventAttention {
    @Id
    private Integer id;

    @Column(name = "event_uuid")
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
     * 事件信息主键
     */
    @Column(name = "event_id")
    private Integer eventId;

    /**
     * 关注人员列表
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 关注状态 1关注 0不关注 默认0
     */
    @Column(name = "attention_status")
    private String attentionStatus;


}