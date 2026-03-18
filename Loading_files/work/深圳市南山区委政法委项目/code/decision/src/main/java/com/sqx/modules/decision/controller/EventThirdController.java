package com.sqx.modules.decision.controller;

import com.sqx.common.utils.*;
import com.sqx.modules.decision.entity.event.*;
import com.sqx.modules.decision.entity.third.*;
import com.sqx.modules.decision.service.*;
import io.swagger.annotations.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/thirdPart")
@Api(tags = {"事件第三方接口"})
@Slf4j
public class EventThirdController {

    @Autowired
    private EventService eventService;



   /* @GetMapping("/getToken")
    @ApiOperation(value = "推送事件token获取")
    public OrderResult<TokenResponse> getToken(@RequestParam("client_id") String clientId) {
        TokenResponse tokenResponse = eventService.getToken(clientId);
        return new OrderResult<>().success(tokenResponse);
    }*/


    /*@PostMapping("/pushMatter")
    @ApiOperation(value = "推送事件")
    public Result pushEvent(@RequestHeader("Authorization") String userToken,@RequestBody(required = false) EventBasic eventBasic) {
        eventService.pushEvent(eventBasic);
        return new Result<>().success();
    }


    @PostMapping("/reciveDecisionProcess")
    @ApiOperation(value = "批示")
    public Result pushProcess(@RequestHeader("Authorization") String userToken,@RequestBody(required = false) EventProcessBasic eventProcessBasic) {
        eventService.pushProcess(eventProcessBasic);
        return new Result<>().success();
    }

    @PostMapping("/recivefeedbackProcess")
    @ApiOperation(value = "反馈")
    public Result reciveDecisionProcess(@RequestHeader("Authorization") String userToken,@RequestBody(required = false) EventProcessBasic eventProcessBasic) {
        eventService.pushProcess(eventProcessBasic);
        return new Result<>().success();
    }*/


   /* @PostMapping("/order/reciveDecisionProcess")
    @ApiOperation(value = "流转平台-工单流转")
    public Result pushOrderProcess(@RequestHeader("Authorization") String userToken,@RequestBody(required = false) OrderEventProcessBasic eventProcessBasic) {
        eventService.pushOrderProcess(eventProcessBasic);
        return new Result<>().success();
    }

*/

   /* @PostMapping("/order/recivefeedbackProcess")
    @ApiOperation(value = "流转平台推送事件处理流程")
    public Result reciveOrderFeedbackProcess(@RequestHeader("Authorization") String userToken,@RequestBody(required = false) OrderEventProcessBasic eventProcessBasic) {
        eventProcessBasic.setCommentOrFeed(2);
        eventService.pushOrderProcess(eventProcessBasic);
        return new Result<>().success();
    }*/



    @PostMapping("/order/pushMatter")
    @ApiOperation(value = "流转平台-推送事件信息")
    public OrderResult pushOrderEvent(@RequestHeader("Authorization") String userToken,@RequestBody(required = false) OrderEventBasic eventBasic) {
//        UserInfo userInfo = JwtToken.getUserInfo(userToken);
        eventService.pushOrderEvent(eventBasic);
        return new OrderResult<>().success();
    }

    /*@PostMapping("/order/pushMessage")
    @ApiOperation(value = "流转平台-推送事件信息")
    public OrderResult pushMessage(@RequestHeader("Authorization") String userToken,@RequestBody(required = false) OrderMessageBasic eventBasic) {
//        UserInfo userInfo = JwtToken.getUserInfo(userToken);
        eventService.pushMessage(eventBasic);
        return new OrderResult<>().success();
    }*/


}
