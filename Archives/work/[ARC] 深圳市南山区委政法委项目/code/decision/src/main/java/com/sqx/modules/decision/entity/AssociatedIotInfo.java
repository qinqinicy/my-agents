package com.sqx.modules.decision.entity;

import io.swagger.annotations.*;

import java.util.Date;
import javax.persistence.*;

@Table(name = "associated_iot_info")
public class AssociatedIotInfo {
    @Id
    private Integer id;
    @Column(name = "event_uuid")
    private String eventUuid;
    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    /**
     * 事件信息主键
     */
    @Column(name = "event_id")
    private Integer eventId;

    /**
     * 物联感知ID
     */
    @ApiModelProperty(value = "物联感知ID")
    @Column(name = "perception_id")
    private String perceptionId;

    /**
     * 物联感知地址
     */
    @ApiModelProperty(value = "物联感知地址")
    private String address;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return created_time
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @return updated_time
     */
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * @param updatedTime
     */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    /**
     * 获取事件信息主键
     *
     * @return event_id - 事件信息主键
     */
    public Integer getEventId() {
        return eventId;
    }

    /**
     * 设置事件信息主键
     *
     * @param eventId 事件信息主键
     */
    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    /**
     * 获取物联感知ID
     *
     * @return perception_id - 物联感知ID
     */
    public String getPerceptionId() {
        return perceptionId;
    }

    /**
     * 设置物联感知ID
     *
     * @param perceptionId 物联感知ID
     */
    public void setPerceptionId(String perceptionId) {
        this.perceptionId = perceptionId;
    }

    /**
     * 获取物联感知地址
     *
     * @return address - 物联感知地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置物联感知地址
     *
     * @param address 物联感知地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    public String getEventUuid() {
        return eventUuid;
    }

    public void setEventUuid(String eventUuid) {
        this.eventUuid = eventUuid;
    }
}