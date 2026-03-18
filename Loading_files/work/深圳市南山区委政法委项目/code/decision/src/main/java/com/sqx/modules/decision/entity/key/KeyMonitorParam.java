package com.sqx.modules.decision.entity.key;

import com.sqx.modules.decision.entity.event.*;
import io.swagger.annotations.*;
import lombok.*;

@Data
@ApiModel
public class KeyMonitorParam  extends UserInfoBase {

    @ApiModelProperty(value = "是否超标")
    private String isError ;

    @ApiModelProperty(value = "区域code")
    private String departmentCode;




}

