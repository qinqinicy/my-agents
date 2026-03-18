package com.sqx.modules.decision.entity.event;

import io.swagger.annotations.*;
import lombok.*;

import java.util.*;

@Data
public class UserNum {

    private String gridCode;
    private String parentCode;
    private String gridName;
    private String gridLevel;
    @ApiModelProperty(value = "实有人口数")
    private String syrks;
    @ApiModelProperty(value = "户籍人口数")
    private String hjrks;
    @ApiModelProperty(value = "流动人口数")
    private String ldrks;
    @ApiModelProperty(value = "常住人口数")
    private String czrks;
    @ApiModelProperty(value = "人口核采集量")
    private String rkhcjl;
    @ApiModelProperty(value = "人口变化量")
    private String rkbhl;


}
