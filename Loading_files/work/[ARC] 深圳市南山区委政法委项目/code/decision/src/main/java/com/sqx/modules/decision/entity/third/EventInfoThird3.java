package com.sqx.modules.decision.entity.third;

import lombok.*;

import java.util.*;

@Data
public class EventInfoThird3 {

    private String id;
    private String eventCode;
    private String gridCode;
    private String lockStatus;
    private String belongDistrict;
    private String belongStreet;
    private String gridName;
    private String title;
    private String eventContent;
    private String address;
    private String eventTime;
    private String positionDetail;
    private String reportTime;
    private String eventX;
    private String hiddenDanger;
    private String eventY;
    private String reportLinkman;
    private String reportContact;
    private String entryItemId;
    private String entryItemName;
    private String nodePreviewEndTime;
    private String reportOrgName;

    private String eventSourceSr;

    List<EventFormBeforeAttrInfo1> eventFormBeforeAttrInfos;




}
