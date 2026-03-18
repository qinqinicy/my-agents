package com.sqx.modules.decision.entity;

import io.swagger.annotations.*;

import java.util.Date;
import javax.persistence.*;

@Table(name = "associated_tenement_info")
public class AssociatedTenementInfo {
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
     * 关联房屋名称
     */
    @ApiModelProperty(value = "关联房屋名称")
    private String name;

    /**
     * 关联房屋编码
     */
    @ApiModelProperty(value = "关联房屋编码")
    private String code;

    /**
     * 关联房屋所属区域
     */
    @ApiModelProperty(value = "关联房屋所属区域")
    private String region;

    /**
     * 关联房屋所属区域code
     */
    @ApiModelProperty(value = "关联房屋所属区域code")
    @Column(name = "region_code")
    private String regionCode;

    /**
     * 关联房屋地址
     */
    @ApiModelProperty(value = "关联房屋地址")
    private String address;

    /**
     * 关联房屋楼栋长
     */
    @ApiModelProperty(value = "关联房屋楼栋长")
    private String chief;

    /**
     * 关联房屋楼栋长号码
     */
    @ApiModelProperty(value = "关联房屋楼栋长号码")
    @Column(name = "chief_phone")
    private String chiefPhone;

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
     * 获取关联房屋名称
     *
     * @return name - 关联房屋名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置关联房屋名称
     *
     * @param name 关联房屋名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取关联房屋编码
     *
     * @return code - 关联房屋编码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置关联房屋编码
     *
     * @param code 关联房屋编码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取关联房屋所属区域
     *
     * @return region - 关联房屋所属区域
     */
    public String getRegion() {
        return region;
    }

    /**
     * 设置关联房屋所属区域
     *
     * @param region 关联房屋所属区域
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * 获取关联房屋所属区域code
     *
     * @return region_code - 关联房屋所属区域code
     */
    public String getRegionCode() {
        return regionCode;
    }

    /**
     * 设置关联房屋所属区域code
     *
     * @param regionCode 关联房屋所属区域code
     */
    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    /**
     * 获取关联房屋地址
     *
     * @return address - 关联房屋地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置关联房屋地址
     *
     * @param address 关联房屋地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取关联房屋楼栋长
     *
     * @return chief - 关联房屋楼栋长
     */
    public String getChief() {
        return chief;
    }

    /**
     * 设置关联房屋楼栋长
     *
     * @param chief 关联房屋楼栋长
     */
    public void setChief(String chief) {
        this.chief = chief;
    }

    /**
     * 获取关联房屋楼栋长号码
     *
     * @return chief_phone - 关联房屋楼栋长号码
     */
    public String getChiefPhone() {
        return chiefPhone;
    }

    /**
     * 设置关联房屋楼栋长号码
     *
     * @param chiefPhone 关联房屋楼栋长号码
     */
    public void setChiefPhone(String chiefPhone) {
        this.chiefPhone = chiefPhone;
    }

    public String getEventUuid() {
        return eventUuid;
    }

    public void setEventUuid(String eventUuid) {
        this.eventUuid = eventUuid;
    }
}