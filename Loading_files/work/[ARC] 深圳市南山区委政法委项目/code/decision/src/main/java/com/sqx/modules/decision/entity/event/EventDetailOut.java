package com.sqx.modules.decision.entity.event;

import com.sqx.modules.decision.dao.*;
import com.sqx.modules.decision.entity.*;
import com.sqx.modules.decision.entity.third.*;
import io.swagger.annotations.*;
import lombok.*;

import java.util.*;

@Data
@ApiModel
public class EventDetailOut {

    @ApiModelProperty(value = "事件信息")
    private EventInfo eventInfo ;

    @ApiModelProperty(value = "第三方事件信息")
    private EventInfoThird eventInfoThird;

    @ApiModelProperty(value = "第三方事件信息")
    private EventInfoThird2 eventInfoThird2;

    @ApiModelProperty(value = "第三方事件信息")
    private EventInfoThird3 eventInfoThird3;

    @ApiModelProperty(value = "事件市民信息")
    private CitizenInfo citizenInfo;

    @ApiModelProperty(value = "扩展信息")
    private EventExtendInfo eventExtendInfo;

    @ApiModelProperty(value = "满意度信息")
    private SatisfactionInfo satisfactionInfo;

    @ApiModelProperty(value = "批示反馈列表")
    private List<EventComment> eventCommentList;

    @ApiModelProperty(value = "处理流程")
    private List<ProcessFlow> processFlowList;

    @ApiModelProperty(value = "关联人员信息")
    private AssociatedPeopleInfo associatedPeopleInfo;

    @ApiModelProperty(value = "关联法人信息")
    private AssociatedLegalInfo associatedLegalInfo;

    @ApiModelProperty(value = "关联物联网")
    private AssociatedIotInfo associatedIotInfo;

    @ApiModelProperty(value = "关联房屋信息")
    private AssociatedTenementInfo associatedTenementInfo;

    @ApiModelProperty(value = "关联事件信息")
    private AssociatedEventInfo associatedEventInfo;

}
