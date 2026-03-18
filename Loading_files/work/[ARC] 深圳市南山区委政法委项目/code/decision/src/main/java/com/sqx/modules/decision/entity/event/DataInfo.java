package com.sqx.modules.decision.entity.event;

import io.swagger.annotations.*;
import lombok.*;

@Data
@ApiModel
public class DataInfo {

    private String userId;
    private String roleId;
    private String roleCode;
    private String roleName;
    private String precinctCode;
    private String precinctName;

}
