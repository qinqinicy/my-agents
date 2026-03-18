package com.sqx.config;

import com.google.gson.*;
import com.sqx.common.utils.*;
import com.sqx.modules.decision.entity.event.*;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.*;
import org.apache.commons.lang.*;
import org.springframework.stereotype.*;
import org.springframework.web.servlet.*;

import javax.servlet.http.*;
import java.util.*;


@Slf4j
public class ThirdInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("In My Third ApiInterceptor!");
        BusiContext busiContext = BusiContext.get();
        if (RequestParseUtil.isJson(request)) {
            String bodyString = RequestParseUtil.getBodyString(request);
            log.info("请求信息：{} | url:{} | token:{} | 请求参数：{}",
                    busiContext.getId(),
                    request.getRequestURI(),
                    request.getHeader("Authorization"),
                    bodyString);
        }
       /* // 在这里处理拦截逻辑
        String authorization = request.getHeader("Authorization");
        String code = request.getHeader("code");
        response.setContentType("application/json;charset=utf-8");
        if (StringUtils.isBlank(authorization) || StringUtils.isBlank(code)) {
            String json = new Gson().toJson(Result.error(401, "第三方请求token或code为空"));
            response.getWriter().write(json);
            return false;
        }
        //校验第三方token是否合法
        boolean b = TokenUtil.checkToken(authorization, Integer.valueOf(code));
        if (!b){
            String json = new Gson().toJson(Result.error(401, "token失效，请重新获取！"));
            response.getWriter().write(json);
            return false;
        }*/
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        BusiContext busiContext = BusiContext.get();
        log.info("返回信息：{} | {} | {}",
                busiContext.getId(),
                request.getRequestURI(),
                busiContext.getResponseBody()
        );
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
