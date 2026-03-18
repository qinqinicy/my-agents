package com.sqx.modules.decision.service;

import com.sqx.modules.decision.entity.event.*;
import com.sqx.modules.decision.entity.key.*;

public interface IndexService {


    EventListOut eventAuditList(EventListParam paramVo);

    KeyIndexOut key(KeyMonitorParam paramVo,UserInfo userInfo);

    EventCalOut eventCal(UserInfo userInfo1);

    EventClassCalOut eventClassNum(EventCalParam paramVo);

    EventListOut eventList(EventCalParam paramVo);

    EventSelectOut relatedField();

    KeyUserOut keyUser(UserInfo userInfo);

}
