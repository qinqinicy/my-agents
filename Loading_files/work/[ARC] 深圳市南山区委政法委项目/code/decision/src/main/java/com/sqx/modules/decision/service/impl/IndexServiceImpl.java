package com.sqx.modules.decision.service.impl;

import com.alibaba.fastjson.*;
import com.sqx.common.exception.*;
import com.sqx.common.utils.DateUtils;
import com.sqx.modules.decision.dao.*;
import com.sqx.modules.decision.entity.event.*;
import com.sqx.modules.decision.entity.key.*;
import com.sqx.modules.decision.service.*;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.*;
import org.apache.http.impl.client.*;
import org.apache.http.util.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.io.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;

import java.io.*;
import java.net.*;
import java.util.*;

@Service
public class IndexServiceImpl implements IndexService {
    private Logger logger = LoggerFactory.getLogger(IndexServiceImpl.class);

    //用户信息map
    HashMap<String, UserInfo> userInfoHashMap = new HashMap<>();

    //所有部门
    HashMap<String, List<Department>> departmentHashMap = new HashMap<>();

    //人口所有数据
    HashMap<String, List<UserNum>> allUserNums = new HashMap<>();

    //区域人口缓存
    HashMap<String, List<UserNum>> areaUserNum = new HashMap<>();


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
    private EventService eventService;

    @Value("${key.monitor.url}")
    private String keyMonitorUrl;

    @Value("${key.user.url}")
    private String keyUserURL;

    @Override
    public EventListOut eventAuditList(EventListParam paramVo) {
        Date date = DateUtils.addDateDays(DateUtils.stringToDate(paramVo.getEndDate(), DateUtils.DATE_PATTERN), 1);
        paramVo.setEndDate(DateUtils.format(date, DateUtils.DATE_PATTERN));
        List<Event> events = eventInfoMapper.selectEventList(paramVo);
        EventListOut eventListOut = new EventListOut();
        eventListOut.setEvents(events);
        return eventListOut;
    }

    @Override
    public KeyIndexOut key(KeyMonitorParam paramVo,UserInfo userInfo) {
        /*try {
            KeyIndexOut keyIndexOut = new KeyIndexOut();
            Integer fxNum = 0;
            List<MonitorClass> detailList = new ArrayList<>();
            ClassPathResource resource = new ClassPathResource("keyData.json");
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
                    Set<String> item = new HashSet<>();
                    for (List<String> strings : values) {
                        String type = strings.get(8);
                        item.add(type);
                    }
                    if (!CollectionUtils.isEmpty(item)) {
                        for (String s : item) {
                            MonitorClass monitorClass0 = new MonitorClass();
                            monitorClass0.setTitle(s);
                            List<MonitorItem> list0 = new ArrayList<>();
                            List<MonitorItem> list1 = new ArrayList<>();
                            for (List<String> strings : values) {
                                if (strings.get(8).equals(s)) {
                                    MonitorItem monitorItem0 = new MonitorItem();
                                    monitorItem0.setLabel(strings.get(5));
                                    String nameOrPercent = strings.get(6);
                                    if (nameOrPercent !=null && nameOrPercent.contains("%")) {
                                        monitorItem0.setValue(nameOrPercent);
                                    } else {
                                        monitorItem0.setValue(strings.get(7));
                                        monitorItem0.setDetail(nameOrPercent);
                                    }
                                    monitorItem0.setIsError(strings.get(4));
                                    if (strings.get(4)!=null && strings.get(4).equals("是")){
                                        fxNum++;
                                    }
                                    if (paramVo.getIsError() != null && paramVo.getIsError().equals("shi") && monitorItem0.getIsError().equals("是")) {
                                        list1.add(monitorItem0);
                                    } else {
                                        list0.add(monitorItem0);
                                    }
                                }
                            }
                            if (paramVo.getIsError() != null && paramVo.getIsError().equals("shi")) {
                                monitorClass0.setList(list1);
                                if (!CollectionUtils.isEmpty(list1)) {
                                    detailList.add(monitorClass0);
                                }
                            } else {
                                monitorClass0.setList(list0);
                                if (!CollectionUtils.isEmpty(list0)) {
                                    detailList.add(monitorClass0);
                                }
                            }
                        }
                        keyIndexOut.setFxNum(fxNum);
                        keyIndexOut.setDetailList(detailList);
                        return keyIndexOut;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;*/
        return getKeyDataFromUrl(paramVo,userInfo);
    }

    @Override
    public EventCalOut eventCal(UserInfo userInfo1) {
        EventCalParam paramVo = new EventCalParam();
        Date date = DateUtils.addDateDays(new Date(), 1);
        paramVo.setEndDate(DateUtils.format(date, DateUtils.DATE_PATTERN));
        paramVo.setBeginDate(DateUtils.format(new Date(), DateUtils.DATE_PATTERN));
        //部门处理
        String precinctCode = null;
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
        Integer area = null;//1=区审核员/区领导  2=街道审核员/街道领导
        boolean audit = false;
        if (!CollectionUtils.isEmpty(roleList)) {
            for (UserRole userRole : roleList) {
                if (userRole.getRoleName().contains("区")) {
                    area = 1;
                    paramVo.setArea(area);
                    paramVo.setDepartmentCode(precinctCode);
                    audit = true;
                    break;
                } else if (userRole.getRoleName().contains("街道")) {
                    area = 2;
                    paramVo.setArea(area);
                    paramVo.setDepartmentCode(communityCode);
                    audit = true;
                    break;
                }
            }
        }
        if (!audit) {
            return new EventCalOut();
        }
        Integer todayEventTotal = 0;
        Integer todayMaintainEventTotal = 0;
        Integer todayKeyEventTotal = 0;
        //查询今日所有事件
        List<Event> events = eventInfoMapper.todayEventCal(paramVo);
        if (!CollectionUtils.isEmpty(events)) {
            for (Event event : events) {
                todayEventTotal++;
                if (StringUtils.isNotBlank(event.getEventSourceClass()) && event.getEventSourceClass().equals("维稳事件")) {
                    todayMaintainEventTotal++;
                } else if (StringUtils.isNotBlank(event.getEventSourceClass()) && event.getEventSourceClass().equals("重点事件")) {
                    todayKeyEventTotal++;
                }
            }
        }
        EventCalOut eventCalOut = new EventCalOut();
        eventCalOut.setTodayEventTotal(todayEventTotal);
        eventCalOut.setTodayMaintainEventTotal(todayMaintainEventTotal);
        eventCalOut.setTodayKeyEventTotal(todayKeyEventTotal);
        return eventCalOut;
    }


    @Override
    public EventClassCalOut eventClassNum(EventCalParam paramVo) {
        UserInfo userInfo1 = paramVo.getUserInfo();
        if (StringUtils.isBlank(paramVo.getBeginDate()) && StringUtils.isBlank(paramVo.getEndDate())) {
            Date date = DateUtils.addDateDays(new Date(), 1);
            paramVo.setEndDate(DateUtils.format(date, DateUtils.DATE_PATTERN));
            paramVo.setBeginDate(DateUtils.format(new Date(), DateUtils.DATE_PATTERN));
        }
        if (StringUtils.isNotBlank(paramVo.getEndDate())) {
            Date date = DateUtils.addDateDays(DateUtils.stringToDate(paramVo.getEndDate(), DateUtils.DATE_PATTERN), 1);
            paramVo.setEndDate(DateUtils.format(date, DateUtils.DATE_PATTERN));
        }
        //部门处理
        String precinctCode = null;
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
        Integer area = null;//1=区审核员/区领导  2=街道审核员/街道领导
        boolean audit = false;
        if (!CollectionUtils.isEmpty(roleList)) {
            for (UserRole userRole : roleList) {
                if (userRole.getRoleName().contains("区")) {
                    area = 1;
                    paramVo.setArea(area);
                    paramVo.setDepartmentCode(precinctCode);
                    audit = true;
                    break;
                } else if (userRole.getRoleName().contains("街道")) {
                    area = 2;
                    paramVo.setArea(area);
                    paramVo.setDepartmentCode(communityCode);
                    audit = true;
                    break;
                }
            }
        }
        if (!audit) {
            return new EventClassCalOut();
        }
        //查询今日所有事件
        List<Event> events = eventInfoMapper.todayEventCal(paramVo);
        Set<String> eventClass = new HashSet<>();
        eventClass.add("维稳事件");
        eventClass.add("重点事件");
        eventClass.add("矛盾纠纷");
        eventClass.add("网格事件");
        eventClass.add("民生诉求");
        List<EventClassNum> eventClassNums = new ArrayList<>();
        for (String aClass : eventClass) {
            EventClassNum eventClassNum = new EventClassNum();
            eventClassNum.setEventSourceClass(aClass);
            Integer num = 0;
            if (!CollectionUtils.isEmpty(events)) {
                for (Event event : events) {
                    if (StringUtils.isNotBlank(event.getEventSourceClass()) && event.getEventSourceClass().equals(aClass)) {
                        num++;
                    }
                }
            }
            eventClassNum.setNum(num);
            eventClassNums.add(eventClassNum);
        }
        EventClassCalOut eventClassCalOut = new EventClassCalOut();
        eventClassCalOut.setEventClassNums(eventClassNums);
        return eventClassCalOut;
    }

    @Override
    public EventListOut eventList(EventCalParam paramVo) {
        if (StringUtils.isBlank(paramVo.getBeginDate()) && StringUtils.isBlank(paramVo.getEndDate())) {
            Date date = DateUtils.addDateDays(new Date(), 1);
            paramVo.setEndDate(DateUtils.format(date, DateUtils.DATE_PATTERN));
            paramVo.setBeginDate(DateUtils.format(new Date(), DateUtils.DATE_PATTERN));
        }
        if (StringUtils.isNotBlank(paramVo.getEndDate())) {
            Date date = DateUtils.addDateDays(DateUtils.stringToDate(paramVo.getEndDate(), DateUtils.DATE_PATTERN), 1);
            paramVo.setEndDate(DateUtils.format(date, DateUtils.DATE_PATTERN));
        }
        //部门处理
        String precinctCode = null;
        String communityCode = null;
        String departmentCode = paramVo.getUserInfo().getDepartmentCode();
        if (departmentCode.contains("-")) {
            String[] split = departmentCode.split("-");
            precinctCode = split[0];
            communityCode = split[0] + split[1];
        } else {
            precinctCode = departmentCode.substring(0, 6);
            communityCode = departmentCode.substring(0, departmentCode.length());
        }
        List<UserRole> roleList = paramVo.getUserInfo().getRoleList();
        Integer area = null;//1=区审核员/区领导  2=街道审核员/街道领导
        boolean audit = false;
        if (!CollectionUtils.isEmpty(roleList)) {
            for (UserRole userRole : roleList) {
                if (userRole.getRoleName().contains("区")) {
                    area = 1;
                    paramVo.setArea(area);
                    paramVo.setDepartmentCode(precinctCode);
                    audit = true;
                    break;
                } else if (userRole.getRoleName().contains("街道")) {
                    area = 2;
                    paramVo.setArea(area);
                    paramVo.setDepartmentCode(communityCode);
                    audit = true;
                    break;
                }
            }
        }
        EventListOut eventListOut = new EventListOut();
        if (!audit) {
            return eventListOut;
        }
        //查询今日所有事件
        List<Event> events = eventInfoMapper.todayEventCal(paramVo);
        eventListOut.setEvents(events);
        return eventListOut;
    }

    @Override
    public EventSelectOut relatedField() {
        Set<String> relatedField = new HashSet<>();
        Set<String> organizer = new HashSet<>();
        Set<String> eventClass = new HashSet<>();
        Set<String> eventSource = new HashSet<>();

        List<Event> events = eventInfoMapper.selectSelect();
        if (!CollectionUtils.isEmpty(events)) {
            for (Event event : events) {
                if (event==null){
                    return new EventSelectOut();
                }
                if (StringUtils.isNotBlank(event.getRelatedField())) {
                    relatedField.add(event.getRelatedField());
                }
                if (StringUtils.isNotBlank(event.getOrganizer())) {
                    organizer.add(event.getOrganizer());
                }
                if (StringUtils.isNotBlank(event.getEventClass())) {
                    eventClass.add(event.getEventClass());
                }
                if (StringUtils.isNotBlank(event.getEventSource())) {
                    eventSource.add(event.getEventSource());
                }
            }
        }
        EventSelectOut eventSelectOut = new EventSelectOut();
        eventSelectOut.setRelatedField(new ArrayList<>(relatedField));
        eventSelectOut.setOrganizer(new ArrayList<>(organizer));
        eventSelectOut.setEventSource(new ArrayList<>(eventSource));
        eventSelectOut.setEventClass(new ArrayList<>(eventClass));
        return eventSelectOut;
    }

    @Override
    public KeyUserOut keyUser(UserInfo userInfo) {
        /*try {
            KeyUserOut keyUserOut = new KeyUserOut();
            ClassPathResource resource = new ClassPathResource("keyUser.json");
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
                        String zdzrs = value.get(4);
                        keyUserOut.setZdzrs(zdzrs);
                        String byxzzdgzrysl = value.get(5);
                        keyUserOut.setByxzzdgzrysl(byxzzdgzrysl);
                        String zdrbyzjl = value.get(6);
                        keyUserOut.setZdrbyzjl(zdrbyzjl);
                        String byxzzdgzryzwngs = value.get(7);
                        keyUserOut.setByxzzdgzryzwngs(byxzzdgzryzwngs);
                        String byxzzdgzryzwzfs = value.get(8);
                        keyUserOut.setByxzzdgzryzwzfs(byxzzdgzryzwzfs);
                        String qbzdgzry = value.get(9);
                        keyUserOut.setQbzdgzry(qbzdgzry);
                        String qbzdgzryzwngs = value.get(10);
                        keyUserOut.setQbzdgzryzwngs(qbzdgzryzwngs);
                        String qbzdgzryzwzfs = value.get(11);
                        keyUserOut.setQbzdgzryzwzfs(qbzdgzryzwzfs);
                        String yhb = value.get(12);
                        keyUserOut.setYhb(yhb);
                        String ytb = value.get(13);
                        keyUserOut.setYtb(ytb);
                        String ngl = value.get(14);
                        keyUserOut.setNgl(ngl);
                        String byzfrs = value.get(15);
                        keyUserOut.setByzfrs(byzfrs);
                        String zssrs = value.get(16);
                        keyUserOut.setZssrs(zssrs);
                        String jrssrs = value.get(17);
                        keyUserOut.setJrssrs(jrssrs);
                        return keyUserOut;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;*/


        return getKeyUserFromUrl(userInfo);
    }

    KeyUserOut getKeyUserFromUrl(UserInfo userInfo) {
        String districtCode = userInfo.getDepartmentCode();
        KeyUserOut keyUserOut = new KeyUserOut();
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            URIBuilder builder1 = new URIBuilder(keyUserURL);
            if (StringUtils.isNotBlank(districtCode)) {
                builder1.addParameter("gridCode", districtCode);
            }
            URI uri1 = builder1.build();
            logger.info("url---" + uri1);
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
                            String zdzrs = value.get(4);
                            keyUserOut.setZdzrs(zdzrs);
                            String byxzzdgzrysl = value.get(5);
                            keyUserOut.setByxzzdgzrysl(byxzzdgzrysl);
                            String zdrbyzjl = value.get(6);
                            keyUserOut.setZdrbyzjl(zdrbyzjl);
                            String byxzzdgzryzwngs = value.get(7);
                            keyUserOut.setByxzzdgzryzwngs(byxzzdgzryzwngs);
                            String byxzzdgzryzwzfs = value.get(8);
                            keyUserOut.setByxzzdgzryzwzfs(byxzzdgzryzwzfs);
                            String qbzdgzry = value.get(9);
                            keyUserOut.setQbzdgzry(qbzdgzry);
                            String qbzdgzryzwngs = value.get(10);
                            keyUserOut.setQbzdgzryzwngs(qbzdgzryzwngs);
                            String qbzdgzryzwzfs = value.get(11);
                            keyUserOut.setQbzdgzryzwzfs(qbzdgzryzwzfs);
                            String yhb = value.get(12);
                            keyUserOut.setYhb(yhb);
                            String ytb = value.get(13);
                            keyUserOut.setYtb(ytb);
                            String ngl = value.get(14);
                            keyUserOut.setNgl(ngl);
                            String byzfrs = value.get(15);
                            keyUserOut.setByzfrs(byzfrs);
                            String zssrs = value.get(16);
                            keyUserOut.setZssrs(zssrs);
                            String jrssrs = value.get(17);
                            keyUserOut.setJrssrs(jrssrs);
                            return keyUserOut;
                        }
                    } else {
                        logger.error("调取失败：" + resultString, response.getStatusLine().getStatusCode());
                        throw new SqxException("调用重点人口接口失败");
                    }
                } else {
                    resultString = EntityUtils.toString(response.getEntity(), "utf-8");
                    logger.error("调取失败：" + resultString, response.getStatusLine().getStatusCode());
                    throw new SqxException("调用重点人口接口失败");
                }
            }
        } catch (Exception e) {
            logger.error("调用重点人口接口失败", e);
        }
        return keyUserOut;
    }

    KeyIndexOut getKeyDataFromUrl(KeyMonitorParam paramVo,UserInfo userInfo) {
        KeyIndexOut keyIndexOut = new KeyIndexOut();
        Integer fxNum = 0;
        List<MonitorClass> detailList = new ArrayList<>();
       /* //获取当前街道
        UserInfo userInfo = paramVo.getUserInfo();
        String departmentCode = userInfo.getDepartmentCode();
        String districtCode = null;
        if (departmentCode.contains("-")) {
            String[] split = departmentCode.split("-");
            districtCode = split[0] + split[1];
        } else {
            districtCode = departmentCode.substring(0, departmentCode.length());
        }*/
        String departmentCode = paramVo.getDepartmentCode();
        if (StringUtils.isBlank(departmentCode)){
            List<RoleData> roleDataList = userInfo.getRoleDataList();
            for (RoleData roleData : roleDataList) {
                if (roleData.getRoleName().contains("决策分析")) {
                    departmentCode= roleData.getPrecinctCode();
                    break;
                }
            }
        }
        URI uri = null;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            URIBuilder builder1 = new URIBuilder(keyMonitorUrl);
            if (StringUtils.isNotBlank(departmentCode)) {
                builder1.addParameter("gridCode", departmentCode);
            }
            URI uri1 = builder1.build();
            uri = uri1;
            logger.info("url---" + uri1);
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
                        Set<String> item = new HashSet<>();
                        for (List<String> strings : values) {
                            String type = strings.get(8);
                            item.add(type);
                        }
                        if (!CollectionUtils.isEmpty(item)) {
                            for (String s : item) {
                                MonitorClass monitorClass0 = new MonitorClass();
                                monitorClass0.setTitle(s);
                                List<MonitorItem> list0 = new ArrayList<>();
                                List<MonitorItem> list1 = new ArrayList<>();
                                for (List<String> strings : values) {
                                    if (strings.get(8).equals(s)) {
                                        MonitorItem monitorItem0 = new MonitorItem();
                                        monitorItem0.setLabel(strings.get(5));
                                        String nameOrPercent = strings.get(6);
                                        if (StringUtils.isNotBlank(nameOrPercent) && nameOrPercent.contains("%")) {
                                            monitorItem0.setValue(nameOrPercent);
                                        } else {
                                            monitorItem0.setValue(strings.get(7));
                                            monitorItem0.setDetail(nameOrPercent);
                                        }
                                        monitorItem0.setIsError(strings.get(4));
                                        if (strings.get(4) != null && strings.get(4).equals("是")) {
                                            fxNum++;
                                        }
                                        monitorItem0.setRuler(strings.get(9));
                                        monitorItem0.setUnit(strings.get(10));
                                        monitorItem0.setUrl(strings.get(11));
                                        if (paramVo.getIsError() != null && paramVo.getIsError().equals("shi") && monitorItem0.getIsError().equals("是")) {
                                            list1.add(monitorItem0);
                                        } else {
                                            list0.add(monitorItem0);
                                        }
                                    }
                                }
                                if (paramVo.getIsError() != null && paramVo.getIsError().equals("shi")) {
                                    monitorClass0.setList(list1);
                                    if (!CollectionUtils.isEmpty(list1)) {
                                        detailList.add(monitorClass0);
                                    }
                                } else {
                                    monitorClass0.setList(list0);
                                    if (!CollectionUtils.isEmpty(list0)) {
                                        detailList.add(monitorClass0);
                                    }
                                }
                            }
                            keyIndexOut.setFxNum(fxNum);
                            keyIndexOut.setDetailList(detailList);
                            return keyIndexOut;
                        }
                    }
                } else {
                    logger.error("调取失败：" + resultString, response.getStatusLine().getStatusCode());
                    throw new SqxException("调用重点检查接口失败");
                }
            } else {
                resultString = EntityUtils.toString(response.getEntity(), "utf-8");
                logger.error("调取失败：" + resultString, response.getStatusLine().getStatusCode());
                throw new SqxException("调用重点检查接口失败");
            }
        } catch (Exception e) {
            logger.error("调用重点监控失败{}",uri, e);
        }
        keyIndexOut.setFxNum(fxNum);
        return keyIndexOut;
    }

    public static void main(String[] args) {
        String configPath = "keyData.json";
        try {
            ClassPathResource resource = new ClassPathResource(configPath);
            byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String s = new String(bytes);
            // 解析JSON数据
            MonitorResource result = JSON.parseObject(s, new TypeReference<MonitorResource>() {
            });
            System.out.println(result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
