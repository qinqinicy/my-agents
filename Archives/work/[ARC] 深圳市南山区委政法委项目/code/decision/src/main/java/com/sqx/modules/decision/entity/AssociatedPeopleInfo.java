package com.sqx.modules.decision.entity;

import io.swagger.annotations.*;

import java.util.Date;
import javax.persistence.*;

@Table(name = "associated_people_info")
public class AssociatedPeopleInfo {
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
     * 关联人员名称
     */
    @ApiModelProperty(value = "关联人员名称")
    @Column(name = "people_name")
    private String peopleName;

    /**
     * 关联人员年龄
     */
    @ApiModelProperty(value = "关联人员年龄")
    @Column(name = "people_age")
    private String peopleAge;

    /**
     * 关联人员电话
     */
    @ApiModelProperty(value = "关联人员电话")
    @Column(name = "people_phone")
    private String peoplePhone;

    /**
     * 关联人员身份证号码
     */
    @ApiModelProperty(value = "关联人员身份证号码")
    @Column(name = "peopleId_card")
    private String peopleidCard;

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
     * 获取关联人员名称
     *
     * @return people_name - 关联人员名称
     */
    public String getPeopleName() {
        return peopleName;
    }

    /**
     * 设置关联人员名称
     *
     * @param peopleName 关联人员名称
     */
    public void setPeopleName(String peopleName) {
        this.peopleName = peopleName;
    }

    /**
     * 获取关联人员年龄
     *
     * @return people_age - 关联人员年龄
     */
    public String getPeopleAge() {
        return peopleAge;
    }

    /**
     * 设置关联人员年龄
     *
     * @param peopleAge 关联人员年龄
     */
    public void setPeopleAge(String peopleAge) {
        this.peopleAge = peopleAge;
    }

    /**
     * 获取关联人员电话
     *
     * @return people_phone - 关联人员电话
     */
    public String getPeoplePhone() {
        return peoplePhone;
    }

    /**
     * 设置关联人员电话
     *
     * @param peoplePhone 关联人员电话
     */
    public void setPeoplePhone(String peoplePhone) {
        this.peoplePhone = peoplePhone;
    }

    /**
     * 获取关联人员身份证号码
     *
     * @return peopleId_card - 关联人员身份证号码
     */
    public String getPeopleidCard() {
        return peopleidCard;
    }

    /**
     * 设置关联人员身份证号码
     *
     * @param peopleidCard 关联人员身份证号码
     */
    public void setPeopleidCard(String peopleidCard) {
        this.peopleidCard = peopleidCard;
    }

    public String getEventUuid() {
        return eventUuid;
    }

    public void setEventUuid(String eventUuid) {
        this.eventUuid = eventUuid;
    }
}