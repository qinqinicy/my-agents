package com.sqx.modules.decision.entity;

import io.swagger.annotations.*;

import java.util.Date;
import javax.persistence.*;

@Table(name = "process_flow")
public class ProcessFlow {
    @Id
    private Integer id;
    @Column(name = "event_uuid")
    private String eventUuid;
    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    @Column(name = "event_id")
    private Integer eventId;

    /**
     * 所属社区
     */
    @ApiModelProperty(value = "所属社区")
    private String community;

    /**
     * 处理部门
     */
    @ApiModelProperty(value = "处理部门")
    @Column(name = "disposal_dep")
    private String disposalDep;

    /**
     * 处理部门编码
     */
    @ApiModelProperty(value = "处理部门")
    @Column(name = "disposal_dep_code")
    private String disposalDepCode;

    /**
     * 处理人
     */
    @ApiModelProperty(value = "处理人")
    @Column(name = "processing_personnel")
    private String processingPersonnel;

    /**
     * 处理人id
     */
    @ApiModelProperty(value = "处理人id")
    @Column(name = "processing_personnel_id")
    private String processingPersonnelId;

    /**
     * 处理人意见
     */
    @ApiModelProperty(value = "处理人意见")
    @Column(name = "personnel_opinion")
    private String personnelOpinion;

    /**
     * 图片地址
     */
    @ApiModelProperty(value = "图片地址")
    @Column(name = "file_url")
    private String fileUrl;

    /**
     * 评价内容
     */
    @ApiModelProperty(value = "评价内容")
    @Column(name = "evaluation_content")
    private String evaluationContent;

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
     * @return event_id
     */
    public Integer getEventId() {
        return eventId;
    }

    /**
     * @param eventId
     */
    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    /**
     * 获取所属社区
     *
     * @return community - 所属社区
     */
    public String getCommunity() {
        return community;
    }

    /**
     * 设置所属社区
     *
     * @param community 所属社区
     */
    public void setCommunity(String community) {
        this.community = community;
    }

    /**
     * 获取处理部门
     *
     * @return disposal_dep - 处理部门
     */
    public String getDisposalDep() {
        return disposalDep;
    }

    /**
     * 设置处理部门
     *
     * @param disposalDep 处理部门
     */
    public void setDisposalDep(String disposalDep) {
        this.disposalDep = disposalDep;
    }

    /**
     * 获取处理部门编码
     *
     * @return disposal_dep_code - 处理部门编码
     */
    public String getDisposalDepCode() {
        return disposalDepCode;
    }

    /**
     * 设置处理部门编码
     *
     * @param disposalDepCode 处理部门编码
     */
    public void setDisposalDepCode(String disposalDepCode) {
        this.disposalDepCode = disposalDepCode;
    }

    /**
     * 获取处理人
     *
     * @return processing_personnel - 处理人
     */
    public String getProcessingPersonnel() {
        return processingPersonnel;
    }

    /**
     * 设置处理人
     *
     * @param processingPersonnel 处理人
     */
    public void setProcessingPersonnel(String processingPersonnel) {
        this.processingPersonnel = processingPersonnel;
    }

    /**
     * 获取处理人id
     *
     * @return processing_personnel_id - 处理人id
     */
    public String getProcessingPersonnelId() {
        return processingPersonnelId;
    }

    /**
     * 设置处理人id
     *
     * @param processingPersonnelId 处理人id
     */
    public void setProcessingPersonnelId(String processingPersonnelId) {
        this.processingPersonnelId = processingPersonnelId;
    }

    /**
     * 获取处理人意见
     *
     * @return personnel_opinion - 处理人意见
     */
    public String getPersonnelOpinion() {
        return personnelOpinion;
    }

    /**
     * 设置处理人意见
     *
     * @param personnelOpinion 处理人意见
     */
    public void setPersonnelOpinion(String personnelOpinion) {
        this.personnelOpinion = personnelOpinion;
    }

    /**
     * 获取评价次数
     *
     * @return file_url - 评价次数
     */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
     * 设置评价次数
     *
     * @param fileUrl 评价次数
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    /**
     * 获取评价内容
     *
     * @return evaluation_content - 评价内容
     */
    public String getEvaluationContent() {
        return evaluationContent;
    }

    /**
     * 设置评价内容
     *
     * @param evaluationContent 评价内容
     */
    public void setEvaluationContent(String evaluationContent) {
        this.evaluationContent = evaluationContent;
    }

    public String getEventUuid() {
        return eventUuid;
    }

    public void setEventUuid(String eventUuid) {
        this.eventUuid = eventUuid;
    }
}