package com.sqx.modules.decision.entity.third;

import lombok.*;

@Data
public class ResponseCode {

    private Integer status;

    private Integer code;

    private String message;

    private Boolean success;

    private String desc;



}
