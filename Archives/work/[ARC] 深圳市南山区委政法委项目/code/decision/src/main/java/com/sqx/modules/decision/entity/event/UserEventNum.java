package com.sqx.modules.decision.entity.event;

import lombok.*;

@Data
public class UserEventNum {
    private String userId;
    private Integer read;
    private Integer eventNum;
}
