package com.sqx.modules.decision.entity;

import io.swagger.annotations.*;

import java.util.Date;
import javax.persistence.*;

@Table(name = "satisfaction_info")
public class SatisfactionInfo {
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
     * 整体满意度
     */
    @ApiModelProperty(value = "整体满意度")
    @Column(name = "overall_satisfaction")
    private String overallSatisfaction;

    /**
     * 评价时间
     */
    @ApiModelProperty(value = "评价时间")
    @Column(name = "evaluation_time")
    private Date evaluationTime;

    /**
     * 处置速度
     */
    @ApiModelProperty(value = "处置速度")
    @Column(name = "disposal_speed")
    private Integer disposalSpeed;

    /**
     * 处置效果
     */
    @ApiModelProperty(value = "处置效果")
    @Column(name = "treatment_effect")
    private Integer treatmentEffect;

    /**
     * 服务体验
     */
    @ApiModelProperty(value = "服务体验")
    @Column(name = "service_experience")
    private Integer serviceExperience;

    /**
     * 反馈质量
     */
    @ApiModelProperty(value = "反馈质量")
    @Column(name = "feedback_quality")
    private Integer feedbackQuality;

    /**
     * 评价次数
     */
    @ApiModelProperty(value = "评价次数")
    @Column(name = "evaluation_frequency")
    private Integer evaluationFrequency;

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
     * 获取整体满意度
     *
     * @return overall_satisfaction - 整体满意度
     */
    public String getOverallSatisfaction() {
        return overallSatisfaction;
    }

    /**
     * 设置整体满意度
     *
     * @param overallSatisfaction 整体满意度
     */
    public void setOverallSatisfaction(String overallSatisfaction) {
        this.overallSatisfaction = overallSatisfaction;
    }

    /**
     * 获取评价时间
     *
     * @return evaluation_time - 评价时间
     */
    public Date getEvaluationTime() {
        return evaluationTime;
    }

    /**
     * 设置评价时间
     *
     * @param evaluationTime 评价时间
     */
    public void setEvaluationTime(Date evaluationTime) {
        this.evaluationTime = evaluationTime;
    }

    /**
     * 获取处置速度
     *
     * @return disposal_speed - 处置速度
     */
    public Integer getDisposalSpeed() {
        return disposalSpeed;
    }

    /**
     * 设置处置速度
     *
     * @param disposalSpeed 处置速度
     */
    public void setDisposalSpeed(Integer disposalSpeed) {
        this.disposalSpeed = disposalSpeed;
    }

    /**
     * 获取处置效果
     *
     * @return treatment_effect - 处置效果
     */
    public Integer getTreatmentEffect() {
        return treatmentEffect;
    }

    /**
     * 设置处置效果
     *
     * @param treatmentEffect 处置效果
     */
    public void setTreatmentEffect(Integer treatmentEffect) {
        this.treatmentEffect = treatmentEffect;
    }

    /**
     * 获取服务体验
     *
     * @return service_experience - 服务体验
     */
    public Integer getServiceExperience() {
        return serviceExperience;
    }

    /**
     * 设置服务体验
     *
     * @param serviceExperience 服务体验
     */
    public void setServiceExperience(Integer serviceExperience) {
        this.serviceExperience = serviceExperience;
    }

    /**
     * 获取反馈质量
     *
     * @return feedback_quality - 反馈质量
     */
    public Integer getFeedbackQuality() {
        return feedbackQuality;
    }

    /**
     * 设置反馈质量
     *
     * @param feedbackQuality 反馈质量
     */
    public void setFeedbackQuality(Integer feedbackQuality) {
        this.feedbackQuality = feedbackQuality;
    }

    /**
     * 获取评价次数
     *
     * @return evaluation_frequency - 评价次数
     */
    public Integer getEvaluationFrequency() {
        return evaluationFrequency;
    }

    /**
     * 设置评价次数
     *
     * @param evaluationFrequency 评价次数
     */
    public void setEvaluationFrequency(Integer evaluationFrequency) {
        this.evaluationFrequency = evaluationFrequency;
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