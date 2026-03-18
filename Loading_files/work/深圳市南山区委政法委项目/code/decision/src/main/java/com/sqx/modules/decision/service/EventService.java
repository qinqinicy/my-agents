package com.sqx.modules.decision.service;

import com.sqx.modules.decision.entity.event.*;
import com.sqx.modules.decision.entity.third.*;

import javax.servlet.http.*;
import java.util.*;

public interface EventService {


    EventListOut eventAuditList(EventListParam paramVo);

    void eventAuditHandle(EventAuditParam paramVo);

    EventDetailOut eventDetail(EventDetailParam paramVo);

    EventListOut todayEventAttention(EventListParam paramVo);

    void attentionOrCancle(EventAttentionParam paramVo);

    void eventComment(EventCommentParam paramVo);

    EventCommentList eventCommentList(EventCommentParam paramVo);

    EventListOut myAttentionList(UserInfo userInfo);

    //审核定时推送
    void pushAuditSchedule();

    EventListOut myCommentList(EventCommentParam paramVo);

    AttentionSum attentionSum(EventCommentParam paramVo);
    UserInfo getUserInfo(String token, HttpServletResponse response);


    List<Department> getAllDepartment(UserInfo userInfo);

    UserNumCal getUserNum(UserCalParam paramVo);

    DistrictOut getDistrict(UserCalParam paramVo);

    ReceiveDpt receiveDpt(UserInfo userInfo1);

    EventListOut leaderCommentList(EventCommentParam paramVo);

    void pushEvent(EventBasic paramVo);

    HaveNewEventOut hasNewEvent(UserInfo userInfo1);

    void pushProcess(EventProcessBasic eventProcessBasic);

    TokenResponse getToken(String clientId);

    void pushOrderEvent(OrderEventBasic eventBasic);

    void pushOrderProcess(OrderEventProcessBasic eventProcessBasic);

    EventDetailOut eventDetailFromThird(String userToken,EventDetailParam paramVo);

    EventDynamicOut dynamicEventList(DynamicEventParam paramVo);

    void saveDynamicEventComment(EventCommentParam1 paramVo);

    void saveDynamicAttentionOrCancle(EventAttentionParam1 paramVo);

    EventDynamicCommentOut commentListAttention(DynamicEvent paramVo);

    DynamicEventTotal getEventTotal(DynamicEventTotalParam paramVo);

    void pushMessage(OrderMessageBasic eventBasic);
}
