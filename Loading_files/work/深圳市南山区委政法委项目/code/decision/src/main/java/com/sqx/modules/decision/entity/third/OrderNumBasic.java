package com.sqx.modules.decision.entity.third;

import io.swagger.annotations.*;
import lombok.*;

@Data
@ApiModel
public class OrderNumBasic {

    private String appCode;
    private String region;
    private String catalog;
    private String cataId;
    private String eventCode;

}

