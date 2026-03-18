package com.sqx.modules.decision.entity.third;

import lombok.*;

@Data
public class AccessToken {

    private String access_token;
    private String refresh_token;
    private String token_type;
    private Integer expires_in;
}
