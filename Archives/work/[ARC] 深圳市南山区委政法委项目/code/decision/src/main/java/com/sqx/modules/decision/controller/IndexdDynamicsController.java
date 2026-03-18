package com.sqx.modules.decision.controller;

import com.sqx.common.utils.*;
import com.sqx.modules.decision.entity.event.*;
import com.sqx.modules.decision.entity.key.*;
import com.sqx.modules.decision.entity.third.*;
import com.sqx.modules.decision.service.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;

@RestController
@RequestMapping(value = "/index")
@Api(tags = {"首页相关"})
public class IndexdDynamicsController extends BaseController{


    @Autowired
    private IndexService indexService;

    @Autowired
    private EventService eventService;

    @PostMapping("/key")
    @ApiOperation(value = "重点监测首页")
    public Result<KeyIndexOut> index(@RequestHeader("Authorization") String userToken,
                                     @RequestBody(required = false) KeyMonitorParam paramVo,
                                     HttpServletResponse response) {
        UserInfo userInfo = eventService.getUserInfo(userToken, response);
        paramVo.setUserInfo(userInfo);
        KeyIndexOut keyIndexOut = indexService.key(paramVo,userInfo);
        return new Result<>().success(keyIndexOut);
    }

    @PostMapping("/eventTodayCal")
    @ApiOperation(value = "事件总量统计")
    public Result<EventCalOut> eventTodayCal(@RequestHeader("Authorization") String userToken,HttpServletResponse response) {
        UserInfo userInfo1 = eventService.getUserInfo(userToken,response);
        EventCalOut eventCal = indexService.eventCal(userInfo1);
        return new Result<>().success(eventCal);
    }

    @PostMapping("/eventClassNum")
    @ApiOperation(value = "事件分类统计")
    public Result<EventClassCalOut> eventClassNum(@RequestHeader("Authorization") String userToken,
                                                  HttpServletResponse response,
                                                  @RequestBody(required = false) EventCalParam paramVo) {
        UserInfo userInfo1 = eventService.getUserInfo(userToken,response);
        paramVo.setUserInfo(userInfo1);
        EventClassCalOut eventCal = indexService.eventClassNum(paramVo);
        return new Result<>().success(eventCal);
    }

    @PostMapping("/eventList")
    @ApiOperation(value = "事件列表")
    public Result<EventListOut> eventList(@RequestHeader("Authorization") String userToken,
                                              @RequestBody(required = false) EventCalParam paramVo,
                                              HttpServletResponse response) {
        UserInfo userInfo1 = eventService.getUserInfo(userToken,response);
        paramVo.setUserInfo(userInfo1);
        EventListOut eventCal = indexService.eventList(paramVo);
        return new Result<>().success(eventCal);
    }



    @PostMapping("/dynamicEventList")
    @ApiOperation(value = "辖区动态调用第三方接口---事件列表")
    public Result<EventDynamicOut> dynamicEventList(@RequestHeader("Authorization") String userToken,
                                                   @RequestBody(required = false) DynamicEventParam paramVo) {
        UserInfo userInfo = JwtToken.getUserInfo(userToken);
        EventDynamicOut eventDetailOut = eventService.dynamicEventList(paramVo);
        return new Result<>().success(eventDetailOut);
    }

    @PostMapping("/saveDynamicEventComment")
    @ApiOperation(value = "辖区动态批示")
    public Result saveDynamicEventComment(@RequestHeader("Authorization") String userToken,
                                                          HttpServletResponse response,
                                                          @RequestBody(required = false) EventCommentParam1 paramVo) {
        UserInfo userInfo1 = eventService.getUserInfo(userToken,response);
        paramVo.setUserInfo(userInfo1);
        eventService.saveDynamicEventComment(paramVo);
        return new Result<>().success();
    }

    @PostMapping("/attentionOrCancle")
    @ApiOperation(value = "辖区动态事项 关注/取消关注")
    public Result attentionOrCancle(@RequestHeader("Authorization") String userToken,
                                    HttpServletResponse response,
                                    @RequestBody(required = false) EventAttentionParam1 paramVo) {
        UserInfo userInfo1 = eventService.getUserInfo(userToken,response);
        paramVo.setUserInfo(userInfo1);
        eventService.saveDynamicAttentionOrCancle(paramVo);
        return new Result<>().success();

    }


    @PostMapping("/commentListAttention")
    @ApiOperation(value = "事项批示列表 和 关注状态")
    public Result<EventDynamicCommentOut> commentListAttention(@RequestHeader("Authorization") String userToken,
                                    HttpServletResponse response,
                                    @RequestBody(required = false) DynamicEvent paramVo) {
        UserInfo userInfo1 = eventService.getUserInfo(userToken,response);
        EventDynamicCommentOut eventDynamicCommentOut = eventService.commentListAttention(paramVo);
        return new Result<>().success(eventDynamicCommentOut);

    }

    @PostMapping("/getEventTotal")
    @ApiOperation(value = "事辖区动态事项首页统计")
    public Result<DynamicEventTotal> getEventTotal(@RequestHeader("Authorization") String userToken,
                                                   HttpServletResponse response,
                                                   @RequestBody(required = false) DynamicEventTotalParam paramVo) {
        UserInfo userInfo = JwtToken.getUserInfo(userToken);
        DynamicEventTotal dynamicEventTotal = eventService.getEventTotal(paramVo);
        return new Result<>().success(dynamicEventTotal);

    }


    @PostMapping("/selectSelect")
    @ApiOperation(value = "涉及领域")
    public Result<EventSelectOut> relatedField(@RequestHeader("Authorization") String userToken,
                                               HttpServletResponse response) {
        UserInfo userInfo = JwtToken.getUserInfo(userToken);
        EventSelectOut relatedFieldOut = indexService.relatedField();
        return new Result<>().success(relatedFieldOut);
    }



    @PostMapping("/keyUser")
    @ApiOperation(value = "首页人相关")
    public Result<KeyUserOut> keyUser(@RequestHeader("Authorization") String userToken,
                                     HttpServletResponse response,
                                      @RequestBody(required = false) UserCalParam paramVo) {
        UserInfo userInfo = eventService.getUserInfo(userToken, response);
        userInfo.setDepartmentCode(paramVo.getDepartmentCode());
        KeyUserOut keyUser = indexService.keyUser(userInfo);
        return new Result<>().success(keyUser);
    }

}
