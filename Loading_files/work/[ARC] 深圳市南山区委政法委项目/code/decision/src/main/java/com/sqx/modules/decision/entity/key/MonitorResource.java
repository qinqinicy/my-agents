package com.sqx.modules.decision.entity.key;

import lombok.*;

@Data
public class MonitorResource {

    private Integer status;
    private String message;

    private DataDetail data;
}
