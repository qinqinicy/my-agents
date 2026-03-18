package com.sqx.modules.decision.service.impl;

import cn.hutool.core.bean.*;
import com.alibaba.fastjson.*;
import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.extension.service.impl.*;
import com.fasterxml.jackson.databind.*;
import com.sqx.common.enume.*;
import com.sqx.common.exception.*;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.*;
import com.sqx.modules.decision.dao.*;
import com.sqx.modules.decision.entity.*;
import com.sqx.modules.decision.entity.event.*;
import com.sqx.modules.decision.entity.key.*;
import com.sqx.modules.decision.entity.third.*;
import com.sqx.modules.decision.service.*;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.*;
import org.apache.http.entity.*;
import org.apache.http.entity.mime.*;
import org.apache.http.impl.client.*;
import org.apache.http.util.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.io.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.*;

import javax.servlet.http.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

@Service
public class EventServiceImpl extends ServiceImpl<EventInfoMapper, EventInfo> implements EventService {
    private Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);
    //用户信息map
    HashMap<String, UserInfo> userInfoHashMap = new HashMap<>();

    //所有部门
    HashMap<String, List<Department>> departmentHashMap = new HashMap<>();

    //人口所有数据
    HashMap<String, List<UserNum>> allUserNums = new HashMap<>();

    //区域人口缓存
    HashMap<String, List<UserNum>> areaUserNum = new HashMap<>();


    HashMap<String, UserEventNum> eventNumMap = new HashMap<>();


    @Autowired
    private EventInfoMapper eventInfoMapper;

    @Autowired
    private EventExtendInfoMapper eventExtendInfoMapper;

    @Autowired
    private CitizenInfoMapper citizenInfoMapper;

    @Autowired
    private SatisfactionInfoMapper satisfactionInfoMapper;

    @Autowired
    private ProcessFlowMapper processFlowMapper;

    @Autowired
    private AssociatedIotInfoMapper associatedIotInfoMapper;

    @Autowired
    private AssociatedLegalInfoMapper associatedLegalInfoMapper;

    @Autowired
    private AssociatedPeopleInfoMapper associatedPeopleInfoMapper;

    @Autowired
    private AssociatedTenementInfoMapper associatedTenementInfoMapper;

    @Autowired
    private EventAttentionMapper eventAttentionMapper;

    @Autowired
    private EventCommentMapper eventCommentMapper;

    @Autowired
    private AttachmentInfoMapper attachmentInfoMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Value("${user.info.url}")
    private String userInfoUrl;

    @Value("${user.message.url}")
    private String sendMessageUrl;

    @Value("${user.message.url}")
    private String userMessageUrl;

    @Value("${key.userdata.url}")
    private String keyUserDataURL;

    @Value("${order.circle.url}")
    private String orderCircleURL;

    @Value("${order.create-order.url}")
    private String orderCreateURL;

    @Value("${order.order-num.url}")
    private String orderNumUrl;

    @Value("${event.access-token.url}")
    private String accessTokenUrl;

    @Value("${event.total.url}")
    private String eventTotalUrl;


    @Value("${event.dynamic.url}")
    private String dynamicEventUrl;

    @Value("${order.update.url}")
    private String orderUpdateURL;

    @Value("${order.recAppCode}")
    private String recAppCode;

    @Value("${order.sjfb}")
    private String sjfb;

    @Value("${order.wg}")
    private String wg;

    @Value("${order.zfpa}")
    private String zfpa;

    @Value("${event.detail.url}")
    private String eventDetailUrl;

    @Value("${event.detail.url1}")
    private String eventDetailUrl1;


    @Value("${event.detail.url2}")
    private String eventDetailUrl2;

    @Value("${decision.zfpa.id}")
    private String zfpaId;

    @Value("${decision.zfpa.username}")
    private String zfpaUserName;

    @Value("${decision.wg.id}")
    private String wgId;

    @Value("${decision.wg.username}")
    private String wgUserName;

    @Value("${decision.sjfb.id}")
    private String sjfbId;

    @Value("${decision.sjfb.username}")
    private String sjfbUserName;

    @Override
    public EventListOut eventAuditList(EventListParam paramVo) {
        UserInfo userInfo = paramVo.getUserInfo();
        paramVo.setIsAudit("1");
        //红点判断
        UserEventNum userEventNum = eventNumMap.get(userInfo.getUserId());
        if (userEventNum != null) {
            userEventNum.setRead(1);
            eventNumMap.put(userInfo.getUserId(), userEventNum);
        }
        //查询当前用户街道
        boolean isAudit = false;
        List<RoleData> roleDataList = userInfo.getRoleDataList();
        if (!CollectionUtils.isEmpty(roleDataList)) {
            for (RoleData roleData : roleDataList) {
                String roleName = roleData.getRoleName();
                if (roleName.contains("区") && roleName.contains("审核员")) {
                    paramVo.setStreetName("区");
                    isAudit = true;
                } else if (roleName.contains("街道") && roleName.contains("审核员")) {
                    /*String departmentName = userInfo.getDepartmentName();
                    String substring = departmentName.substring(0, departmentName.indexOf("街道") + 2);*/
                    paramVo.setStreetName(roleData.getPrecinctName());
                    isAudit = true;
                }
            }
        }
        EventListOut eventListOut = new EventListOut();
        if (!isAudit) {
            return eventListOut;
        }
        if (StringUtils.isNotBlank(paramVo.getEndDate())) {
            Date date = DateUtils.addDateDays(DateUtils.stringToDate(paramVo.getEndDate(), DateUtils.DATE_PATTERN), 1);
            paramVo.setEndDate(DateUtils.format(date, DateUtils.DATE_PATTERN));
        }
        List<Event> events = eventInfoMapper.selectEventList(paramVo);
        eventListOut.setEvents(events);
        return eventListOut;
    }

    @Override
    public void eventAuditHandle(EventAuditParam paramVo) {
        //判断是否已经审核
        EventInfo eventInfo2 = eventInfoMapper.selectById(paramVo.getId());
        if (eventInfo2.getAuditPushTime()!=null){
            throw new SqxException("已审核，不能重复审核！");
        }
        EventInfo eventInfo = new EventInfo();
        eventInfo.setId(paramVo.getId());
        if (StringUtils.isNotBlank(paramVo.getPushTime())) {
            eventInfo.setAuditPushTime(DateUtils.stringToDate(paramVo.getPushTime(), DateUtils.DATE_TIME_PATTERN));
            eventInfo.setExamineStatus("0");
            eventInfo.setStatus("0");
            eventInfo.setAuditUserId(paramVo.getUserInfo().getUserId());
            eventInfoMapper.updateById(eventInfo);
            //todo 定时任务推送事项审核数据
        } else { //审核立即推送/不通过
            eventInfo.setExamineStatus(paramVo.getExamineStatus());
            eventInfo.setStatus(paramVo.getExamineStatus());
            eventInfo.setAuditPushTime(new Date());
            eventInfo.setAuditUserId(paramVo.getUserInfo().getUserId());
            eventInfo.setAuditUserName(paramVo.getUserInfo().getUserName());
            eventInfoMapper.updateById(eventInfo);
            //todo 给事项发布人推送消息
            try {

            } catch (Exception e) {
                logger.error("审核给用户推送消息发送失败：{}", e);
                throw new SqxException("审核失败");
            }
            //流转中心推消息
            try {
                //往流转中心推数据 orderCircleURL
                EventInfo eventInfo1 = eventInfo2;
                QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper();
                queryWrapper.eq("order_num", eventInfo1.getOrderNum());
                queryWrapper.orderByDesc("id");
                List<OrderInfo> list = orderInfoMapper.selectList(queryWrapper);
                OrderParamBasic orderParamBasic = new OrderParamBasic();
                orderParamBasic.setOrderNum(eventInfo1.getOrderNum());
                orderParamBasic.setUserId(paramVo.getUserInfo().getUserId());
                orderParamBasic.setUserName(paramVo.getUserInfo().getUserName());
                orderParamBasic.setOrderState("5");
                orderParamBasic.setHandleApp(list.get(0).getRecAppCode());
                orderParamBasic.setProcessAction("海致审批推送消息！");
                EventProcessBasic eventProcessBasic = new EventProcessBasic();
                eventProcessBasic.setMatterUuid(eventInfo1.getMatterUuid());
                eventProcessBasic.setEventMID(eventInfo1.getEventMid());
                if (paramVo.getExamineStatus().equals("1")) {
                    eventProcessBasic.setStatus(1);
                } else {
                    eventProcessBasic.setStatus(2);
                }
                ObjectMapper objectMapper = new ObjectMapper();
                String s = objectMapper.writeValueAsString(eventProcessBasic);
                logger.info("审核推送数据成功:{}", s);
                orderParamBasic.setDataPackage(s);
                orderCirculation(orderParamBasic, GetToken.createJwt(), orderUpdateURL);
                String orderJson = objectMapper.writeValueAsString(eventProcessBasic);
                logger.info("审核数据状态消息发送成功！data:{}", orderJson);
            } catch (Exception e) {
                logger.error("审核数据状态消息发送失败：{}", e);
                throw new SqxException("审核失败");
            }
        }
    }

    private void orderCirculation(OrderParamBasic orderParamBasic, String token, String urls) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        // 创建URL对象
        URL url = new URL(urls);
        // 打开连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 设置请求方法为POST
        connection.setRequestMethod("POST");
        // 设置Content-Type为application/json
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", token);
        // 启用输出流
        connection.setDoOutput(true);
        // 创建JSON数据
        String jsonData = objectMapper.writeValueAsString(orderParamBasic);
        logger.info("工单流转数据：{}", jsonData);
        // 发送JSON数据
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        // 获取响应状态码
        int responseCode = connection.getResponseCode();
        logger.info("工单流转：Response Code: " + responseCode);

        try {
            if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) { // 500
                // 服务器内部错误，读取响应体中的错误信息
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
                String inputLine;
                StringBuffer errorResponse = new StringBuffer();

                while ((inputLine = errorReader.readLine()) != null) {
                    errorResponse.append(inputLine);
                }
                errorReader.close();
                // 解析错误信息
                String errorData = errorResponse.toString();
                logger.error("工单流转失败Error data: " + errorData);
                throw new SqxException("调用工单流转失败");
            } else if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                br.close();
                ResponseCode responseCode1 = JSONObject.parseObject(response.toString(), new TypeReference<ResponseCode>() {
                });
                if (responseCode1.getCode().equals(0)) {
                    logger.info("工单流转成功：Response: " + response.toString());
                } else {
                    throw new SqxException(response.toString());
                }
            } else {
                throw new SqxException("工单流转失败,code:{}", responseCode);
            }
        } catch (SqxException e) {
            logger.error("工单流转失败：", e);
            throw e;
//            throw new SqxException("需回复后再批示！");
        } catch (Exception e) {
            logger.error("工单流转失败：", e);
            throw e;
        } finally {
            // 关闭连接
            connection.disconnect();
        }
    }

    private void orderCreate(OrderCreateParamBasic orderParamBasic, String token, String urls) throws IOException {
        logger.info("请求地址：{}",urls);
        ObjectMapper objectMapper = new ObjectMapper();
        // 创建URL对象
        URL url = new URL(urls);
        // 打开连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 设置请求方法为POST
        connection.setRequestMethod("POST");
        // 设置Content-Type为application/json
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", token);
        // 启用输出流
        connection.setDoOutput(true);
        // 创建JSON数据
        String jsonData = objectMapper.writeValueAsString(orderParamBasic);
        logger.info("工单流转数据：{}", jsonData);
        // 发送JSON数据
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        // 获取响应状态码
        int responseCode = connection.getResponseCode();
        logger.info("工单流转：Response Code: " + responseCode);

        try {
            if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) { // 500
                // 服务器内部错误，读取响应体中的错误信息
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
                String inputLine;
                StringBuffer errorResponse = new StringBuffer();

                while ((inputLine = errorReader.readLine()) != null) {
                    errorResponse.append(inputLine);
                }
                errorReader.close();
                // 解析错误信息
                String errorData = errorResponse.toString();
                logger.error("工单流转失败Error data: " + errorData);
                throw new SqxException("调用工单流转失败");
            } else if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                br.close();
                ResponseCode responseCode1 = JSONObject.parseObject(response.toString(), new TypeReference<ResponseCode>() {
                });
                if (responseCode1.getCode().equals(0)) {
                    logger.info("工单流转成功：Response: " + response.toString());
                } else {
                    throw new SqxException(response.toString());
                }
            } else {
                // 服务器内部错误，读取响应体中的错误信息
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
                String inputLine;
                StringBuffer errorResponse = new StringBuffer();

                while ((inputLine = errorReader.readLine()) != null) {
                    errorResponse.append(inputLine);
                }
                errorReader.close();
                // 解析错误信息
                String errorData = errorResponse.toString();
                logger.error("工单调用失败Error data: " + errorData);
                throw new SqxException("工单调用失败,code:{}", responseCode);
            }
        } catch (SqxException e){
            throw new SqxException("回复后再批示");
        } catch (Exception e) {
            logger.error("工单流转失败：", e);
            throw e;
        } finally {
            // 关闭连接
            connection.disconnect();
        }
    }


    private void sendMessage(MessageBasic messageBasic, String token, String urls) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        // 创建URL对象
        URL url = new URL(urls);
        // 打开连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 设置请求方法为POST
        connection.setRequestMethod("POST");
        // 设置Content-Type为application/json
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", token);
        // 启用输出流
        connection.setDoOutput(true);
        // 创建JSON数据
        String jsonData = objectMapper.writeValueAsString(messageBasic);
        logger.info("发送政务微信数据：{}", jsonData);
        // 发送JSON数据
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        // 获取响应状态码
        int responseCode = connection.getResponseCode();
        logger.info("发送政务微信code：Response Code: " + responseCode);

        try {
            if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) { // 500
                // 服务器内部错误，读取响应体中的错误信息
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
                String inputLine;
                StringBuffer errorResponse = new StringBuffer();

                while ((inputLine = errorReader.readLine()) != null) {
                    errorResponse.append(inputLine);
                }
                errorReader.close();
                // 解析错误信息
                String errorData = errorResponse.toString();
                logger.error("发送政务微信失败Error data: " + errorData);
                throw new SqxException("发送政务微信失败");
            } else if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                br.close();
                ResponseCode responseCode1 = JSONObject.parseObject(response.toString(), new TypeReference<ResponseCode>() {
                });
                if (responseCode1.getCode().equals(0)) {
                    logger.info("发送政务微信成功：Response: " + response.toString());
                } else {
                    throw new SqxException(response.toString());
                }
            } else {
                throw new SqxException("发送政务微信失败,code:{}", responseCode);
            }
        } catch (Exception e) {
            logger.info("发送政务微信失败：", e);
            throw new SqxException("发送政务微信失败！");
        } finally {
            // 关闭连接
            connection.disconnect();
        }
    }


    @Override
    public EventDetailOut eventDetail(EventDetailParam paramVo) {
        EventInfo eventInfo = eventInfoMapper.selectById(paramVo.getId());
        eventInfo.setPushTime(eventInfo.getAuditPushTime());
        eventInfo.setEventNumber(eventInfo.getOrderNum());
        QueryWrapper<EventAttention> queryWrapperAtt = new QueryWrapper<>();
        queryWrapperAtt.eq("event_id", eventInfo.getId());
        EventAttention eventAttention = eventAttentionMapper.selectOne(queryWrapperAtt);
        if (eventAttention != null) {
            eventInfo.setAttentionStatus(eventAttention.getAttentionStatus());
        } else {
            eventInfo.setAttentionStatus("0");
        }

        QueryWrapper<EventExtendInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("event_uuid", eventInfo.getEventUuid());
        EventExtendInfo eventExtendInfo = eventExtendInfoMapper.selectOne(queryWrapper);


        QueryWrapper<CitizenInfo> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("citizen_uuid", eventInfo.getCitizenuuid());
        CitizenInfo citizenInfo = citizenInfoMapper.selectOne(queryWrapper1);

        QueryWrapper<SatisfactionInfo> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("event_uuid", eventInfo.getEventUuid());
        SatisfactionInfo satisfactionInfo = satisfactionInfoMapper.selectOne(queryWrapper2);

        QueryWrapper<ProcessFlow> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.eq("event_uuid", eventInfo.getEventUuid());
        queryWrapper3.orderByDesc("created_time");
        List<ProcessFlow> processFlows = processFlowMapper.selectList(queryWrapper3);

        QueryWrapper<AssociatedPeopleInfo> queryWrapper4 = new QueryWrapper<>();
        queryWrapper4.eq("event_uuid", eventInfo.getEventUuid());
        AssociatedPeopleInfo associatedPeopleInfo = associatedPeopleInfoMapper.selectOne(queryWrapper4);

        QueryWrapper<AssociatedTenementInfo> queryWrapper5 = new QueryWrapper<>();
        queryWrapper5.eq("event_uuid", eventInfo.getEventUuid());
        AssociatedTenementInfo associatedTenementInfo = associatedTenementInfoMapper.selectOne(queryWrapper5);

        QueryWrapper<AssociatedLegalInfo> queryWrapper6 = new QueryWrapper<>();
        queryWrapper6.eq("event_uuid", eventInfo.getEventUuid());
        AssociatedLegalInfo associatedLegalInfo = associatedLegalInfoMapper.selectOne(queryWrapper6);

        QueryWrapper<AssociatedIotInfo> queryWrapper7 = new QueryWrapper<>();
        queryWrapper7.eq("event_uuid", eventInfo.getEventUuid());
        AssociatedIotInfo associatedIotInfo = associatedIotInfoMapper.selectOne(queryWrapper7);


        QueryWrapper<EventComment> queryWrapper8 = new QueryWrapper<EventComment>();
        queryWrapper8.eq("event_uuid", eventInfo.getEventUuid());
        queryWrapper8.orderByDesc("created_time");
        List<EventComment> eventComments = eventCommentMapper.selectList(queryWrapper8);

        EventDetailOut eventDetailOut = new EventDetailOut();
        eventDetailOut.setEventInfo(eventInfo);
        eventDetailOut.setCitizenInfo(citizenInfo);
        eventDetailOut.setEventExtendInfo(eventExtendInfo);
        eventDetailOut.setSatisfactionInfo(satisfactionInfo);
        eventDetailOut.setProcessFlowList(processFlows);
        eventDetailOut.setAssociatedPeopleInfo(associatedPeopleInfo);
        eventDetailOut.setAssociatedTenementInfo(associatedTenementInfo);
        eventDetailOut.setAssociatedLegalInfo(associatedLegalInfo);
        eventDetailOut.setAssociatedIotInfo(associatedIotInfo);
        eventDetailOut.setEventCommentList(eventComments);

        return eventDetailOut;
    }

    @Override
    public EventListOut todayEventAttention(EventListParam paramVo) {
        //权限控制 如果是审核员 当前只能看到自己的审核数据
        String auditUserId = null;
        UserInfo userInfo = paramVo.getUserInfo();
        List<UserRole> roleList = userInfo.getRoleList();
        if (!CollectionUtils.isEmpty(roleList)) {
            for (UserRole userRole : roleList) {
                if (userRole.getRoleName().contains("审核员")) {
                    auditUserId = userInfo.getUserId();
                    break;
                }
            }
        }

        EventListOut eventListOut = new EventListOut();
        EventListParam eventListParam = new EventListParam();
        eventListParam.setAuditUserId(auditUserId);
        eventListParam.setUserId(paramVo.getUserInfo().getUserId());
        eventListParam.setArea(paramVo.getArea());
        eventListParam.setExamineStatus("1");
        Date date = new Date();
        if (StringUtils.isNotBlank(paramVo.getToday())) {
            String format = DateUtils.format(DateUtils.addDateDays(date, 1), DateUtils.DATE_PATTERN);
            eventListParam.setPushEndDate(format);
            eventListParam.setToday(paramVo.getToday());
            eventListParam.setPushBeginDate(DateUtils.format(date, DateUtils.DATE_PATTERN));
        }
        eventListParam.setName(paramVo.getName());
        eventListParam.setIncidentArea(paramVo.getIncidentArea());
        eventListParam.setHandleStatus(paramVo.getHandleStatus());
        eventListParam.setAppealSource(paramVo.getAppealSource());
        eventListParam.setBeginDate(paramVo.getBeginDate());
        if (StringUtils.isNotBlank(paramVo.getEndDate())) {
            Date end = DateUtils.addDateDays(DateUtils.stringToDate(paramVo.getEndDate(), DateUtils.DATE_PATTERN), 1);
            paramVo.setEndDate(DateUtils.format(end, DateUtils.DATE_PATTERN));
            eventListParam.setEndDate(paramVo.getEndDate());
        }
        List<Event> events = eventInfoMapper.selectEventList(eventListParam);
        eventListOut.setEvents(events);
        //近24小时
        Date start = DateUtils.addDateDays(date, -1);
        //默认进来没有today参数 没有时间参数 需要分两个list
        if (StringUtils.isBlank(paramVo.getToday()) && StringUtils.isBlank(paramVo.getBeginDate()) && StringUtils.isBlank(paramVo.getEndDate())) {
            List<Event> recent = new ArrayList<>();
            List<Event> events1 = new ArrayList<>();
            if (!CollectionUtils.isEmpty(events)) {
                for (Event event : events) {
                    Date auditPushTime = event.getAuditPushTime();
                    if (auditPushTime.compareTo(start) > 0 && auditPushTime.compareTo(date) < 0) {
                        recent.add(event);
                    } else {
                        events1.add(event);
                    }
                }
            }
//            eventListOut.setRecently(recent);
//            eventListOut.setEvents(events1);
            eventListOut.setEvents(events);
        }
        return eventListOut;
    }

    @Override
    public void attentionOrCancle(EventAttentionParam paramVo) {
        EventAttention eventAttention = new EventAttention();
        eventAttention.setEventId(paramVo.getEventId());
        eventAttention.setUserId(paramVo.getUserInfo().getUserId());
        eventAttention.setAttentionStatus(paramVo.getAttentionStatus());

        QueryWrapper<EventAttention> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("event_id", paramVo.getEventId()).eq("user_id", paramVo.getUserInfo().getUserId());
        EventAttention attention = eventAttentionMapper.selectOne(queryWrapper);
        if (attention == null) {
            eventAttention.setCreatedTime(new Date());
            eventAttentionMapper.insert(eventAttention);
        } else {
            eventAttentionMapper.update(eventAttention, queryWrapper);
        }
        try {

            //往流转中心推数据
            EventInfo eventInfo = eventInfoMapper.selectById(paramVo.getEventId());
            QueryWrapper<OrderInfo> orderInfoQueryWrapper = new QueryWrapper<>();
            orderInfoQueryWrapper.eq("order_num", eventInfo.getOrderNum());
            orderInfoQueryWrapper.orderByDesc("id");
            List<OrderInfo> orderInfos = orderInfoMapper.selectList(orderInfoQueryWrapper);

            OrderParamBasic orderParamBasic = new OrderParamBasic();
            orderParamBasic.setOrderNum(eventInfo.getOrderNum());
            orderParamBasic.setHandleApp(orderInfos.get(0).getRecAppCode());
            orderParamBasic.setOrderState("5");
            orderParamBasic.setProcessAction("海致领导关注推送");
            orderParamBasic.setUserId(paramVo.getUserInfo().getUserId());
            orderParamBasic.setUserName(paramVo.getUserInfo().getUserName());
            EventProcessBasic eventProcessBasic = new EventProcessBasic();
            eventProcessBasic.setMatterUuid(eventInfo.getMatterUuid());
            eventProcessBasic.setOrderNum(eventInfo.getOrderNum());
            eventProcessBasic.setEventMID(eventInfo.getEventMid());
            eventProcessBasic.setCreatedTime(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
            eventProcessBasic.setUserId(paramVo.getUserInfo().getUserId());
            eventProcessBasic.setUserName(paramVo.getUserInfo().getUserName());
            if (paramVo.getAttentionStatus().equals("1")) {
                eventProcessBasic.setReplyType(1);
            } else if (paramVo.getAttentionStatus().equals("0")) {
                eventProcessBasic.setReplyType(3);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(eventProcessBasic);
            orderParamBasic.setDataPackage(s);
            orderCirculation(orderParamBasic, GetToken.createJwt(), orderUpdateURL);

        } catch (Exception e) {
            logger.error("调用流转中心接口失败：{}", e);
            throw new SqxException("调用流转中心接口失败!");
        }


    }

    @Override
    @Transactional
    public void eventComment(EventCommentParam paramVo) {
        EventInfo eventInfo = eventInfoMapper.selectById(paramVo.getEventId());
        EventComment eventComment = new EventComment();
        eventComment.setEventUuid(eventInfo.getEventUuid());
        eventComment.setEventId(eventInfo.getId());
        eventComment.setUserId(paramVo.getUserInfo().getUserId());
        eventComment.setReceiveUnit(paramVo.getReceiveUnit());
        eventComment.setReceiveCode(paramVo.getReceiveCode());
        eventComment.setCreatedTime(new Date());
        eventComment.setEventId(paramVo.getEventId());
        eventComment.setComment(paramVo.getComment());
        eventComment.setUnitName(paramVo.getUserInfo().getDepartmentName());
        eventComment.setUnitCode(paramVo.getUserInfo().getDepartmentCode());
        eventComment.setUserName(paramVo.getUserInfo().getUserName());
        eventComment.setOrderNum(eventInfo.getOrderNum());
        eventCommentMapper.insert(eventComment);
        // todo 推送消息
        /*try {
            MessageBasic messageBasic = new MessageBasic();
            messageBasic.setSubject("领导完成批示");
            messageBasic.setSource(4);
            messageBasic.setType(1);
            messageBasic.setContent("您收到了一项批示，请前往决策分析首页领导批示中查看，点击查看");
            messageBasic.setCreator(paramVo.getUserInfo().getUserId());
            messageBasic.setReceiver(eventInfo.getUnitUserId());
            messageBasic.setMobileLink("https://rioweb.szns.gov.cn/pans/fxdp_online/nsjcfxpre/#/home");
            messageBasic.setReceiverType(0);
            sendMessage(messageBasic, paramVo.getUserInfo().getToken(), sendMessageUrl);
        } catch (SqxException e) {
            logger.error("发送消息失败", e);
            throw e;
        } catch (Exception e) {
            logger.error("调用发送消息失败", e);
        }*/
        //往流转中心推数据 orderCircleURL
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("order_num", eventInfo.getOrderNum());
            queryWrapper.orderByDesc("id");
            List<OrderInfo> list = orderInfoMapper.selectList(queryWrapper);
            OrderParamBasic orderParamBasic = new OrderParamBasic();
            orderParamBasic.setOrderNum(eventInfo.getOrderNum());
            orderParamBasic.setRecAppCode(recAppCode);
            if (!CollectionUtils.isEmpty(list)) {
                orderParamBasic.setNewRecAppCode(list.get(0).getSendAppCode());
            }
            orderParamBasic.setUserId(paramVo.getUserInfo().getUserId());
            orderParamBasic.setUserName(paramVo.getUserInfo().getUserName());
            EventProcessBasic eventProcessBasic = new EventProcessBasic();
            eventProcessBasic.setMatterUuid(eventInfo.getMatterUuid());
            eventProcessBasic.setOrderNum(eventInfo.getOrderNum());
            eventProcessBasic.setEventMID(eventInfo.getEventMid());
            eventProcessBasic.setCreatedTime(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
            eventProcessBasic.setUserId(paramVo.getUserInfo().getUserId());
            eventProcessBasic.setUserName(paramVo.getUserInfo().getUserName());
            if (paramVo.getCommentOrFeedBack() == 1) {
                eventProcessBasic.setStatus(3);
                eventProcessBasic.setDecisionComment(paramVo.getComment());
            } else {
                eventProcessBasic.setStatus(4);
                eventProcessBasic.setFeedbackComment(paramVo.getComment());
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(eventProcessBasic);
            orderParamBasic.setDataPackage(s);
            String s1 = objectMapper.writeValueAsString(orderParamBasic);
            logger.info(s1);
            orderCirculation(orderParamBasic, GetToken.createJwt(), orderCircleURL);
        } catch (SqxException e){
            logger.error("调用流转中心接口失败：{}", e);
            throw new SqxException("回复后再批示");
        } catch (Exception e) {
            logger.error("调用流转中心接口失败：{}", e);
            throw new SqxException("调用流转中心接口失败!");
        }
    }

    @Override
    public EventCommentList eventCommentList(EventCommentParam paramVo) {
        QueryWrapper<EventComment> queryWrapper = new QueryWrapper<EventComment>();
        queryWrapper.eq("event_id", paramVo.getEventId());
        queryWrapper.orderByDesc("id");
        List<EventComment> eventComments = eventCommentMapper.selectList(queryWrapper);
        EventCommentList eventCommentList = new EventCommentList();
        eventCommentList.setEventCommentList(eventComments);
        return eventCommentList;
    }

    @Override
    public EventListOut myAttentionList(UserInfo userInfo) {
        List<Event> events = eventInfoMapper.myAttEvent(userInfo.getUserId());
        EventListOut eventListOut = new EventListOut();
        eventListOut.setEvents(events);
        return eventListOut;
    }

    @Override
    @Scheduled(cron = "0 0/1 * * * ?")
    public void pushAuditSchedule() {
        Date date = new Date();
        String format = DateUtils.format(date, DateUtils.DATE_PATTERN);
        Date date1 = DateUtils.addDateDays(date, 1);
        String format1 = DateUtils.format(date1, DateUtils.DATE_PATTERN);
        List<Event> events = eventInfoMapper.selectUnAuditId(format, format1);
        Date after = DateUtils.addDateMinutes(date, 1);
        Date before = DateUtils.addDateMinutes(date, -2);
        if (!CollectionUtils.isEmpty(events)) {
            for (Event event : events) {
                if (event.getPushTime().before(after) && event.getPushTime().after(before)) {
                    EventInfo eventInfo = new EventInfo();
                    eventInfo.setId(event.getId());
                    eventInfo.setExamineStatus("1");
                    eventInfoMapper.updateById(eventInfo);
                    //流转中心推消息
                    try {
                        //往流转中心推数据 orderCircleURL
                        EventInfo eventInfo1 = eventInfoMapper.selectById(event.getId());
                        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper();
                        queryWrapper.eq("order_num", eventInfo1.getOrderNum());
                        queryWrapper.orderByDesc("id");
                        List<OrderInfo> list = orderInfoMapper.selectList(queryWrapper);
                        OrderParamBasic orderParamBasic = new OrderParamBasic();
                        orderParamBasic.setOrderNum(eventInfo1.getOrderNum());
                        orderParamBasic.setUserId(eventInfo1.getAuditUserId());
                        orderParamBasic.setUserName(eventInfo1.getAuditUserName());
                        orderParamBasic.setOrderState("5");
                        orderParamBasic.setHandleApp(recAppCode);
                        orderParamBasic.setProcessAction("海致审批推送消息！");
                        EventProcessBasic eventProcessBasic = new EventProcessBasic();
                        eventProcessBasic.setMatterUuid(eventInfo1.getMatterUuid());
                        eventProcessBasic.setEventMID(eventInfo1.getEventMid());
                        eventProcessBasic.setStatus(1);
                        ObjectMapper objectMapper = new ObjectMapper();
                        String s = objectMapper.writeValueAsString(eventProcessBasic);
                        logger.info("审核推送数据成功:{}", s);
                        orderParamBasic.setDataPackage(s);
                        orderCirculation(orderParamBasic, GetToken.createJwt(), orderUpdateURL);
                        String orderJson = objectMapper.writeValueAsString(eventProcessBasic);
                        logger.info("审核数据状态消息发送成功！data:{}", orderJson);
                    } catch (Exception e) {
                        logger.error("审核数据状态消息发送失败：{}", e);
                        throw new SqxException("审核失败");
                    }
                }
            }
        }
    }

    @Override
    public EventListOut myCommentList(EventCommentParam paramVo) {
        List<Event> events = eventInfoMapper.selectComment(paramVo.getUserInfo().getUserId(), null, null);
        EventListOut eventListOut = new EventListOut();
        eventListOut.setEvents(events);
        return eventListOut;
    }

    @Override
    public AttentionSum attentionSum(EventCommentParam paramVo) {
        List<Event> comment = eventInfoMapper.selectComment(paramVo.getUserInfo().getUserId(), null, null);
        List<Event> myAttEvent = eventInfoMapper.myAttEvent(paramVo.getUserInfo().getUserId());
        //查询领导批示
        //部门处理
        String departmentCode = paramVo.getUserInfo().getDepartmentCode();
        if (departmentCode.contains("-")) {
            String[] split = departmentCode.split("-");
            departmentCode = split[0] + split[1];
        } else {
            departmentCode = departmentCode.substring(0, departmentCode.length());
        }
        List<Event> leaderComment = eventInfoMapper.selectComment(null, null, paramVo.getUserInfo().getUserId());
        AttentionSum attentionSum = new AttentionSum();
        if (!CollectionUtils.isEmpty(myAttEvent)) {
            attentionSum.setMyAttentionNum(myAttEvent.size());
        } else {
            attentionSum.setMyAttentionNum(myAttEvent.size());
        }
        if (!CollectionUtils.isEmpty(comment)) {
            attentionSum.setMyCommentNum(comment.size());
        } else {
            attentionSum.setMyCommentNum(0);
        }
        if (!CollectionUtils.isEmpty(leaderComment)) {
            attentionSum.setLeaderNum(leaderComment.size());
        } else {
            attentionSum.setLeaderNum(0);
        }

        return attentionSum;
    }

    public UserInfo getUserInfo(String userToken, HttpServletResponse res) {
      /*  UserInfo userInfo3 = userInfo();
        return userInfo3;*/

        try {
            UserInfo userInfo = JwtToken.getUserInfo(userToken);
            //判断hashmap中是否有用户信息
            /*UserInfo userInfo1 = userInfoHashMap.get(userInfo.getUserId());
            if (null != userInfo1) {
                return userInfo1;
            }*/

            if (StringUtils.isBlank(userToken)) {
                throw new SqxException("token失效", 401);
            }
            CloseableHttpClient httpClient = HttpClients.createDefault();
            URIBuilder builder1 = new URIBuilder(userInfoUrl);
            builder1.addParameter("userId", userInfo.getUserId());
            URI uri1 = builder1.build();
            logger.info("url---" + uri1);
            HttpGet httpGet = new HttpGet(uri1);
            httpGet.setHeader("Authorization", userToken);
            logger.info("Authorization:{}", userToken);
            // 执行请求
            HttpResponse response = httpClient.execute(httpGet);
            String resultString = "";
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "utf-8");
                logger.info("调取成功：" + resultString, 200);
                JSONObject jsonObject = JSONObject.parseObject(resultString);
                Integer code = (Integer) jsonObject.get("code");
                if (code == 200) {
                    String user = jsonObject.get("data").toString();
                    UserInfo result = JSON.parseObject(user, new TypeReference<UserInfo>() {
                    });
//                    result.setToken(userInfo.getToken());
//                    userInfoHashMap.put(userInfo.getUserId(), result);
                    return result;
                } else {
                    logger.error("调取失败：" + resultString, response.getStatusLine().getStatusCode());
                    throw new SqxException("token失效", 401);
                }
            } else {
                res.addHeader("X-Log-Out", "token expire time");
                resultString = EntityUtils.toString(response.getEntity(), "utf-8");
                logger.error("调取失败：" + resultString, response.getStatusLine().getStatusCode());
            }

        } catch (Exception e) {
            res.addHeader("X-Log-Out", "token expire time");
            logger.error("调用失败", e);
            throw new SqxException("token失效", 401);
        }
        return null;
    }

    UserInfo getUserInfoById(String token, String userId) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            URIBuilder builder1 = new URIBuilder(userInfoUrl);
            builder1.addParameter("userId", userId);
            URI uri1 = builder1.build();
            logger.info("url---" + uri1);
            HttpGet httpGet = new HttpGet(uri1);
            httpGet.setHeader("Authorization", token);
            // 执行请求
            HttpResponse response = httpClient.execute(httpGet);
            String resultString = "";
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "utf-8");
                logger.info("调取成功：" + resultString, 200);
                JSONObject jsonObject = JSONObject.parseObject(resultString);
                Integer code = (Integer) jsonObject.get("code");
                if (code == 200) {
                    String user = jsonObject.get("data").toString();
                    UserInfo result = JSON.parseObject(user, new TypeReference<UserInfo>() {
                    });
                    return result;
                } else {
                    logger.error("调取失败：" + resultString, response.getStatusLine().getStatusCode());
                }
            } else {
                resultString = EntityUtils.toString(response.getEntity(), "utf-8");
                logger.error("调取失败：" + resultString, response.getStatusLine().getStatusCode());
            }

        } catch (Exception e) {
            logger.error("调用失败", e);
        }
        return null;
    }

    /**
     * 本地用户
     *
     * @return
     */
    UserInfo userInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId("9bb10f4841d148619160ed723346dd91");
        userInfo.setUserName("邓志阳");
        userInfo.setDepartmentCode("440305002");
        userInfo.setDepartmentName("南山街道");
        List<UserRole> roleList = new ArrayList<>();
        UserRole userRole = new UserRole();
        userRole.setUserId("9bb10f4841d148619160ed723346dd91");
        userRole.setRoleId("200");
        userRole.setRoleCode("icfxgshy");
        userRole.setRoleName("【决策分析】(区级审核员)");
        roleList.add(userRole);
        userInfo.setRoleList(roleList);
        return userInfo;
    }

    @Override
    public List<Department> getAllDepartment(UserInfo userInfo) {
        try {
            //从hashmap获取 如果有直接返回
            List<Department> departments = departmentHashMap.get("department");
            if (!CollectionUtils.isEmpty(departments)) {
                return departments;
            }
            //获取token
            String token = userInfo.getToken();
            CloseableHttpClient httpClient = HttpClients.createDefault();
            URIBuilder builder1 = new URIBuilder("http://ipaas-api.szns.gov.cn/32/department/getAllDepartments");
            URI uri1 = builder1.build();
            logger.info("url---" + uri1);
            HttpGet httpGet = new HttpGet(uri1);
            httpGet.setHeader("Authorization", token);
            // 执行请求
            HttpResponse response = httpClient.execute(httpGet);
            String resultString = "";
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "utf-8");
                logger.info("调取成功：" + resultString, 200);
                JSONObject jsonObject = JSONObject.parseObject(resultString);
                Integer code = (Integer) jsonObject.get("code");
                if (code == 200) {
                    String departs = jsonObject.get("data").toString();
                    List<Department> result = JSON.parseObject(departs, new TypeReference<List<Department>>() {
                    });
                    departmentHashMap.put("department", result);
                    return result;
                } else {
                    logger.error("调取失败：" + resultString, response.getStatusLine().getStatusCode());
                    return null;
                }
            } else {
                resultString = EntityUtils.toString(response.getEntity(), "utf-8");
                logger.error("调取失败：" + resultString, response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            logger.error("调用失败", e);
            throw new SqxException("调用失败");
        }
        return null;
    }

    @Override
    public UserNumCal getUserNum(UserCalParam paramVo) {
      /*try {
          UserNumCal userNumCal = new UserNumCal();
            ClassPathResource resource = new ClassPathResource("userData.json");
            byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String ss = new String(bytes);
            JSONObject jsonObject = JSONObject.parseObject(ss);
            Integer code = (Integer) jsonObject.get("status");
            if (code == 200) {
                String dataDetail = jsonObject.get("data").toString();
                DataDetail dataDetail1 = JSON.parseObject(dataDetail, new TypeReference<DataDetail>() {
                });
                List<List<String>> values = dataDetail1.getResult().getValues();
                if (!CollectionUtils.isEmpty(values)){
                    for (List<String> value : values) {
                        String syrks = value.get(4);
                        userNumCal.setTotal(syrks);
                        String rkhcjl = value.get(8);
                        userNumCal.setTodayCaiHe(rkhcjl);
                        String rkbhl = value.get(9);
                        userNumCal.setChange(rkbhl);
                        return userNumCal;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;*/
        return getUserNum(paramVo.getDepartmentCode());
    }


    UserNumCal getUserNum(String departmentCode) {
        UserNumCal userNumCal = new UserNumCal();
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            URIBuilder builder1 = new URIBuilder(keyUserDataURL);
            if (StringUtils.isNotBlank(departmentCode)) {
                builder1.addParameter("gridCode", departmentCode);
            }
            URI uri1 = builder1.build();
            logger.info("查询人口url---" + uri1);
            HttpGet httpGet = new HttpGet(uri1);
            // 执行请求
            HttpResponse response = httpClient.execute(httpGet);
            String resultString = "";
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "utf-8");
                logger.info("调取成功：" + resultString, 200);
                JSONObject jsonObject = JSONObject.parseObject(resultString);
                Integer code = (Integer) jsonObject.get("status");
                if (code == 200) {
                    String dataDetail = jsonObject.get("data").toString();
                    DataDetail dataDetail1 = JSON.parseObject(dataDetail, new TypeReference<DataDetail>() {
                    });
                    List<List<String>> values = dataDetail1.getResult().getValues();
                    if (!CollectionUtils.isEmpty(values)) {
                        for (List<String> value : values) {
                            String syrks = value.get(4);
                            userNumCal.setTotal(syrks);
                            String rkhcjl = value.get(8);
                            userNumCal.setTodayCaiHe(rkhcjl);
                            String rkbhl = value.get(9);
                            userNumCal.setChange(rkbhl);
                            return userNumCal;
                        }
                    } else {
                        logger.error("调取失败：" + resultString, response.getStatusLine().getStatusCode());
                        throw new SqxException("调用人口接口失败");
                    }
                } else {
                    resultString = EntityUtils.toString(response.getEntity(), "utf-8");
                    logger.error("调取失败：" + resultString, response.getStatusLine().getStatusCode());
                    throw new SqxException("调用人口接口失败");
                }
            }
        } catch (Exception e) {
            logger.error("调用人口统计接口失败", e);
        }
        return userNumCal;
    }

    @Override
    public DistrictOut getDistrict(UserCalParam paramVo) {
        DistrictOut districtOut = new DistrictOut();
        List<District> districtList = new ArrayList<>();
        List<Department> allDepartment = getAllDepartment(paramVo.getUserInfo());
        if (!CollectionUtils.isEmpty(allDepartment)) {
            for (Department department : allDepartment) {
                District district = new District();
                district.setDepartmentCode(department.getDepartmentCode());
                district.setDepartmentName(department.getDepartmentName());
                districtList.add(district);
            }
        }
        districtOut.setDistrictList(districtList);
        return districtOut;
    }

    @Override
    public ReceiveDpt receiveDpt(UserInfo userInfo1) {
        ReceiveDpt receiveDpt = new ReceiveDpt();
        List<District> districtList = new ArrayList<>();
        List<District> districtList1 = new ArrayList<>();
        /*List<Department> allDept = getAllDept();
        if (!CollectionUtils.isEmpty(allDept)) {
            for (Department department : allDept) {
                List<Department> children = department.getChildren();
                if (!CollectionUtils.isEmpty(children)) {
                    for (Department child : children) {
                        List<Department> children1 = child.getChildren();
                        if (!CollectionUtils.isEmpty(children1)) {
                            for (Department department1 : children1) {
                                if (department1.getDepartmentName().contains("街道")) {
                                    District district1 = new District();
                                    district1.setDepartmentCode(department1.getDepartmentCode());
                                    district1.setDepartmentName(department1.getDistrictName());
                                    district1.setDistrict(department1.getDistrict());
                                    district1.setDistrictName(department1.getDistrictName());
                                    districtList.add(district1);
                                }
                            }
                        }
                    }
                }
            }
        }*/
        List<RoleData> roleDataList = userInfo1.getRoleDataList();
        if (!CollectionUtils.isEmpty(roleDataList)) {
            for (RoleData dataInfo : roleDataList) {
                if (dataInfo.getRoleName().contains("决策分析")) {
                    District district = new District();
                    district.setDepartmentCode(dataInfo.getPrecinctCode());
                    district.setDepartmentName(dataInfo.getPrecinctName());
                    district.setDistrictName(dataInfo.getPrecinctName());
                    district.setDistrict(dataInfo.getPrecinctCode());
                    districtList.add(district);
                }
            }
            //去重
            if (!CollectionUtils.isEmpty(districtList)) {
                Set<String> set = new HashSet<>();
                for (District district : districtList) {
                    set.add(district.getDepartmentCode());
                }
                for (String s : set) {
                    for (District district : districtList) {
                        if (district.getDepartmentCode().equals(s)) {
                            districtList1.add(district);
                            break;
                        }
                    }
                }
            }
        }
        receiveDpt.setDistrictList(districtList1);
        return receiveDpt;
    }

    @Override
    public EventListOut leaderCommentList(EventCommentParam paramVo) {
        String departmentCode = paramVo.getUserInfo().getDepartmentCode();
        //权限控制
        //部门处理
        if (departmentCode.contains("-")) {
            String[] split = departmentCode.split("-");
            departmentCode = split[0] + split[1];
        } else {
            departmentCode = departmentCode.substring(0, departmentCode.length());
        }
        //查询领导批示
        List<Event> events = eventInfoMapper.selectComment(null, null, paramVo.getUserInfo().getUserId());
        EventListOut eventListOut = new EventListOut();
        eventListOut.setEvents(events);
        return eventListOut;
    }

    @Override
    public void pushEvent(EventBasic eventBasic) {
        EventInfo eventInfo = new EventInfo();
        eventInfo.setEventUuid(eventBasic.getMatterUuid());
        eventInfo.setName(eventBasic.getName());
        eventInfo.setTakePlace(eventBasic.getTakePlace());
        if (StringUtils.isNotBlank(eventBasic.getAppearTime())) {
            eventInfo.setAppearTime(DateUtils.stringToDate(eventBasic.getAppearTime(), DateUtils.DATE_TIME_PATTERN));
        }
        eventInfo.setEventSummarize(eventBasic.getMatterSummarize());
        eventInfo.setMatterSource(eventBasic.getMatterSource());
        eventInfo.setEventMid(eventBasic.getEventMID());
        eventInfo.setEventFid(eventBasic.getEventFID());
        /*List<Attachment> attachments = eventBasic.getAttachment();
        if (!CollectionUtils.isEmpty(attachments)) {
            String collect = attachments.stream().map(s -> s.getAttachmentData().getFilePath()).collect(Collectors.joining(","));
            eventInfo.setAttachment(collect);
        }*/
        if (eventBasic.getMatterType() != null) {
            eventInfo.setMatterType(eventBasic.getMatterType() + "");
        }
        if (eventBasic.getStatus() != null) {
            eventInfo.setStatus(eventBasic.getStatus() + "");
        }
        eventInfo.setUnitSource(eventBasic.getUnitSource());
        eventInfo.setDecisionPersonId(eventBasic.getDecisionPersonID());
        eventInfo.setDecisionPersonName(eventBasic.getDecisionPersonName());
        eventInfo.setReadPersonId(eventBasic.getReadPersonID());
        eventInfo.setReadPersonName(eventBasic.getReadPersonName());
        eventInfoMapper.insert(eventInfo);
    }

    @Override
    public HaveNewEventOut hasNewEvent(UserInfo userInfo1) {
        EventListParam paramVo = new EventListParam();
        boolean audit = false;
        //部门处理
        /*String precinctCode = null;
        String communityCode = null;
        String departmentCode = userInfo1.getDepartmentCode();
        if (departmentCode.contains("-")) {
            String[] split = departmentCode.split("-");
            precinctCode = split[0];
            communityCode = split[0] + split[1];
        } else {
            precinctCode = departmentCode.substring(0, 6);
            communityCode = departmentCode.substring(0, departmentCode.length());
        }
        List<UserRole> roleList = userInfo1.getRoleList();
        Integer area = null;//1=区审核员  2=街道审核员
        if (!CollectionUtils.isEmpty(roleList)) {
            for (UserRole userRole : roleList) {
                if (userRole.getRoleName().contains("区级审核员")) {
                    area = 1;
                    paramVo.setArea(area);
                    paramVo.setDepartmentCode(precinctCode);
                    audit = true;
                    break;
                } else if (userRole.getRoleName().contains("街道级审核员")) {
                    area = 2;
                    paramVo.setArea(area);
                    paramVo.setDepartmentCode(communityCode);
                    audit = true;
                    break;
                }
            }
        }*/
        List<RoleData> roleDataList = userInfo1.getRoleDataList();
        if (!CollectionUtils.isEmpty(roleDataList)) {
            for (RoleData dataInfo : roleDataList) {
                if (dataInfo.getRoleName().contains("区") && dataInfo.getRoleName().contains("审核员")) {
                    audit = true;
                    paramVo.setStreetName(dataInfo.getPrecinctName());
                    break;
                } else if (dataInfo.getRoleName().contains("街道") && dataInfo.getRoleName().contains("审核员")) {
                    audit = true;
                    paramVo.setStreetName(dataInfo.getPrecinctName());
                }
            }
        }
        HaveNewEventOut haveNewEventOut = new HaveNewEventOut();
        if (!audit) {
            haveNewEventOut.setHaveNewEvent(false);
            return haveNewEventOut;
        }
        Date date = DateUtils.addDateDays(new Date(), 1);
        paramVo.setCreateEndDate(DateUtils.format(date, DateUtils.DATE_PATTERN));
        paramVo.setCreateDate(DateUtils.format(new Date(), DateUtils.DATE_PATTERN));
        List<Event> events = eventInfoMapper.selectEventList(paramVo);
        UserEventNum userEventNum = eventNumMap.get(userInfo1.getUserId());
        if (userEventNum != null) {
            Integer read = userEventNum.getRead();
            Integer old = userEventNum.getEventNum();
            //没有点击 一直是红色
            if (!CollectionUtils.isEmpty(events)) {
                if (old != null && events.size() == old) {
                    if (read == 0) {
                        haveNewEventOut.setHaveNewEvent(true);
                    } else {
                        haveNewEventOut.setHaveNewEvent(false);
                    }
                } else {
                    haveNewEventOut.setHaveNewEvent(true);
                    userEventNum.setEventNum(events.size());
                    userEventNum.setRead(0);
                    eventNumMap.put(userInfo1.getUserId(), userEventNum);
                }
            } else {
                haveNewEventOut.setHaveNewEvent(false);
            }
        } else {
            userEventNum = new UserEventNum();
            if (!CollectionUtils.isEmpty(events)) {
                haveNewEventOut.setHaveNewEvent(true);
                userEventNum.setEventNum(events.size());
            } else {
                haveNewEventOut.setHaveNewEvent(false);
                userEventNum.setEventNum(0);
            }
            userEventNum.setRead(0);
            eventNumMap.put(userInfo1.getUserId(), userEventNum);
        }
//        haveNewEventOut.setHaveNewEvent(true);
        return haveNewEventOut;
    }

    @Override
    public void pushProcess(EventProcessBasic eventProcessBasic) {
        EventComment eventComment = new EventComment();
        eventComment.setComment(eventProcessBasic.getDecisionComment());
        eventComment.setComment(eventProcessBasic.getFeedbackComment());
        eventComment.setEventUuid(eventProcessBasic.getEventMID());
        eventComment.setEventUuid(eventProcessBasic.getMatterUuid());
        eventComment.setMatterNumber(eventProcessBasic.getMatterNumber());
        eventComment.setUserId(eventProcessBasic.getUserId());
        eventComment.setUserName(eventProcessBasic.getUserName());
        eventComment.setUnitCode(eventProcessBasic.getUnitCode());
        eventComment.setUnitName(eventProcessBasic.getUnitName());
        if (StringUtils.isNotBlank(eventProcessBasic.getCreatedTime())) {
            eventComment.setCreatedTime(DateUtils.stringToDate(eventProcessBasic.getCreatedTime(), DateUtils.DATE_TIME_PATTERN));
        }
        eventCommentMapper.insert(eventComment);
    }

    @Override
    public TokenResponse getToken(String clientId) {
        TokenResponse token = TokenUtil.getToken();
        return token;
    }

    @Transactional
    @Override
    public void pushOrderEvent(OrderEventBasic orderEventBasic) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String param = objectMapper.writeValueAsString(orderEventBasic);
            logger.info("流转平台参数：{}", param);
            OrderInfo orderInfo = new OrderInfo();
            BeanUtil.copyProperties(orderEventBasic, orderInfo);
            orderInfo.setOrderJson(param);
            orderInfo.setCreateTime(new Date());
            /*orderInfo.setEventUuid(orderEventBasic.getCataId());
            orderInfo.setOrderNum(orderEventBasic.getOrderNum());
            orderInfo.setMatterUuid(orderEventBasic.getOrderNum());
            orderInfo.setDataPackage(orderEventBasic.getDataPackage());
            orderInfo.setSendTime(orderEventBasic.getSendTime());
            orderInfo.setSenderId(orderEventBasic.getSenderId());
            orderInfo.setRecAppCode(orderEventBasic.getRecAppCode());
            orderInfo.setSendAppCode(orderEventBasic.getSendAppCode());
            orderInfo.setEventCode(orderEventBasic.getEventCode());
            orderInfo.setOrderState(orderEventBasic.getOrderState());
            orderInfo.setCataId(orderEventBasic.getCataId());
            orderInfo.setCatalog(orderEventBasic.getCatalog());
            orderInfo.setApplyTime(orderEventBasic.getApplyTime());
            orderInfo.setUserType(orderEventBasic.getUserType());
            orderInfo.setUserId(orderEventBasic.getUserId());
            orderInfo.setName(orderEventBasic.getName());
            orderInfo.setContactNo(orderEventBasic.getContactNo());
            orderInfo.setEventContent(orderEventBasic.getEventContent());
            orderInfo.setAddressCode(orderEventBasic.getAddressCode());*/

            //处理dataPackge内容
            EventBasic eventBasic;
            if (orderEventBasic != null) {
                String dataPackage = orderEventBasic.getDataPackage();
                if (StringUtils.isNotBlank(dataPackage)) {
                    eventBasic = JSON.parseObject(dataPackage, new TypeReference<EventBasic>() {
                    });
                } else {
                    throw new SqxException("数据包为空！");
                }
            } else {
                throw new SqxException("参数为空！");
            }
            orderInfo.setEventUuid(eventBasic.getEventMID());
            orderInfo.setMatterUuid(eventBasic.getMatterUuid());
            orderInfoMapper.insert(orderInfo);
            //新增event_info数据
            if (StringUtils.isBlank(eventBasic.getDecisionComment()) && StringUtils.isBlank(eventBasic.getFeedbackComment())) {
                List<Attachment> attachments = eventBasic.getAttachment();
                EventInfo eventInfo = new EventInfo();
                eventInfo.setDataSource("1");
                eventInfo.setIsAudit(true);
                eventInfo.setSendTime(orderEventBasic.getSendTime());
                String streetName = eventBasic.getStreetName();
                String decisionPersonID = eventBasic.getDecisionPersonID();
                if (StringUtils.isNotBlank(decisionPersonID)) {
                    List<String> list = new ArrayList<>();
                    if (decisionPersonID.contains(";")) {
                        list = Arrays.asList(decisionPersonID.split(";"));
                    } else {
                        list.add(decisionPersonID);
                    }
                    if (!CollectionUtils.isEmpty(list)) {
                        for (String s : list) {
                            UserInfo userInfoById = getUserInfoById(GetToken.createJwt(), s);
                            List<RoleData> roleDataList = userInfoById.getRoleDataList();
                            if (!CollectionUtils.isEmpty(roleDataList)) {
                                for (RoleData roleData : roleDataList) {
                                    if (roleData.getRoleName().contains("决策分析") && roleData.getRoleName().contains("区") && roleData.getRoleName().contains("领导")) {
                                        eventInfo.setStreetName(roleData.getPrecinctName());
                                        break;
                                    } else if (roleData.getRoleName().contains("决策分析") && roleData.getRoleName().contains("街道") && roleData.getRoleName().contains("领导")) {
                                        eventInfo.setStreetName(roleData.getPrecinctName());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                eventInfo.setStreet(streetName);
                eventInfo.setUnitUserId(orderEventBasic.getUserId());
                eventInfo.setUnitUsername(orderEventBasic.getName());
                eventInfo.setUnitSource(eventBasic.getUserDpt());
                eventInfo.setSystemid(orderInfo.getSendAppCode());
                eventInfo.setCreatedTime(new Date());
                eventInfo.setEventContent(orderEventBasic.getEventContent());
                if (StringUtils.isNotBlank(eventBasic.getEventMID())) {
                    eventInfo.setEventUuid(eventBasic.getEventMID());
                } else {
                    eventInfo.setEventUuid(eventBasic.getMatterUuid());
                }
                eventInfo.setName(eventBasic.getName());
                eventInfo.setOrderNum(orderEventBasic.getOrderNum());
                eventInfo.setTakePlace(eventBasic.getTakePlace());
                if (StringUtils.isNotBlank(eventBasic.getAppearTime())) {
                    eventInfo.setAppearTime(DateUtils.stringToDate(eventBasic.getAppearTime(), DateUtils.DATE_TIME_PATTERN));
                }
                eventInfo.setEventSummarize(eventBasic.getMatterSummarize());
                eventInfo.setMatterSource(eventBasic.getMatterSource());
                eventInfo.setEventMid(eventBasic.getEventMID());
                eventInfo.setEventFid(eventBasic.getEventFID());
                eventInfo.setMatterUuid(eventBasic.getMatterUuid());
                if (!CollectionUtils.isEmpty(attachments)) {
                    String collect = attachments.stream().map(s -> s.getAttachmentData().getFilePath()).collect(Collectors.joining(","));
                    eventInfo.setAttachment(collect);
                    for (Attachment attachment : attachments) {
                        AttachmentInfo attachmentInfo = new AttachmentInfo();
                        attachmentInfo.setAttachmentId(attachment.getAttachmentId());
                        attachmentInfo.setMatterUuid(eventBasic.getMatterUuid());
                        attachmentInfo.setOrderNum(orderEventBasic.getOrderNum());
                        attachmentInfo.setEventUuid(eventBasic.getEventMID());
                        //转义特殊字符
                        String fileName = attachment.getAttachmentData().getFileName();
                        attachmentInfo.setFileName(fileName);
                        attachmentInfo.setFileSuffix(attachment.getAttachmentData().getFileSuffix());
                        attachmentInfo.setFilePath(attachment.getAttachmentData().getFilePath());
                        attachmentInfo.setCreateTime(new Date());
                        attachmentInfoMapper.insert(attachmentInfo);
                        logger.info("附件添加成功！");
                    }
                }
                if (eventBasic.getMatterType() != null) {
                    eventInfo.setMatterType(eventBasic.getMatterType() + "");
                }
                if (eventBasic.getStatus() != null) {
                    eventInfo.setStatus(eventBasic.getStatus() + "");
                }
                if (StringUtils.isNotBlank(eventBasic.getUnitSource())) {
                    eventInfo.setUnitSource(eventBasic.getUnitSource());
                } else if (StringUtils.isNotBlank(eventBasic.getUserDpt())) {
                    eventInfo.setUnitSource(eventBasic.getUserDpt());
                }
                eventInfo.setDecisionPersonId(decisionPersonID);
                eventInfo.setDecisionPersonName(eventBasic.getDecisionPersonName());
                eventInfo.setReadPersonId(eventBasic.getReadPersonID());
                eventInfo.setReadPersonName(eventBasic.getReadPersonName());
                String sendAppCode = orderEventBasic.getSendAppCode();
                eventInfo.setSystemSource(SystemCodeEnum.getName(sendAppCode));
                eventInfoMapper.insert(eventInfo);
                logger.info("流转中心数据保存成功！");
            } else {
                EventInfo eventInfo = eventInfoMapper.selectByOrderNum(orderEventBasic.getOrderNum());
                //批示反馈相关
                EventComment eventComment = new EventComment();
                eventComment.setOrderNum(orderEventBasic.getOrderNum());
                eventComment.setEventUuid(eventInfo.getEventUuid());
                eventComment.setMatterNumber(eventBasic.getMatterNumber());
                eventComment.setUserId(eventBasic.getUserId());
                eventComment.setUserName(eventBasic.getUserName());
                eventComment.setFeedbackCommentType(eventBasic.getFeedbackCommentType());
                eventComment.setDecisionCommentType(eventComment.getDecisionCommentType());
                if (eventBasic.getStatus().equals(3) && StringUtils.isNotBlank(eventBasic.getDecisionComment())) {
                    eventComment.setComment(eventBasic.getDecisionComment());
                    logger.info("流转中心-批示");
                } else if (eventBasic.getStatus().equals(4) && StringUtils.isNotBlank(eventBasic.getFeedbackComment())) {
                    eventComment.setComment(eventBasic.getFeedbackComment());
                    logger.info("流转中心-反馈");
                }
                eventComment.setUnitCode(eventBasic.getUnitCode());
                eventComment.setUnitName(eventBasic.getUnitName());
                eventComment.setCreatedTime(new Date());
                eventCommentMapper.insert(eventComment);
                logger.info("流转中心--批示/反馈数据保存成功！");
            }
        } catch (Exception e) {
            log.error("流转平台调用推送事项接口失败", e);
            throw new SqxException("接口调用失败，请联系管理员！");
        }
    }

    @Override
    public void pushOrderProcess(OrderEventProcessBasic orderEventProcessBasic) {
        EventProcessBasic eventProcessBasic = null;
        if (orderEventProcessBasic != null) {
            String dataPackage = orderEventProcessBasic.getDataPackage();
            if (StringUtils.isNotBlank(dataPackage)) {
                eventProcessBasic = JSON.parseObject(dataPackage, new TypeReference<EventProcessBasic>() {
                });
            } else {
                throw new SqxException("数据包为空！");
            }
        } else {
            throw new SqxException("参数为空！");
        }
        //查询主事项ID
        EventComment eventComment = new EventComment();
        eventComment.setEventUuid(eventProcessBasic.getEventMID());
        eventComment.setMatterNumber(eventProcessBasic.getMatterNumber());
        eventComment.setUserId(eventProcessBasic.getUserId());
        eventComment.setUserName(eventProcessBasic.getUserName());
        eventComment.setFeedbackCommentType(eventProcessBasic.getFeedbackCommentType());
        eventComment.setDecisionCommentType(eventComment.getDecisionCommentType());
        if (eventProcessBasic.getStatus().equals("3") && StringUtils.isNotBlank(eventProcessBasic.getDecisionComment())) {
            eventComment.setComment(eventProcessBasic.getDecisionComment());
        } else if (eventProcessBasic.getStatus().equals("4") && StringUtils.isNotBlank(eventProcessBasic.getFeedbackComment())) {
            eventComment.setComment(eventProcessBasic.getFeedbackComment());
        }
        eventComment.setUnitCode(eventProcessBasic.getUnitCode());
        eventComment.setUnitName(eventProcessBasic.getUnitName());
        if (StringUtils.isNotBlank(eventProcessBasic.getCreatedTime())) {
            eventComment.setCreatedTime(DateUtils.stringToDate(eventProcessBasic.getCreatedTime(), DateUtils.DATE_TIME_PATTERN));
        }
        eventCommentMapper.insert(eventComment);
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderNum(orderEventProcessBasic.getOrderNum());
        orderInfo.setEventUuid(eventProcessBasic.getEventMID());
        orderInfo.setDataPackage(orderEventProcessBasic.getDataPackage());
        orderInfo.setSendTime(orderEventProcessBasic.getSendTime());
        orderInfo.setRecAppCode(orderEventProcessBasic.getRecAppCode());
        orderInfo.setSendAppCode(orderEventProcessBasic.getSendAppCode());
        orderInfoMapper.insert(orderInfo);
    }

    @Override
    public EventDetailOut eventDetailFromThird(String token, EventDetailParam paramVo) {
        //判断调用哪个url
        EventInfo eventInfo1 = eventInfoMapper.selectById(paramVo.getId());
        QueryWrapper orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("order_num", eventInfo1.getOrderNum());
        List<OrderInfo> orderInfos = orderInfoMapper.selectList(orderQueryWrapper);
        OrderInfo orderInfo = orderInfos.get(0);
        eventInfo1.setSendTime(orderInfo.getSendTime());
        eventInfo1.setSenderName(orderInfo.getName());
        eventInfo1.setSenderId(orderInfo.getSenderId());
        //查询关注状态
        QueryWrapper<EventAttention> queryWrapperAtt = new QueryWrapper<>();
        queryWrapperAtt.eq("event_id", eventInfo1.getId()).eq("user_id", paramVo.getUserInfo().getUserId());
        EventAttention eventAttention = eventAttentionMapper.selectOne(queryWrapperAtt);
        if (eventAttention != null) {
            eventInfo1.setAttentionStatus(eventAttention.getAttentionStatus());
        } else {
            eventInfo1.setAttentionStatus("0");
        }
        EventDetailOut eventDetailOut = new EventDetailOut();
        //查询批示 反馈详情
        QueryWrapper<EventComment> queryWrapper8 = new QueryWrapper<EventComment>();
        queryWrapper8.eq("order_num", eventInfo1.getOrderNum());
        queryWrapper8.orderByDesc("created_time");
        List<EventComment> eventComments = eventCommentMapper.selectList(queryWrapper8);
        eventDetailOut.setEventCommentList(eventComments);
        if (StringUtils.isNotBlank(eventInfo1.getDataSource()) && eventInfo1.getDataSource().equals("2")) {
            eventDetailOut.setEventInfo(eventInfo1);
            return eventDetailOut;
        }
        try {
            //查询附件
            QueryWrapper<AttachmentInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_num", eventInfo1.getOrderNum());
            List<AttachmentInfo> attachmentInfos = attachmentInfoMapper.selectList(queryWrapper);
            eventInfo1.setAttachmentInfos(attachmentInfos);
            String sendAppCode = eventInfo1.getSystemid();
            if (StringUtils.isBlank(eventInfo1.getEventUuid())) {
                eventDetailOut.setEventInfo(eventInfo1);
                return eventDetailOut;
            }
            if (sendAppCode.equals("PANS-WGH") && StringUtils.isNotBlank(eventInfo1.getEventMid())) {
                // 创建URL对象
                URL url = new URL(eventDetailUrl);
                // 打开连接
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // 设置请求方法为POST
                connection.setRequestMethod("POST");
                // 设置Content-Type为application/json
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", GetToken.createJwt());
                // 启用输出流
                connection.setDoOutput(true);
                // 创建JSON数据
                ObjectMapper objectMapper = new ObjectMapper();
                EventDetailThirdParam thirdParam = new EventDetailThirdParam();
                thirdParam.setId(eventInfo1.getEventUuid());
                String jsonData = objectMapper.writeValueAsString(thirdParam);
                // 发送JSON数据
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
                // 获取响应状态码
                int responseCode = connection.getResponseCode();
                logger.info("调用事项详情 Response Code: " + responseCode);
                // 读取响应数据
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    String jsonReuslt = response.toString();
                    logger.info("调用事项详情json: " + jsonReuslt);
                    EventInfoJson result = JSON.parseObject(jsonReuslt, new TypeReference<EventInfoJson>() {
                    });
                    if (result.getStatus().equals(0) && result.getSuccess()) {
                        EventInfoThird eventInfoThird = result.getResult();
                        if (eventInfoThird == null) {
                            eventDetailOut.setEventInfo(eventInfo1);
                            return eventDetailOut;
                        }
                        eventDetailOut.setEventInfoThird(eventInfoThird);
                        eventInfo1.setWorkOrderType(eventInfoThird.getEntryItemCode());
                        eventInfo1.setEventNumber(eventInfoThird.getOrderNum());
                        eventInfo1.setName(eventInfoThird.getEntryItemName());
                        eventInfo1.setTakeAddress(eventInfoThird.getEventAddr());
                        /*if (StringUtils.isNotBlank(eventInfoThird.getEventTime())) {
                            eventInfo1.setAppearTime(DateUtils.stringToDate(eventInfoThird.getEventTime(), DateUtils.DATE_TIME_PATTERN));
                        }*/
                        eventInfo1.setOwningGrid(eventInfoThird.getGridName());
                        eventInfo1.setEventContent(eventInfoThird.getEventRemark());
                        eventDetailOut.setEventInfo(eventInfo1);
                        return eventDetailOut;
                    } else {
                        logger.info("调用PANS-WGH第三方事件详情接口失败，结果：{}", result);
                    }
                } catch (Exception e) {
                    log.error("调用PANS-WGH第三方事件详情接口失败", e);
                } finally {
                    // 关闭连接
                    connection.disconnect();
                }
                return null;
            } else if (sendAppCode.equals("PANS-ZFPA") && StringUtils.isNotBlank(eventInfo1.getEventMid())) {
                Map<String, String> params = new HashMap<>();
                params.put("orderNo", eventInfo1.getEventUuid());
                // 构建 URL 字符串
                StringBuilder urlBuilder = new StringBuilder(eventDetailUrl1 + "?");
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
                String url = urlBuilder.toString();
                // 移除最后一个多余的 "&"
                url = url.substring(0, url.length() - 1);
                // 创建 URL 对象
                URL obj = new URL(url);
                // 打开连接
                HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
                // 设置请求方法为 GET
                connection.setRequestMethod("GET");
                // 发送请求
                int responseCode = connection.getResponseCode();
                System.out.println("Response Code: " + responseCode);
                // 如果响应码为 200（成功），则读取响应内容
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    // 将响应内容解析为 JSON 对象
                    String jsonReuslt = response.toString();
                    logger.info("调用事项详情json: " + jsonReuslt);
                    EventInfoJson result = JSON.parseObject(jsonReuslt, new TypeReference<EventInfoJson>() {
                    });
                    if (result.getCode().equals(200) && result.getMessage().equals("success")) {
                        EventInfoThird2 eventInfoThird = result.getData();
                        if (eventInfoThird == null) {
                            eventDetailOut.setEventInfo(eventInfo1);
                            return eventDetailOut;
                        }
                        eventDetailOut.setEventInfoThird2(eventInfoThird);
                        eventInfo1.setName(eventInfoThird.getName());
                        eventInfo1.setEventNumber(eventInfoThird.getEvent_no());
                        eventInfo1.setEventContent(eventInfoThird.getDescription());
                        eventInfo1.setTakeAddress(eventInfoThird.getStandard_address());
                        /*if (StringUtils.isNotBlank(eventInfoThird.getHappen_time())) {
                            eventInfo1.setAppearTime(DateUtils.stringToDate(eventInfoThird.getHappen_time(), DateUtils.DATE_TIME_PATTERN));
                        }*/
                        eventInfo1.setWorkOrderType(eventInfoThird.getEvent_type());
                        eventDetailOut.setEventInfo(eventInfo1);
                        return eventDetailOut;
                    } else {
                        logger.info("调用PANS-ZFPA第三方事件详情接口失败，结果：{}", result);
                    }
                } else {
                    logger.error("调用PANS-ZFPA第三方事件详情接口失败!");
                }
            } else if (sendAppCode.equals("PANS-SJFB") && StringUtils.isNotBlank(eventInfo1.getEventMid())) {
                try {
                    URL url = new URL(eventDetailUrl2 + "/" + eventInfo1.getEventUuid());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("token", GetToken.createJwt());
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) { // 成功响应
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        String jsonReuslt = response.toString();
                        logger.info("调用事件分拨PANS-SJFB事项详情json: " + jsonReuslt);
                        EventInfoJson1 result = JSON.parseObject(jsonReuslt, new TypeReference<EventInfoJson1>() {
                        });
                        if (result.getCode().equals(0)) {
                            EventInfoThird3 eventInfoThird = result.getData();
                            if (eventInfoThird == null) {
                                eventDetailOut.setEventInfo(eventInfo1);
                                return eventDetailOut;
                            }
                            eventDetailOut.setEventInfoThird3(eventInfoThird);
                            eventInfo1.setName(eventInfoThird.getTitle());
                            eventInfo1.setEventNumber(eventInfoThird.getEventCode());
                            eventInfo1.setEventContent(eventInfoThird.getEventContent());
                            eventInfo1.setTakeAddress(eventInfoThird.getAddress());
                            /*if (StringUtils.isNotBlank(eventInfoThird.getEventTime())) {
                                eventInfo1.setAppearTime(DateUtils.stringToDate(eventInfoThird.getEventTime(), DateUtils.DATE_TIME_PATTERN));
                            }*/
                            eventInfo1.setWorkOrderType(eventInfoThird.getEntryItemName());
                            eventInfo1.setName(eventInfoThird.getTitle());
                            eventDetailOut.setEventInfo(eventInfo1);
                            return eventDetailOut;
                        } else {
                            logger.info("调用第三方事件详情接口失败，结果：{}", result);
                        }
                        in.close();
                    } else {
                        throw new SqxException("调用事件分拨失败！ ");
                    }
                } catch (SqxException e) {
                    throw e;
                } catch (Exception e) {
                    logger.error("调用PANS-SJFB事件分拨接口失败{}", e);
                    throw new SqxException("调用时间分拨接口失败！");
                }
            }
            eventDetailOut.setEventInfo(eventInfo1);
            return eventDetailOut;
        } catch (IOException e) {
            log.error("请求失败{}", e);
            eventDetailOut.setEventInfo(eventInfo1);
            return eventDetailOut;
        }
    }

    @Override
    public EventDynamicOut dynamicEventList(DynamicEventParam paramVo) {
        if (StringUtils.isNotBlank(paramVo.getStreetCode())) {
            if (paramVo.getStreetCode().equals("440305")) {
                paramVo.setStreetCode("");
            }
        }
        EventDynamicOut eventDetailOut = new EventDynamicOut();
        try {
            AccessToken accessToken = getAccessToken(accessTokenUrl);
            logger.info("accessToken{}", accessToken);
            List<DynamicEvent> eventFromThird = getEventFromThird(dynamicEventUrl, accessToken.getAccess_token(), paramVo);
            eventDetailOut.setDynamicEvents(eventFromThird);
            return eventDetailOut;
        } catch (SqxException e) {
            throw e;
        } catch (Exception e) {
            logger.error("调用数仓失败", e);
        }
        return null;
    }

    @Override
    @Transactional
    public void saveDynamicEventComment(EventCommentParam1 paramVo) {
        Date createdTime = new Date();
        String orderNum = null;
        try {
            DynamicEvent dynamicEvent = paramVo.getDynamicEvent();
            String normalTypeCode = dynamicEvent.getNormalTypeCode();
            String token = GetToken.createJwt();
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("event_uuid", dynamicEvent.getSourceTableId());
            queryWrapper.eq("in_or_out", 2);
            OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);
            if (orderInfo != null) {
                String sendApp = null;
                if (normalTypeCode.contains("民意速办")) {//分拨系统
                    sendApp = sjfb;
                } else if (normalTypeCode.contains("社会风险防范")) {//政法平安
                    sendApp = zfpa;
                } else if (normalTypeCode.contains("网格上报")) {//网格上报
                    sendApp = wg;
                }
                orderNum = orderInfo.getOrderNum();
                OrderParamBasic orderParamBasic = new OrderParamBasic();
                orderParamBasic.setOrderNum(orderNum);
                orderParamBasic.setRecAppCode(recAppCode);
                orderParamBasic.setNewRecAppCode(sendApp);
                orderParamBasic.setUserId(paramVo.getUserInfo().getUserId());
                orderParamBasic.setUserName(paramVo.getUserInfo().getUserName());
                EventProcessBasic eventProcessBasic = new EventProcessBasic();
                eventProcessBasic.setMatterUuid(orderNum);
                eventProcessBasic.setOrderNum(orderNum);
                eventProcessBasic.setEventMID(dynamicEvent.getSourceTableId());
                eventProcessBasic.setCreatedTime(DateUtils.format(createdTime, DateUtils.DATE_TIME_PATTERN));
                eventProcessBasic.setUserId(paramVo.getUserInfo().getUserId());
                eventProcessBasic.setUserName(paramVo.getUserInfo().getUserName());
                if (paramVo.getCommentOrFeedBack() == 1) {
                    eventProcessBasic.setStatus(3);
                    eventProcessBasic.setDecisionComment(paramVo.getComment());
                } else {
                    eventProcessBasic.setStatus(4);
                    eventProcessBasic.setFeedbackComment(paramVo.getComment());
                }
                eventProcessBasic.setEventDetail(dynamicEvent);
                ObjectMapper objectMapper = new ObjectMapper();
                String s = objectMapper.writeValueAsString(eventProcessBasic);
                orderParamBasic.setDataPackage(s);
                String s1 = objectMapper.writeValueAsString(orderParamBasic);
                logger.info(s1);

                //查询event_info
                QueryWrapper<EventInfo> queryWrapperExist = new QueryWrapper();
                queryWrapperExist.eq("event_uuid", dynamicEvent.getSourceTableId());
                EventInfo eventInfo1 = eventInfoMapper.selectOne(queryWrapperExist);

                EventComment eventComment = new EventComment();
                eventComment.setEventId(eventInfo1.getId());
                eventComment.setEventUuid(dynamicEvent.getSourceTableId());
                eventComment.setOrderNum(orderNum);
                eventComment.setUserId(paramVo.getUserInfo().getUserId());
                eventComment.setCreatedTime(createdTime);
                eventComment.setComment(paramVo.getComment());
                eventComment.setUnitName(paramVo.getUserInfo().getDepartmentName());
                eventComment.setUnitCode(paramVo.getUserInfo().getDepartmentCode());
                eventComment.setUserName(paramVo.getUserInfo().getUserName());
                eventCommentMapper.insert(eventComment);

                orderCirculation(orderParamBasic, GetToken.createJwt(), orderCircleURL);
            } else {
                //获取工单号
                OrderNumBasic orderParamBasic = new OrderNumBasic();
                orderParamBasic.setCataId(dynamicEvent.getSourceTableId());
                orderParamBasic.setAppCode(recAppCode);
                orderParamBasic.setEventCode("99999998");
                orderParamBasic.setCatalog("海致辖区动态事件批示推送！");
                orderParamBasic.setRegion(paramVo.getUserInfo().getDepartmentCode());
                orderNum = getOrderNum(orderParamBasic, token, orderNumUrl);
                logger.info("接口获取工单号：{}",orderNum);

                OrderCreateParamBasic orderCreateParamBasic = new OrderCreateParamBasic();
                orderCreateParamBasic.setOrderNum(orderNum);
                orderCreateParamBasic.setSendAppCode(recAppCode);
                EventBasicOut eventBasic = new EventBasicOut();
                EventInfo eventInfo = new EventInfo();
                //目前政法平安的只有矛盾纠纷事件，网格系统：网格事件，分拨系统：民生诉求，政法平安系统：维稳、重点、矛盾纠纷
                String sendAppName = null;
                String sendApp = null;
                if (normalTypeCode.contains("民意速办")) {//分拨系统
                    sendApp = sjfb;
                    sendAppName = "事件分拨";
                    /*eventBasic.setDecisionPersonID("090e5e4c952d4cb887448e1acf0cd11f");
                    eventBasic.setDecisionPersonName("汤开新主任");*/
                    eventBasic.setDecisionPersonID(sjfbId);
                    eventBasic.setDecisionPersonName(sjfbUserName);

                   /* eventBasic.setDecisionPersonID("b0e0b58001c84689bd91231f39edb332");
                    eventBasic.setDecisionPersonName("黄建松");*/

                } else if (normalTypeCode.contains("社会风险防范")) {//政法平安
                    sendApp = zfpa;
                    sendAppName = "政法平安";
                    /*eventBasic.setDecisionPersonID("72fb530deac2461fa7187a5b4fae76eb");
                    eventBasic.setDecisionPersonName("易小蓉科长");*/
                    eventBasic.setDecisionPersonID(zfpaId);
                    eventBasic.setDecisionPersonName(zfpaUserName);
                    /*eventBasic.setDecisionPersonID("9c7a9928db2b4c0c8565fb97961477e3");
                    eventBasic.setDecisionPersonName("郑娜");*/

                } else if (normalTypeCode.contains("网格上报")) {//网格上报
                    sendApp = wg;
                    sendAppName = "网格化+";
                    /*eventBasic.setDecisionPersonID("8d467cff9dbe458f8712d5fc0fa68e74");
                    eventBasic.setDecisionPersonName("张雯雯部长");*/
                    eventBasic.setDecisionPersonID(wgId);
                    eventBasic.setDecisionPersonName(wgUserName);
                   /* eventBasic.setDecisionPersonID("02b12a7625014d89b4428a6868306663");
                    eventBasic.setDecisionPersonName("马泽华");*/

                }
                orderCreateParamBasic.setRecAppCode(sendApp);
                orderCreateParamBasic.setSenderId(paramVo.getUserInfo().getUserId());
                String format = DateUtils.format(createdTime, DateUtils.DATE_TIME_PATTERN);
                orderCreateParamBasic.setSendTime(format);
                orderCreateParamBasic.setEventCode("99999998");
                orderCreateParamBasic.setApplyTime(format);
                orderCreateParamBasic.setUserType("01");
                orderCreateParamBasic.setUserId(paramVo.getUserInfo().getUserId());
                orderCreateParamBasic.setName(paramVo.getUserInfo().getUserName());
                orderCreateParamBasic.setContactNo(paramVo.getUserInfo().getPhone());
                orderCreateParamBasic.setEventContent(dynamicEvent.getEventDesc());
                orderCreateParamBasic.setAddressCode(dynamicEvent.getStreetCode());

                //数据包处理
                eventBasic.setEventMID(dynamicEvent.getSourceTableId());
                eventBasic.setEventCode(dynamicEvent.getEventCode());
                eventBasic.setMatterUuid(orderNum);
                eventBasic.setDecisionComment(paramVo.getComment());
                eventBasic.setName(dynamicEvent.getTitle());
                eventBasic.setTakePlace(dynamicEvent.getHappenAddress());
                eventBasic.setAppearTime(dynamicEvent.getReportTime());
                eventBasic.setMatterSummarize(dynamicEvent.getEventDesc());
                eventBasic.setMatterSource(6);
                eventBasic.setMatterType(1);
                eventBasic.setUnitSource(dynamicEvent.getStreetName());
                eventBasic.setStatus(3);
                eventBasic.setUserId(paramVo.getUserInfo().getUserId());
                eventBasic.setUserName(paramVo.getUserInfo().getUserName());
                eventBasic.setSystemid(sendApp);
                eventBasic.setEventDetail(dynamicEvent);
                ObjectMapper objectMapper = new ObjectMapper();
                String s = objectMapper.writeValueAsString(eventBasic);
                orderCreateParamBasic.setDataPackage(s);

                //本地order_info、event_info保存信息
                OrderInfo orderInfoInsert = new OrderInfo();
                orderInfoInsert.setOrderNum(orderNum);
                orderInfoInsert.setSendAppCode(recAppCode);
                orderInfoInsert.setRecAppCode(sendApp);
                orderInfoInsert.setCreateTime(createdTime);
                orderInfoInsert.setInOrOut("2");
                orderInfoInsert.setEventUuid(dynamicEvent.getSourceTableId());
                String s1 = objectMapper.writeValueAsString(orderCreateParamBasic);
                orderInfoInsert.setOrderJson(s1);
                orderInfoInsert.setDataPackage(s);
                orderInfoInsert.setSenderId(paramVo.getUserInfo().getUserId());
                orderInfoInsert.setSendTime(format);
                orderInfoInsert.setEventCode("99999998");
                orderInfoInsert.setUserId(paramVo.getUserInfo().getUserId());
                orderInfoInsert.setName(paramVo.getUserInfo().getUserName());
                orderInfoInsert.setApplyTime(format);
                orderInfoInsert.setMatterUuid(orderNum);
                orderInfoInsert.setCataId(dynamicEvent.getSourceTableId());
                orderInfoInsert.setCatalog("辖区动态事项推向业务方！");
                orderInfoMapper.insert(orderInfoInsert);

                //判断是否存在event_info
                //查询event_info
                QueryWrapper<EventInfo> queryWrapperExist = new QueryWrapper();
                queryWrapperExist.eq("event_uuid", dynamicEvent.getSourceTableId());
                EventInfo eventInfo1 = eventInfoMapper.selectOne(queryWrapperExist);
                if (eventInfo1 == null) {
                    eventInfo.setEventMid(dynamicEvent.getSourceTableId());
                    eventInfo.setEventUuid(dynamicEvent.getSourceTableId());
                    eventInfo.setDataSource("2");
                    eventInfo.setSendTime(DateUtils.format(createdTime, DateUtils.DATE_TIME_PATTERN));
                    eventInfo.setCreatedTime(createdTime);
                    eventInfo.setStreetName(dynamicEvent.getStreetName());
                    eventInfo.setStreet(dynamicEvent.getStreetName());
                    eventInfo.setStreetCode(dynamicEvent.getStreetCode());
                    eventInfo.setWorkOrderType(dynamicEvent.getTypeName());
                    if (StringUtils.isNotBlank(dynamicEvent.getReportTime()) && dynamicEvent.getReportTime().contains(".0")) {
                        Date localDateTime = DateUtils.handlePointTime(dynamicEvent.getReportTime());
                        eventInfo.setAppearTime(localDateTime);
                    } else if (StringUtils.isNotBlank(dynamicEvent.getReportTime())) {
                        eventInfo.setAppearTime(DateUtils.stringToDate(dynamicEvent.getReportTime(), DateUtils.DATE_TIME_PATTERN));
                    }
                    eventInfo.setEventSummarize(dynamicEvent.getEventDesc());
                    eventInfo.setEventContent(dynamicEvent.getEventDesc());
                    eventInfo.setEventMid(dynamicEvent.getSourceTableId());
                    eventInfo.setName(dynamicEvent.getTitle());
                    eventInfo.setUnitSource(dynamicEvent.getStreetName());
                    eventInfo.setMatterSource("6");
                    eventInfo.setMatterType("1");
                    eventInfo.setStatus("3");
                    eventInfo.setTakePlace(dynamicEvent.getHappenAddress());
                    eventInfo.setTakeAddress(dynamicEvent.getHappenAddress());
                    eventInfo.setUnitUserId(paramVo.getUserInfo().getUserId());
                    eventInfo.setUnitUsername(paramVo.getUserInfo().getUserName());
                    eventInfo.setDecisionPersonId(eventBasic.getDecisionPersonID());
                    eventInfo.setDecisionPersonName(eventBasic.getDecisionPersonName());
                    eventInfo.setOrderNum(orderNum);
                    eventInfo.setCommunity(dynamicEvent.getCommunityName());
                    eventInfo.setCommunityCode(dynamicEvent.getCommunityCode());
//                eventInfo.setIsAudit(true);
                    eventInfo.setMatterUuid(orderNum);
                    eventInfo.setSystemid(sendApp);
                    eventInfo.setSystemSource(sendAppName);
                    eventInfoMapper.insert(eventInfo);

                    //新建批示
                    EventComment eventComment = new EventComment();
                    eventComment.setEventId(eventInfo.getId());
                    eventComment.setEventUuid(dynamicEvent.getSourceTableId());
                    eventComment.setOrderNum(orderNum);
                    eventComment.setUserId(paramVo.getUserInfo().getUserId());
                    eventComment.setCreatedTime(createdTime);
                    eventComment.setComment(paramVo.getComment());
                    eventComment.setUnitName(paramVo.getUserInfo().getDepartmentName());
                    eventComment.setUnitCode(paramVo.getUserInfo().getDepartmentCode());
                    eventComment.setUserName(paramVo.getUserInfo().getUserName());
                    eventCommentMapper.insert(eventComment);
                    //批示即关注
                    EventAttention eventAttention = new EventAttention();
                    eventAttention.setEventId(eventInfo.getId());
                    eventAttention.setUserId(paramVo.getUserInfo().getUserId());
                    eventAttention.setAttentionStatus("1");
                    eventAttention.setCreatedTime(createdTime);
                    eventAttention.setEventUuid(dynamicEvent.getSourceTableId());
                    eventAttentionMapper.insert(eventAttention);

                } else {
                    //先关注后批示 修改事项order_num
                    eventInfo1.setOrderNum(orderNum);
                    eventInfo1.setMatterUuid(orderNum);
                    eventInfo1.setStatus("3");
                    eventInfoMapper.updateById(eventInfo1);
                    //新增批示
                    EventComment eventComment = new EventComment();
                    eventComment.setEventId(eventInfo1.getId());
                    eventComment.setEventUuid(dynamicEvent.getSourceTableId());
                    eventComment.setOrderNum(orderNum);
                    eventComment.setUserId(paramVo.getUserInfo().getUserId());
                    eventComment.setCreatedTime(createdTime);
                    eventComment.setComment(paramVo.getComment());
                    eventComment.setUnitName(paramVo.getUserInfo().getDepartmentName());
                    eventComment.setUnitCode(paramVo.getUserInfo().getDepartmentCode());
                    eventComment.setUserName(paramVo.getUserInfo().getUserName());
                    eventCommentMapper.insert(eventComment);
                }
                //创建工单,推送数据
                logger.info("开始调用创建工单接口：{}",orderNum);
                orderCreate(orderCreateParamBasic, token, orderCreateURL);
            }
        } catch (SqxException e) {
            logger.error("创建工单失败，工单号：{},{}",orderNum,e);
            throw e;
        } catch (Exception e) {
            logger.error("调用工单接口失败", e);
            throw new SqxException("调用工单接口失败");
        }
    }

    @Override
    @Transactional
    public void saveDynamicAttentionOrCancle(EventAttentionParam1 paramVo) {
        DynamicEvent dynamicEvent = paramVo.getDynamicEvent();
        //判断order_info是否已创建，只有批示才有可能创建工单
        QueryWrapper<OrderInfo> queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("event_uuid", dynamicEvent.getSourceTableId()).eq("in_or_out", 2);
        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper1);
        Date createdTime = new Date();
        //查询本地是否已经建立好数据
        //如果orderinfo为空 说明还没有进行批示
        if (orderInfo == null) {
            //查询是否已经有此事项数据 可能多次关注或取消
            QueryWrapper<EventInfo> eventInfoQueryWrapper = new QueryWrapper<>();
            eventInfoQueryWrapper.eq("event_uuid", dynamicEvent.getSourceTableId());
            EventInfo eventInfoExist = eventInfoMapper.selectOne(eventInfoQueryWrapper);
            if (eventInfoExist == null) {
                //创建event_info信息
                EventInfo eventInfo = new EventInfo();
                eventInfo.setSendTime(DateUtils.format(createdTime, DateUtils.DATE_TIME_PATTERN));
                eventInfo.setDataSource("2");
                eventInfo.setCreatedTime(createdTime);
                eventInfo.setStreetName(dynamicEvent.getStreetName());
                eventInfo.setStreet(dynamicEvent.getStreetName());
                eventInfo.setStreetCode(dynamicEvent.getStreetCode());
                eventInfo.setWorkOrderType(dynamicEvent.getNormalTypeCode());
                if (StringUtils.isNotBlank(dynamicEvent.getReportTime()) && dynamicEvent.getReportTime().contains(".0")) {
                    Date localDateTime = DateUtils.handlePointTime(dynamicEvent.getReportTime());
                    eventInfo.setAppearTime(localDateTime);
                } else if (StringUtils.isNotBlank(dynamicEvent.getReportTime())) {
                    eventInfo.setAppearTime(DateUtils.stringToDate(dynamicEvent.getReportTime(), DateUtils.DATE_TIME_PATTERN));
                }
                eventInfo.setEventSummarize(dynamicEvent.getEventDesc());
                eventInfo.setEventContent(dynamicEvent.getEventDesc());
                eventInfo.setName(dynamicEvent.getTitle());
                eventInfo.setUnitSource(dynamicEvent.getStreetName());
                eventInfo.setTakeAddress(dynamicEvent.getHappenAddress());
                eventInfo.setTakePlace(dynamicEvent.getHappenAddress());
                eventInfo.setMatterSource("6");
                eventInfo.setMatterType("1");
                eventInfo.setUnitUserId(paramVo.getUserInfo().getUserId());
                eventInfo.setUnitUsername(paramVo.getUserInfo().getUserName());
                //目前政法平安的只有矛盾纠纷事件，网格系统：网格事件，分拨系统：民生诉求，政法平安系统：维稳、重点、矛盾纠纷
                String normalTypeCode = dynamicEvent.getNormalTypeCode();
                String sendApp = null;
                String sendAppName = null;
                EventAttention eventAttention = new EventAttention();
                if (normalTypeCode.contains("民意速办")) {//分拨系统
                    sendApp = sjfb;
                    sendAppName = "事件分拨";
                    eventInfo.setDecisionPersonId("090e5e4c952d4cb887448e1acf0cd11f");
                    eventInfo.setDecisionPersonName("汤开新主任");
                   /* eventInfo.setDecisionPersonId("b0e0b58001c84689bd91231f39edb332");
                    eventInfo.setDecisionPersonName("黄建松");*/
                } else if (normalTypeCode.contains("社会风险防范")) {//政法平安
                    sendApp = zfpa;
                    sendAppName = "政法平安";
                    eventInfo.setDecisionPersonId("72fb530deac2461fa7187a5b4fae76eb");
                    eventInfo.setDecisionPersonName("易小蓉科长");
                   /* eventInfo.setDecisionPersonId("9c7a9928db2b4c0c8565fb97961477e3");
                    eventInfo.setDecisionPersonName("郑娜");*/
                } else if (normalTypeCode.contains("网格上报")) {//网格上报
                    sendApp = wg;
                    sendAppName = "网格化+";
                    eventInfo.setDecisionPersonId("8d467cff9dbe458f8712d5fc0fa68e74");
                    eventInfo.setDecisionPersonName("张雯雯部长");
                    /*eventInfo.setDecisionPersonId("02b12a7625014d89b4428a6868306663");
                    eventInfo.setDecisionPersonName("马泽华");*/
                }
                eventInfo.setEventUuid(dynamicEvent.getSourceTableId());
                eventInfo.setEventMid(dynamicEvent.getSourceTableId());
                eventInfo.setCommunity(dynamicEvent.getCommunityName());
                eventInfo.setCommunityCode(dynamicEvent.getCommunityCode());
                eventInfo.setSystemid(sendApp);
                eventInfo.setSystemSource(sendAppName);
                eventInfoMapper.insert(eventInfo);
                //新增关注
                eventAttention.setEventUuid(dynamicEvent.getSourceTableId());
                eventAttention.setEventId(eventInfo.getId());
                eventAttention.setUserId(paramVo.getUserInfo().getUserId());
                eventAttention.setAttentionStatus(paramVo.getAttentionStatus());
                eventAttention.setCreatedTime(createdTime);
                eventAttentionMapper.insert(eventAttention);
            } else {
                QueryWrapper<EventInfo> queryWrapper = new QueryWrapper();
                queryWrapper.eq("event_uuid", dynamicEvent.getSourceTableId());
                EventInfo eventInfo1 = eventInfoMapper.selectOne(queryWrapper);
                EventAttention eventAttention = new EventAttention();
                eventAttention.setAttentionStatus(paramVo.getAttentionStatus());
                QueryWrapper<EventAttention> attentionQueryWrapper = new QueryWrapper<>();
                attentionQueryWrapper.eq("event_id", eventInfo1.getId()).eq("user_id", paramVo.getUserInfo().getUserId());
                eventAttentionMapper.update(eventAttention, attentionQueryWrapper);
            }
        } else {
            try {
                //已经评论创建工单后进行关注
                QueryWrapper<EventInfo> queryWrapper = new QueryWrapper();
                queryWrapper.eq("event_uuid", dynamicEvent.getSourceTableId());
                EventInfo eventInfo1 = eventInfoMapper.selectOne(queryWrapper);
                QueryWrapper<EventAttention> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("event_id", eventInfo1.getId()).eq("user_id", paramVo.getUserInfo().getUserId());
                EventAttention attention = eventAttentionMapper.selectOne(queryWrapper2);
                if (attention == null) { //为空首次关注推送数据
                    if (paramVo.getAttentionStatus().equals("1")) {
                        String orderNum = orderInfo.getOrderNum();
                        OrderParamBasic orderParamBasic = new OrderParamBasic();
                        orderParamBasic.setOrderNum(orderNum);
                        orderParamBasic.setHandleApp(recAppCode);
                        orderParamBasic.setProcessAction("辖区动态事项-海致领导关注！");
                        orderParamBasic.setOrderState("5");
                        orderParamBasic.setUserName(paramVo.getUserInfo().getUserName());
                        EventProcessBasic eventProcessBasic = new EventProcessBasic();
                        eventProcessBasic.setMatterUuid(orderNum);
                        eventProcessBasic.setOrderNum(orderNum);
                        eventProcessBasic.setEventMID(dynamicEvent.getSourceTableId());
                        eventProcessBasic.setCreatedTime(DateUtils.format(createdTime, DateUtils.DATE_TIME_PATTERN));
                        eventProcessBasic.setUserId(paramVo.getUserInfo().getUserId());
                        eventProcessBasic.setUserName(paramVo.getUserInfo().getUserName());
                        eventProcessBasic.setReplyType(1);
                        eventProcessBasic.setEventDetail(dynamicEvent);
                        ObjectMapper objectMapper = new ObjectMapper();
                        String s = objectMapper.writeValueAsString(eventProcessBasic);
                        orderParamBasic.setDataPackage(s);
                        String s1 = objectMapper.writeValueAsString(orderParamBasic);
                        logger.info("关注推送：{}", s1);
                        orderCirculation(orderParamBasic, GetToken.createJwt(), orderUpdateURL);
                    }
                    EventAttention eventAttention = new EventAttention();
                    eventAttention.setEventId(eventInfo1.getId());
                    eventAttention.setUserId(paramVo.getUserInfo().getUserId());
                    eventAttention.setAttentionStatus(paramVo.getAttentionStatus());
                    eventAttention.setEventUuid(dynamicEvent.getSourceTableId());
                    eventAttention.setCreatedTime(createdTime);
                    eventAttentionMapper.insert(eventAttention);
                } else {
                    //关注或者取消关注
                    EventAttention eventAttention = new EventAttention();
                    eventAttention.setEventId(eventInfo1.getId());
                    eventAttention.setUserId(paramVo.getUserInfo().getUserId());
                    eventAttention.setAttentionStatus(paramVo.getAttentionStatus());
                    eventAttentionMapper.update(eventAttention, queryWrapper2);

                    String orderNum = orderInfo.getOrderNum();
                    OrderParamBasic orderParamBasic = new OrderParamBasic();
                    orderParamBasic.setOrderNum(orderNum);
                    orderParamBasic.setHandleApp(recAppCode);
                    orderParamBasic.setProcessAction("辖区动态事项-海致领导关注！");
                    orderParamBasic.setOrderState("5");
                    orderParamBasic.setUserName(paramVo.getUserInfo().getUserName());
                    EventProcessBasic eventProcessBasic = new EventProcessBasic();
                    eventProcessBasic.setMatterUuid(orderNum);
                    eventProcessBasic.setOrderNum(orderNum);
                    eventProcessBasic.setEventMID(dynamicEvent.getSourceTableId());
                    eventProcessBasic.setCreatedTime(DateUtils.format(createdTime, DateUtils.DATE_TIME_PATTERN));
                    eventProcessBasic.setUserId(paramVo.getUserInfo().getUserId());
                    eventProcessBasic.setUserName(paramVo.getUserInfo().getUserName());
                    if (paramVo.getAttentionStatus().equals("1")) {
                        eventProcessBasic.setReplyType(1);
                    } else if (paramVo.getAttentionStatus().equals("0")) {
                        eventProcessBasic.setReplyType(3);
                    }
                    eventProcessBasic.setEventDetail(dynamicEvent);
                    ObjectMapper objectMapper = new ObjectMapper();
                    String s = objectMapper.writeValueAsString(eventProcessBasic);
                    orderParamBasic.setDataPackage(s);
                    String s1 = objectMapper.writeValueAsString(orderParamBasic);
                    logger.info("关注推送：{}", s1);
                    orderCirculation(orderParamBasic, GetToken.createJwt(), orderUpdateURL);
                }
            } catch (SqxException e) {
                throw e;
            } catch (Exception e) {
                logger.error("关注/取消关注失败", e);
                throw new SqxException("关注/取消关注失败");
            }
        }
    }

    @Override
    public EventDynamicCommentOut commentListAttention(DynamicEvent dynamicEvent) {
        EventDynamicCommentOut eventDynamicCommentOut = new EventDynamicCommentOut();
        QueryWrapper<EventInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("event_uuid", dynamicEvent.getSourceTableId());
        EventInfo eventInfo = eventInfoMapper.selectOne(queryWrapper);
        if (eventInfo != null) {
            //查询关注状态
            QueryWrapper<EventAttention> queryWrapperAtt = new QueryWrapper<>();
            queryWrapperAtt.eq("event_id", eventInfo.getId());
            EventAttention eventAttention = eventAttentionMapper.selectOne(queryWrapperAtt);
            if (eventAttention != null) {
                eventDynamicCommentOut.setAttentionStatus(eventAttention.getAttentionStatus());
            } else {
                eventDynamicCommentOut.setAttentionStatus("0");
            }
            //查询批示 反馈详情
            QueryWrapper<EventComment> queryWrapper8 = new QueryWrapper<EventComment>();
            queryWrapper8.eq("order_num", eventInfo.getOrderNum());
            queryWrapper8.orderByDesc("created_time");
            List<EventComment> eventComments = eventCommentMapper.selectList(queryWrapper8);
            eventDynamicCommentOut.setEventCommentList(eventComments);
            return eventDynamicCommentOut;
        }
        eventDynamicCommentOut.setAttentionStatus("0");
        eventDynamicCommentOut.setEventCommentList(new ArrayList<>());
        return eventDynamicCommentOut;
    }

    @Override
    public DynamicEventTotal getEventTotal(DynamicEventTotalParam paramVo) {
        if (StringUtils.isNotBlank(paramVo.getStreetCode())) {
            if (paramVo.getStreetCode().equals("440305")) {
                paramVo.setStreetCode("");
            }
        }
        AccessToken accessToken = getAccessToken(accessTokenUrl);
        List<DynamicEventTotal> eventTotal = getEventTotal(eventTotalUrl, accessToken.getAccess_token(), paramVo);
        if (!CollectionUtils.isEmpty(eventTotal)) {
            return eventTotal.get(0);
        }
        DynamicEventTotal dynamicEventTotal = new DynamicEventTotal();
        dynamicEventTotal.setTotal("0");
        return dynamicEventTotal;
    }

    @Override
    public void pushMessage(OrderMessageBasic eventBasic) {

    }

    private String getOrderNum(OrderNumBasic orderParamBasic, String token, String urls) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        // 创建URL对象
        URL url = new URL(urls);
        // 打开连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 设置请求方法为POST
        connection.setRequestMethod("POST");
        // 设置Content-Type为application/json
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", token);
        // 启用输出流
        connection.setDoOutput(true);
        // 创建JSON数据
        String jsonData = objectMapper.writeValueAsString(orderParamBasic);
        logger.info("工单编号返回json数据：{}", jsonData);
        // 发送JSON数据
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        // 获取响应状态码
        int responseCode = connection.getResponseCode();
        logger.info("查询工单编号返回json数据：Response Code: " + responseCode);

        try {
            if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) { // 500
                // 服务器内部错误，读取响应体中的错误信息
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
                String inputLine;
                StringBuffer errorResponse = new StringBuffer();

                while ((inputLine = errorReader.readLine()) != null) {
                    errorResponse.append(inputLine);
                }
                errorReader.close();
                // 解析错误信息
                String errorData = errorResponse.toString();
                logger.error("工单流转失败Error data: " + errorData);
                throw new SqxException("调用创建工单编号失败");
            } else if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                br.close();
                OrderNumJson orderNumJson = JSONObject.parseObject(response.toString(), new TypeReference<OrderNumJson>() {
                });
                if (orderNumJson.getCode().equals(0)) {
                    logger.info("工单流转成功：Response: " + response.toString());
                    OrderNum data = orderNumJson.getData();
                    String orderNum = data.getOrderNum();
                    return orderNum;
                } else {
                    throw new SqxException(response.toString());
                }
            } else {
                throw new SqxException("工单流转失败,code:{}", responseCode);
            }
        } catch (Exception e) {
            logger.error("工单流转失败：", e);
            throw e;
        } finally {
            // 关闭连接
            connection.disconnect();
        }
    }


    public List<Department> getAllDept() {
        String configPath = "allDepart.json";
        try {
            ClassPathResource resource = new ClassPathResource(configPath);
            byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String s = new String(bytes);
            // 解析JSON数据
            List<Department> result = JSON.parseObject(s, new TypeReference<List<Department>>() {
            });
            return result;

        } catch (IOException e) {
            logger.error("Error reading config file: " + e.getMessage());
        }
        return null;
    }


    @Scheduled(cron = "0 0/1 * * * ?")
    public void handleFile() {
        List<AttachmentInfo> attachmentInfos = attachmentInfoMapper.selectAttachmentUnHandle();
        if (!CollectionUtils.isEmpty(attachmentInfos)) {
            for (AttachmentInfo attachmentInfo : attachmentInfos) {
                try {
                    //创建存放目录
                    String dataFile = "/decisionfile/" + DateUtils.format(new Date(), DateUtils.DATE_PATTERN);
                    String saveDirPre = "/" + DateUtils.format(new Date(), DateUtils.DATE_PATTERN);
                    File directory = new File(dataFile);
                    if (!directory.exists()) {
                        // 创建目录
                        boolean isCreated = directory.mkdirs();
                        if (isCreated) {
                            logger.info("目录创建成功: " + dataFile);
                        } else {
                            logger.info("目录创建失败");
                        }
                    } else {
                        logger.info("目录已存在: " + dataFile);
                    }

                    // 对文件路径进行URL编码
                    String filePath = attachmentInfo.getFilePath();
                    String substring = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
                    String encodedFilePath = URLEncoder.encode(substring, "UTF-8");
                    // 替换编码后空格为"%20"，因为URLEncoder会将空格编码为"+"
                    encodedFilePath = encodedFilePath.replace("+", "%20");
                    filePath = filePath.replace(substring, encodedFilePath);
                    URL url = new URL(filePath);
                    URLConnection connection = url.openConnection();
                    String fileName = attachmentInfo.getFileName();
                    String savePath = null;
                    String saveDir = null;
                    if (fileName.contains("/")) {
                        savePath = dataFile + "/" + fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
                        saveDir = saveDirPre + "/" + fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
                        ;
                    } else {
                        savePath = dataFile + "/" + fileName;
                        saveDir = saveDirPre + "/" + fileName;
                    }
                    try (BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
                         FileOutputStream fileOutputStream = new FileOutputStream(savePath)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, bytesRead);
                        }
                        logger.info("文件下载成功: " + savePath);
                    }
                    attachmentInfo.setCurrentFileUrl(saveDir);
                    attachmentInfoMapper.updateById(attachmentInfo);
                } catch (Exception e) {
                    logger.error("下载文件失败", e);
                }
            }
        }


    }

    public static void main(String[] args) {
        String depart = "南山街道党工委（办事处）";
        String substring = depart.substring(0, depart.indexOf("街道") + 2);
        System.out.println(substring);

    }

    public static void downloadFile(String url, String orderNum, String filename, String token, String path) throws IOException, InterruptedException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        // 设置请求头
//        httpPost.addHeader("Content-Type", "application/octet-stream");
        httpPost.addHeader("Authorization", token);

        // 构建multipart/form-data请求体
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("orderNum", orderNum, ContentType.TEXT_PLAIN);
        builder.addTextBody("filename", filename, ContentType.TEXT_PLAIN);
        HttpEntity multipart = builder.build();

        // 设置请求体
        httpPost.setEntity(multipart);

        // 发送请求并获取响应
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();
        System.out.println("返回结果：" + responseEntity);

        /*InputStream content = responseEntity.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(content)) ;
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }*/
        // 检查响应状态码
        String savePath = path + "/" + filename;
        if (response.getStatusLine().getStatusCode() == 200) {
            // 处理响应体中的文件数据
            try (InputStream inputStream = responseEntity.getContent();
                 FileOutputStream outputStream = new FileOutputStream(savePath)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            System.out.println("文件下载成功: " + filename);
        } else {
            System.out.println("文件下载失败, HTTP状态码: " + response.getStatusLine().getStatusCode());
        }

        // 关闭HttpClient
        httpClient.close();
    }


    //获取access_token
    private AccessToken getAccessToken(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            // 创建HttpPost对象
            HttpPost httpPost = new HttpPost(url);

            // 设置请求头
            httpPost.setHeader("Content-Type", "application/json");

            // 设置请求体
            String jsonBody = "{\"client_secret\":\"572f580a1d0484e6692d\",\"client_id\":\"0cd5e15e92119ef58c99\"}";
            StringEntity entity = new StringEntity(jsonBody);
            httpPost.setEntity(entity);
            // 执行请求并获取响应
            CloseableHttpResponse response = httpClient.execute(httpPost);
            logger.info("获取accessToken返回code：{}", response.getStatusLine().getStatusCode());
            try {
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity responseEntity = response.getEntity();
                    if (responseEntity != null) {
                        // 输出响应内容
                        String responseString = EntityUtils.toString(responseEntity, "UTF-8");
                        logger.info("accessToken: " + responseString);
                        AccessToken accessToken = JSON.parseObject(responseString, new TypeReference<AccessToken>() {
                        });
                        return accessToken;
                    }
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {
            logger.error("获取accessToken失败", e);
        } catch (Exception e) {
            logger.error("获取accessToken失败", e);
            throw e;
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    List<DynamicEvent> getEventFromThird(String url, String token, DynamicEventParam dynamicEventParam) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(dynamicEventParam);
            // 创建HttpPost对象
            HttpPost httpPost = new HttpPost(url);
            // 设置请求头
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Authorization", "Bearer " + token);
            // 设置请求体
            StringEntity entity = new StringEntity(s, StandardCharsets.UTF_8);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            // 执行请求并获取响应
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    // 输出响应内容
                    String responseString = EntityUtils.toString(responseEntity, "UTF-8");
                    logger.info("Response: " + responseString);
                    DynamicEventJson dynamicEventJson = JSON.parseObject(responseString, new TypeReference<DynamicEventJson>() {
                    });
                    String data = dynamicEventJson.getData();
                    if (StringUtils.isNotBlank(data)) {
                        List<DynamicEvent> dynamicEvents = JSON.parseObject(data, new TypeReference<List<DynamicEvent>>() {
                        });
                        return dynamicEvents;
                    }
                }
            } catch (Exception e) {
                logger.error("调用失败", e);
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    List<DynamicEventTotal> getEventTotal(String url, String token, DynamicEventTotalParam dynamicEventParam) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(dynamicEventParam);
            // 创建HttpPost对象
            HttpPost httpPost = new HttpPost(url);
            // 设置请求头
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Authorization", "Bearer " + token);
            // 设置请求体
            StringEntity entity = new StringEntity(s, StandardCharsets.UTF_8);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            // 执行请求并获取响应
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    // 输出响应内容
                    String responseString = EntityUtils.toString(responseEntity, "UTF-8");
                    logger.info("Response: " + responseString);
                    DynamicEventJson dynamicEventJson = JSON.parseObject(responseString, new TypeReference<DynamicEventJson>() {
                    });
                    String data = dynamicEventJson.getData();
                    if (StringUtils.isNotBlank(data)) {
                        List<DynamicEventTotal> dynamicEvents = JSON.parseObject(data, new TypeReference<List<DynamicEventTotal>>() {
                        });
                        return dynamicEvents;
                    }
                }
            } catch (Exception e) {
                logger.error("调用失败", e);
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //处理部门权限
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void handleDpt() {
        //查询部门为空的事项
        List<EventInfo> eventInfos = eventInfoMapper.selectEventStreetIsNull();
        if (!CollectionUtils.isEmpty(eventInfos)) {
            for (EventInfo eventInfo : eventInfos) {
                String decisionPersonID = eventInfo.getDecisionPersonId();
                if (StringUtils.isNotBlank(decisionPersonID)) {
                    List<String> list = new ArrayList<>();
                    if (decisionPersonID.contains(";")) {
                        list = Arrays.asList(decisionPersonID.split(";"));
                    } else {
                        list.add(decisionPersonID);
                    }
                    int role = 0;
                    if (!CollectionUtils.isEmpty(list)) {
                        for (String s : list) {
                            try {
                                UserInfo userInfoById = getUserInfoById(GetToken.createJwt(), s);
                                if (userInfoById != null) {
                                    List<UserRole> roleList = userInfoById.getRoleList();
                                    if (!CollectionUtils.isEmpty(roleList)) {
                                        for (UserRole userRole : roleList) {
                                            String roleName = userRole.getRoleName();
                                            if (roleName.contains("区") && roleName.contains("领导")) {
                                                role = 1;
                                                break;
                                            }
                                        }
                                        if (role == 1) {
                                            eventInfo.setStreet("区");
                                            eventInfo.setStreetName("区");
                                        } else {
                                            eventInfo.setStreet(userInfoById.getDepartmentName());
                                            eventInfo.setStreetName(userInfoById.getDepartmentName());
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                logger.error("调用人员接口失败", e);
                            }
                        }
                    }
                    eventInfoMapper.updateById(eventInfo);
                }
            }
        }
    }

}

