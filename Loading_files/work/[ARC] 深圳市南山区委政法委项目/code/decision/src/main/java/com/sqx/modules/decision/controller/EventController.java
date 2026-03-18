package com.sqx.modules.decision.controller;

import com.sqx.common.exception.*;
import com.sqx.common.utils.*;
import com.sqx.modules.decision.entity.event.*;
import com.sqx.modules.decision.entity.third.*;
import com.sqx.modules.decision.service.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.util.*;

@RestController
@RequestMapping(value = "/event")
@Api(tags = {"事件接口"})
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/eventAuditList")
    @ApiOperation(value = "事件审核列表")
    public Result<EventListOut> eventAuditList(@RequestHeader("Authorization") String userToken,
                                               @RequestBody(required = false) EventListParam paramVo,
                                                HttpServletResponse response
                                               ) {
        UserInfo userInfo1 = eventService.getUserInfo(userToken,response);
        paramVo.setUserInfo(userInfo1);
        EventListOut eventListOut = eventService.eventAuditList(paramVo);
        return new Result<>().success(eventListOut);
    }


    @PostMapping("/eventAuditHandle")
    @ApiOperation(value = "事件审核操作 ")
    public Result eventAuditHandle(@RequestHeader("Authorization") String userToken,@RequestBody(required = false) EventAuditParam paramVo) {
        UserInfo userInfo = JwtToken.getUserInfo(userToken);
        userInfo.setToken(userToken);
        paramVo.setUserInfo(userInfo);
        eventService.eventAuditHandle(paramVo);
        return new Result<>().success();
    }

    @PostMapping("/todayEventAttention")
    @ApiOperation(value = "首页今日关注列表")
    public Result<EventListOut> todayEventAttention(@RequestHeader("Authorization") String userToken,
                                                    @RequestBody(required = false) EventListParam paramVo,
                                                    HttpServletResponse response) {
        UserInfo userInfo1 = eventService.getUserInfo(userToken,response);
        paramVo.setUserInfo(userInfo1);
        EventListOut eventListOut = eventService.todayEventAttention(paramVo);
        return new Result<>().success(eventListOut);
    }

    @PostMapping("/attentionOrCancle")
    @ApiOperation(value = "关注/取消关注")
    public Result attentionOrCancle(@RequestHeader("Authorization") String userToken,@RequestBody(required = false) EventAttentionParam paramVo) {
        UserInfo userInfo = JwtToken.getUserInfo(userToken);
        userInfo.setToken(userToken);
        paramVo.setUserInfo(userInfo);
        eventService.attentionOrCancle(paramVo);
        return new Result<>().success();

    }

    @PostMapping("/saveEventComment")
    @ApiOperation(value = "保存事件批示、反馈")
    public Result eventComment(@RequestHeader("Authorization") String userToken,HttpServletResponse response,@RequestBody(required = false) EventCommentParam paramVo) {
        UserInfo userInfo1 = eventService.getUserInfo(userToken,response);
        paramVo.setUserInfo(userInfo1);
        eventService.eventComment(paramVo);
        return new Result<>().success();
    }

    @PostMapping("/myAttentionList")
    @ApiOperation(value = "我的关注列表")
    public Result<EventListOut> myAttentionList(@RequestHeader("Authorization") String userToken) {
        UserInfo userInfo = JwtToken.getUserInfo(userToken);
        EventListOut myAttentionList = eventService.myAttentionList(userInfo);
        return new Result<>().success(myAttentionList);
    }


    @PostMapping("/myCommentList")
    @ApiOperation(value = "我的批示列表")
    public Result<EventListOut> myCommentList(@RequestHeader("Authorization") String userToken,@RequestBody(required = false) EventCommentParam paramVo) {
        UserInfo userInfo = JwtToken.getUserInfo(userToken);
        paramVo.setUserInfo(userInfo);
        EventListOut eventListOut = eventService.myCommentList(paramVo);
        return new Result<>().success(eventListOut);
    }

    @PostMapping("/leaderCommentList")
    @ApiOperation(value = "领导批示")
    public Result<EventListOut> leaderCommentList(@RequestHeader("Authorization") String userToken,
                                                  @RequestBody(required = false) EventCommentParam paramVo,
                                                  HttpServletResponse response) {
        UserInfo userInfo1 = eventService.getUserInfo(userToken,response);
        paramVo.setUserInfo(userInfo1);
        EventListOut eventListOut = eventService.leaderCommentList(paramVo);
        return new Result<>().success(eventListOut);
    }


    @PostMapping("/attentionSum")
    @ApiOperation(value = "关注概览")
    public Result<AttentionSum> attentionSum(@RequestHeader(value="Authorization") String userToken,
                                             @RequestBody(required = false) EventCommentParam paramVo,
                                             HttpServletResponse response) {
        UserInfo userInfo1 = eventService.getUserInfo(userToken,response);
        paramVo.setUserInfo(userInfo1);
        AttentionSum attentionSum = eventService.attentionSum(paramVo);
        return new Result<>().success(attentionSum);
    }

    @PostMapping("/getUserInfo")
    @ApiOperation(value = "获取用户信息")
    public Result<UserInfo> getUserInfo(@RequestHeader("Authorization") String userToken ,HttpServletResponse response) {
        try {
            UserInfo userInfo1 = eventService.getUserInfo(userToken,response);
            return new Result<>().success(userInfo1);
        }catch (Exception e){
            throw new SqxException("token失效",401);
        }
    }

   /* @PostMapping("/getAllDepartment")
    @ApiOperation(value = "获取所有组织架构")
    public Result getAllDepartment(@RequestHeader("Authorization") String userToken) {
        UserInfo userInfo = JwtToken.getUserInfo(userToken);
        userInfo.setToken(userToken);
        List<Department> departments = eventService.getAllDepartment(userInfo);
        return new Result<>().success(departments);
    }*/

    @PostMapping("/getUserNum")
    @ApiOperation(value = "首页人口统计")
    public Result<UserNumCal> getUserNum(@RequestHeader("Authorization") String userToken,@RequestBody(required = false) UserCalParam paramVo,HttpServletResponse response) {
        UserInfo userInfo = eventService.getUserInfo(userToken,response);
        userInfo.setToken(userToken);
        paramVo.setUserInfo(userInfo);
        UserNumCal departments = eventService.getUserNum(paramVo);
        departments.setDate(DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN));
        return new Result<>().success(departments);
    }


    @PostMapping("/getDistrict")
    @ApiOperation(value = "分区下拉框")
    public Result<DistrictOut> getDistrict(@RequestHeader("Authorization") String userToken) {
        UserCalParam paramVo = new UserCalParam();
        UserInfo userInfo = JwtToken.getUserInfo(userToken);
        userInfo.setToken(userToken);
        paramVo.setUserInfo(userInfo);
//        DistrictOut departments = eventService.getDistrict(paramVo);
        DistrictOut departments = new DistrictOut();
        List<District> districtList = new ArrayList<>();
        District district = new District();
        district.setDepartmentCode("440305");
        district.setDepartmentName("南山区");
        districtList.add(district);
        departments.setDistrictList(districtList);
        return new Result<>().success(departments);
    }

    @PostMapping("/receiveDpt")
    @ApiOperation(value = "反馈部门接口")
    public Result<ReceiveDpt> receiveDpt(@RequestHeader("Authorization") String userToken,HttpServletResponse response) {
        UserInfo userInfo1 = eventService.getUserInfo(userToken,response);
        ReceiveDpt departments = eventService.receiveDpt(userInfo1);
        return new Result<>().success(departments);
    }

    @PostMapping("/hasNewEvent")
    @ApiOperation(value = "审核是否有新数据(是否显示红点)")
    public Result<HaveNewEventOut> hasNewEvent(@RequestHeader("Authorization") String userToken, HttpServletResponse response) {
        UserInfo userInfo1 = eventService.getUserInfo(userToken,response);
        HaveNewEventOut haveNewEventOut = eventService.hasNewEvent(userInfo1);
        return new Result<>().success(haveNewEventOut);
    }

    @PostMapping("/eventDetail")
    @ApiOperation(value = "事件详情-调用第三方接口")
    public Result<EventDetailOut> eventDetailFromThird(@RequestHeader("Authorization") String userToken,
                                                       HttpServletResponse response,
                                                       @RequestBody(required = false) EventDetailParam paramVo) {
        UserInfo userInfo = JwtToken.getUserInfo(userToken);
        paramVo.setUserInfo(userInfo);
        EventDetailOut eventDetailOut = eventService.eventDetailFromThird(userToken,paramVo);
        return new Result<>().success(eventDetailOut);
    }

    /*@PostMapping("/eventDetailOld")
    @ApiOperation(value = "事件详情 传id")
    public Result<EventDetailOut> eventDetail(@RequestHeader("Authorization") String userToken,@RequestBody(required = false) EventDetailParam paramVo) {
        UserInfo userInfo = JwtToken.getUserInfo(userToken);
        return new Result<>().success(eventService.eventDetail(paramVo));
    }*/






}
