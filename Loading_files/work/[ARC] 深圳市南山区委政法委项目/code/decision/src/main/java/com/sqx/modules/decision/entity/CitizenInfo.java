package com.sqx.modules.decision.entity;

import io.swagger.annotations.*;

import java.util.Date;
import javax.persistence.*;

@Table(name = "citizen_info")
public class CitizenInfo {
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
     * 市民称呼
     */
    @ApiModelProperty(value = "市民称呼")
    private String name;

    /**
     * 市民性别
     */
    private String sex;

    /**
     * 市民身份
     */
    @ApiModelProperty(value = "市民身份")
    private String standing;

    /**
     * 来电号码
     */
    @ApiModelProperty(value = "来电号码")
    @Column(name = "calling_number")
    private String callingNumber;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    @Column(name = "contact_number")
    private String contactNumber;

    /**
     * 证件类型
     */
    @ApiModelProperty(value = "证件类型")
    @Column(name = "document_type")
    private String documentType;

    /**
     * 证件号码
     */
    @ApiModelProperty(value = "证件号码")
    @Column(name = "document_number")
    private String documentNumber;

    /**
     * 是否党员1是 0否
     */
    @ApiModelProperty(value = "否党员1是 0否")
    @Column(name = "party_member")
    private String partyMember;

    /**
     * 是否保密1是 0否
     */
    @ApiModelProperty(value = "是否保密1是 0否")
    private String confidentiality;

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
     * 获取创建时间
     *
     * @return created_time - 创建时间
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * 设置创建时间
     *
     * @param createdTime 创建时间
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * 获取更新时间
     *
     * @return updated_time - 更新时间
     */
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * 设置更新时间
     *
     * @param updatedTime 更新时间
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
     * 获取市民称呼
     *
     * @return name - 市民称呼
     */
    public String getName() {
        return name;
    }

    /**
     * 设置市民称呼
     *
     * @param name 市民称呼
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取市民性别
     *
     * @return sex - 市民性别
     */
    public String getSex() {
        return sex;
    }

    /**
     * 设置市民性别
     *
     * @param sex 市民性别
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * 获取市民身份
     *
     * @return standing - 市民身份
     */
    public String getStanding() {
        return standing;
    }

    /**
     * 设置市民身份
     *
     * @param standing 市民身份
     */
    public void setStanding(String standing) {
        this.standing = standing;
    }

    /**
     * 获取来电号码
     *
     * @return calling_number - 来电号码
     */
    public String getCallingNumber() {
        return callingNumber;
    }

    /**
     * 设置来电号码
     *
     * @param callingNumber 来电号码
     */
    public void setCallingNumber(String callingNumber) {
        this.callingNumber = callingNumber;
    }

    /**
     * 获取联系电话
     *
     * @return contact_number - 联系电话
     */
    public String getContactNumber() {
        return contactNumber;
    }

    /**
     * 设置联系电话
     *
     * @param contactNumber 联系电话
     */
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    /**
     * 获取证件类型
     *
     * @return document_type - 证件类型
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     * 设置证件类型
     *
     * @param documentType 证件类型
     */
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    /**
     * 获取证件号码
     *
     * @return document_number - 证件号码
     */
    public String getDocumentNumber() {
        return documentNumber;
    }

    /**
     * 设置证件号码
     *
     * @param documentNumber 证件号码
     */
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    /**
     * 获取是否党员1是 0否
     *
     * @return party_member - 是否党员1是 0否
     */
    public String getPartyMember() {
        return partyMember;
    }

    /**
     * 设置是否党员1是 0否
     *
     * @param partyMember 是否党员1是 0否
     */
    public void setPartyMember(String partyMember) {
        this.partyMember = partyMember;
    }

    /**
     * 获取是否保密1是 0否
     *
     * @return confidentiality - 是否保密1是 0否
     */
    public String getConfidentiality() {
        return confidentiality;
    }

    /**
     * 设置是否保密1是 0否
     *
     * @param confidentiality 是否保密1是 0否
     */
    public void setConfidentiality(String confidentiality) {
        this.confidentiality = confidentiality;
    }

    public String getEventUuid() {
        return eventUuid;
    }

    public void setEventUuid(String eventUuid) {
        this.eventUuid = eventUuid;
    }
}