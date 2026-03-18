package com.sqx.modules.decision.entity.third;

import lombok.*;

@Data
public class DynamicEventParam {

    private String keyword="";
    private String normalTypeCode="";
    private String streetCode="";
    private String communityCode="";
    private String gridCode="";
    private String orderStatusName="";
    private String reportTimeStart="";
    private String reportTimeEnd="";
    private String pageNo="";
    private String pageSize="";
    private String dataSource="";


}
