package com.sqx.modules.decision.entity.event;

import io.swagger.annotations.*;
import lombok.*;

@Data
@ApiModel
public class RoleData {

    private String precinctCode;

    private String precinctName;

    private String roleName;

    private String roleCode;

}
