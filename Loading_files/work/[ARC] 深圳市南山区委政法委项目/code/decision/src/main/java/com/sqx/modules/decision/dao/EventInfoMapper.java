package com.sqx.modules.decision.dao;

import com.baomidou.mybatisplus.core.mapper.*;
import com.sqx.modules.decision.entity.*;
import com.sqx.modules.decision.entity.event.*;
import com.sqx.modules.decision.entity.key.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Mapper;

import javax.print.*;
import java.util.*;

@Mapper
public interface EventInfoMapper extends BaseMapper<EventInfo> {
    List<Event> selectEventList(EventListParam eventListParam);
    List<Event> myAttEvent(@Param("userId") String userId);
    EventInfo selectByOrderNum(@Param("orderNum") String orderNum);
    List<Event> selectComment(@Param("userId") String userId,
                              @Param("receiveCode")String receiveCode,
                              @Param("readPersonId") String readPersonId);
    List<Event> selectUnAuditId(@Param("beginDate") String userId,@Param("endDate")String endDate);


    List<Event> todayEventCal(EventCalParam eventListParam);
    List<Event> selectSelect();

    List<EventInfo> selectEventStreetIsNull();
}