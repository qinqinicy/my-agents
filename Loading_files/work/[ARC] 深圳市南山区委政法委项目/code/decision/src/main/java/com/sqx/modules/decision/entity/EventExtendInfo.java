package com.sqx.modules.decision.entity;

import io.swagger.annotations.*;

import java.util.Date;
import javax.persistence.*;

@Table(name = "event_extend_info")
public class EventExtendInfo {
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
     * 是否疑难工单
     */
    @ApiModelProperty(value = "是否疑难工单")
    @Column(name = "trouble_ticket")
    private String troubleTicket;

    /**
     * 是否突发事件1是 0否
     */
    @ApiModelProperty(value = "否突发事件1是 0否")
    private String emergency;

    /**
     * 突发类型1是 0否
     */
    @ApiModelProperty(value = "突发类型1是 0否")
    @Column(name = "burst_type")
    private String burstType;

    /**
     * 是否城管类工单
     */
    @ApiModelProperty(value = "是否城管类工单")
    @Column(name = "urban_management_work_order")
    private String urbanManagementWorkOrder;

    /**
     * 是否领导接电
     */
    @ApiModelProperty(value = "是否领导接电")
    @Column(name = "leader_connection")
    private String leaderConnection;

    /**
     * 是否重办单
     */
    @ApiModelProperty(value = "是否重办单")
    private String reorder;

    /**
     * 是否首派责任工单
     */
    @ApiModelProperty(value = "是否首派责任工单")
    @Column(name = "first_dispatch_duty_order")
    private String firstDispatchDutyOrder;

    /**
     * 事项影响情况
     */
    @ApiModelProperty(value = "事项影响情况")
    @Column(name = "event_impact")
    private String eventImpact;

    /**
     * 市民要求回复方式
     */
    @ApiModelProperty(value = "市民要求回复方式")
    @Column(name = "citizens_ask_reply")
    private String citizensAskReply;

    /**
     * 回复备注
     */
    @ApiModelProperty(value = "回复备注")
    @Column(name = "reply_note")
    private String replyNote;

    /**
     * 市民备注
     */
    @ApiModelProperty(value = "是否疑难工单")
    @Column(name = "citizen_note")
    private String citizenNote;

    /**
     * 备注地址
     */
    @ApiModelProperty(value = "备注地址")
    @Column(name = "note_address")
    private String noteAddress;

    /**
     * 对象分类
     */
    @ApiModelProperty(value = "是否疑难工单")
    @Column(name = "object_classification")
    private String objectClassification;

    /**
     * 业务点选
     */
    @ApiModelProperty(value = "业务点选")
    @Column(name = "service_click")
    private String serviceClick;

    /**
     * 事项关键字
     */
    @ApiModelProperty(value = "事项关键字")
    @Column(name = "transaction_key")
    private String transactionKey;

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
     * 获取是否疑难工单
     *
     * @return trouble_ticket - 是否疑难工单
     */
    public String getTroubleTicket() {
        return troubleTicket;
    }

    /**
     * 设置是否疑难工单
     *
     * @param troubleTicket 是否疑难工单
     */
    public void setTroubleTicket(String troubleTicket) {
        this.troubleTicket = troubleTicket;
    }

    /**
     * 获取是否突发事件1是 0否
     *
     * @return emergency - 是否突发事件1是 0否
     */
    public String getEmergency() {
        return emergency;
    }

    /**
     * 设置是否突发事件1是 0否
     *
     * @param emergency 是否突发事件1是 0否
     */
    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    /**
     * 获取突发类型1是 0否
     *
     * @return burst_type - 突发类型1是 0否
     */
    public String getBurstType() {
        return burstType;
    }

    /**
     * 设置突发类型1是 0否
     *
     * @param burstType 突发类型1是 0否
     */
    public void setBurstType(String burstType) {
        this.burstType = burstType;
    }

    /**
     * 获取是否城管类工单
     *
     * @return urban_management_work_order - 是否城管类工单
     */
    public String getUrbanManagementWorkOrder() {
        return urbanManagementWorkOrder;
    }

    /**
     * 设置是否城管类工单
     *
     * @param urbanManagementWorkOrder 是否城管类工单
     */
    public void setUrbanManagementWorkOrder(String urbanManagementWorkOrder) {
        this.urbanManagementWorkOrder = urbanManagementWorkOrder;
    }

    /**
     * 获取是否领导接电
     *
     * @return leader_connection - 是否领导接电
     */
    public String getLeaderConnection() {
        return leaderConnection;
    }

    /**
     * 设置是否领导接电
     *
     * @param leaderConnection 是否领导接电
     */
    public void setLeaderConnection(String leaderConnection) {
        this.leaderConnection = leaderConnection;
    }

    /**
     * 获取是否重办单
     *
     * @return reorder - 是否重办单
     */
    public String getReorder() {
        return reorder;
    }

    /**
     * 设置是否重办单
     *
     * @param reorder 是否重办单
     */
    public void setReorder(String reorder) {
        this.reorder = reorder;
    }

    /**
     * 获取是否首派责任工单
     *
     * @return first_dispatch_duty_order - 是否首派责任工单
     */
    public String getFirstDispatchDutyOrder() {
        return firstDispatchDutyOrder;
    }

    /**
     * 设置是否首派责任工单
     *
     * @param firstDispatchDutyOrder 是否首派责任工单
     */
    public void setFirstDispatchDutyOrder(String firstDispatchDutyOrder) {
        this.firstDispatchDutyOrder = firstDispatchDutyOrder;
    }

    /**
     * 获取事项影响情况
     *
     * @return event_impact - 事项影响情况
     */
    public String getEventImpact() {
        return eventImpact;
    }

    /**
     * 设置事项影响情况
     *
     * @param eventImpact 事项影响情况
     */
    public void setEventImpact(String eventImpact) {
        this.eventImpact = eventImpact;
    }

    /**
     * 获取市民要求回复方式
     *
     * @return citizens_ask_reply - 市民要求回复方式
     */
    public String getCitizensAskReply() {
        return citizensAskReply;
    }

    /**
     * 设置市民要求回复方式
     *
     * @param citizensAskReply 市民要求回复方式
     */
    public void setCitizensAskReply(String citizensAskReply) {
        this.citizensAskReply = citizensAskReply;
    }

    /**
     * 获取回复备注
     *
     * @return reply_note - 回复备注
     */
    public String getReplyNote() {
        return replyNote;
    }

    /**
     * 设置回复备注
     *
     * @param replyNote 回复备注
     */
    public void setReplyNote(String replyNote) {
        this.replyNote = replyNote;
    }

    /**
     * 获取市民备注
     *
     * @return citizen_note - 市民备注
     */
    public String getCitizenNote() {
        return citizenNote;
    }

    /**
     * 设置市民备注
     *
     * @param citizenNote 市民备注
     */
    public void setCitizenNote(String citizenNote) {
        this.citizenNote = citizenNote;
    }

    /**
     * 获取备注地址
     *
     * @return note_address - 备注地址
     */
    public String getNoteAddress() {
        return noteAddress;
    }

    /**
     * 设置备注地址
     *
     * @param noteAddress 备注地址
     */
    public void setNoteAddress(String noteAddress) {
        this.noteAddress = noteAddress;
    }

    /**
     * 获取对象分类
     *
     * @return object_classification - 对象分类
     */
    public String getObjectClassification() {
        return objectClassification;
    }

    /**
     * 设置对象分类
     *
     * @param objectClassification 对象分类
     */
    public void setObjectClassification(String objectClassification) {
        this.objectClassification = objectClassification;
    }

    /**
     * 获取业务点选
     *
     * @return service_click - 业务点选
     */
    public String getServiceClick() {
        return serviceClick;
    }

    /**
     * 设置业务点选
     *
     * @param serviceClick 业务点选
     */
    public void setServiceClick(String serviceClick) {
        this.serviceClick = serviceClick;
    }

    /**
     * 获取事项关键字
     *
     * @return transaction_key - 事项关键字
     */
    public String getTransactionKey() {
        return transactionKey;
    }

    /**
     * 设置事项关键字
     *
     * @param transactionKey 事项关键字
     */
    public void setTransactionKey(String transactionKey) {
        this.transactionKey = transactionKey;
    }

    public String getEventUuid() {
        return eventUuid;
    }

    public void setEventUuid(String eventUuid) {
        this.eventUuid = eventUuid;
    }
}