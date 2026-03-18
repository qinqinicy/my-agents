package com.sqx.modules.decision.entity;

import io.swagger.annotations.*;

import java.util.Date;
import javax.persistence.*;

@Table(name = "associated_legal_info")
@ApiModel
public class AssociatedLegalInfo {
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
     * 关联法人主体名称
     */
    @ApiModelProperty(value = "关联法人主体名称")
    private String name;

    /**
     * 关联法人登记名称
     */
    @ApiModelProperty(value = "关联法人登记名称")
    @Column(name = "registered_name")
    private String registeredName;

    /**
     * 关联法人注册地址
     */
    @ApiModelProperty(value = "关联法人注册地址")
    private String address;

    /**
     * 关联法人实际经营地址
     */
    @ApiModelProperty(value = "关联法人实际经营地址")
    @Column(name = "business_address")
    private String businessAddress;

    /**
     * 关联法人企业联系电话
     */
    @ApiModelProperty(value = "关联法人企业联系电话")
    private String phone;

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
     * 获取关联法人主体名称
     *
     * @return name - 关联法人主体名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置关联法人主体名称
     *
     * @param name 关联法人主体名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取关联法人登记名称
     *
     * @return registered_name - 关联法人登记名称
     */
    public String getRegisteredName() {
        return registeredName;
    }

    /**
     * 设置关联法人登记名称
     *
     * @param registeredName 关联法人登记名称
     */
    public void setRegisteredName(String registeredName) {
        this.registeredName = registeredName;
    }

    /**
     * 获取关联法人注册地址
     *
     * @return address - 关联法人注册地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置关联法人注册地址
     *
     * @param address 关联法人注册地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取关联法人实际经营地址
     *
     * @return business_address - 关联法人实际经营地址
     */
    public String getBusinessAddress() {
        return businessAddress;
    }

    /**
     * 设置关联法人实际经营地址
     *
     * @param businessAddress 关联法人实际经营地址
     */
    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    /**
     * 获取关联法人企业联系电话
     *
     * @return phone - 关联法人企业联系电话
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置关联法人企业联系电话
     *
     * @param phone 关联法人企业联系电话
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEventUuid() {
        return eventUuid;
    }

    public void setEventUuid(String eventUuid) {
        this.eventUuid = eventUuid;
    }
}