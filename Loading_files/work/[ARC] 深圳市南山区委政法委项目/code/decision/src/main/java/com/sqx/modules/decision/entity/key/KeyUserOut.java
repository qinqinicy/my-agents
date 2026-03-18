package com.sqx.modules.decision.entity.key;

import io.swagger.annotations.*;
import lombok.*;

import java.util.*;

@Data
@ApiModel
public class KeyUserOut {

    @ApiModelProperty(value = "重点总人数")
    private String zdzrs;

    @ApiModelProperty(value = "本月新增重点关注人员数量")
    private String byxzzdgzrysl;

    @ApiModelProperty(value = "重点人本月增减量")
    private String zdrbyzjl;

    @ApiModelProperty(value = "本月新增重点关注人员暂未纳管数")
    private String byxzzdgzryzwngs;

    @ApiModelProperty(value = "本月新增重点关注人员暂未走访数")
    private String byxzzdgzryzwzfs;

    @ApiModelProperty(value = "全部重点关注人员")
    private String qbzdgzry;

    @ApiModelProperty(value = "全部重点关注人员暂未纳管数")
    private String qbzdgzryzwngs;

    @ApiModelProperty(value = "全部重点关注人员暂未走访数")
    private String qbzdgzryzwzfs;

    @ApiModelProperty(value = "月环比")
    private String yhb;

    @ApiModelProperty(value = "月同比")
    private String ytb;

    @ApiModelProperty(value = "纳管率")
    private String ngl;

    @ApiModelProperty(value = "本月重点人走访")
    private String byzfrs;

    @ApiModelProperty(value = "总涉事人数")
    private String zssrs;

    @ApiModelProperty(value = "总涉事人数")
    private String jrssrs;

}
