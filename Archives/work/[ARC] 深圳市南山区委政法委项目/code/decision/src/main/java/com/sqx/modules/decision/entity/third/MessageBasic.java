package com.sqx.modules.decision.entity.third;

import io.swagger.annotations.*;
import lombok.*;

@Data
@ApiModel
public class MessageBasic {


    private String content;
    private String subject;
    private Integer source;
    private Integer type;
    private String link;
    private String mobileLink;
    private Integer priority;
    private String creator;
    private String receiver;
    private Integer receiverType;


}

