package com.sqx.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sqx.common.exception.*;
import com.sqx.common.utils.*;
import com.sqx.modules.decision.entity.event.*;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.*;
import org.apache.commons.io.*;
import org.apache.commons.lang.*;
import org.springframework.lang.*;
import org.springframework.stereotype.*;
import org.springframework.web.method.*;
import org.springframework.web.servlet.*;

import javax.servlet.http.*;
import java.io.*;
import java.util.*;


@Slf4j
public class ApiInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("In My ApiInterceptor!");
        BusiContext busiContext = BusiContext.get();
        if (RequestParseUtil.isJson(request)) {
            String bodyString = RequestParseUtil.getBodyString(request);
            String context_id = UUID.randomUUID().toString().replace("-", "");
            log.info("请求信息：{} | url:{} | token:{} | 请求参数：{}",
                    busiContext.getId(),
                    request.getRequestURI(),
                    request.getHeader("Authorization"),
                    bodyString);
        }
        // 在这里处理拦截逻辑
        String authorization = request.getHeader("Authorization");
        response.setContentType("application/json;charset=utf-8");
        ObjectMapper objectMapper = new ObjectMapper();
        if (StringUtils.isBlank(authorization)) {
//            response.addHeader("X-Log-Out","token expire time");
            try {
                String json = objectMapper.writeValueAsString(Result.error(401, "用户 token 为空"));
                response.getWriter().write(json);
            } catch (Exception e) {
                log.error("返回 JSON 错误", e);
            }
            return false;
        }
        UserInfo userInfo = null;
        try {
            //判断是否超时
            userInfo = JwtToken.getUserInfo(authorization);
        } catch (Exception e) {
            log.error("解析 token 错误：", e);
            if (e.getClass().equals(ExpiredJwtException.class)) {
                response.addHeader("X-Log-Out", "token expire time");
                try {
                    String json = objectMapper.writeValueAsString(Result.error(401, "token 失效"));
                    response.getWriter().write(json);
                } catch (Exception ex) {
                    log.error("返回 JSON 错误", ex);
                }
                return false;
            }
            response.addHeader("X-Log-Out", "token expire time");
            try {
                String json = objectMapper.writeValueAsString(Result.error(401, "token 失效"));
                response.getWriter().write(json);
            } catch (Exception ex) {
                log.error("返回 JSON 错误", ex);
            }
            return false;
        }
        long exp = userInfo.getExp();
        log.info("token 过期时间戳：" + exp);
        long time = new Date().getTime();
        log.info("当前时间戳：" + time);
        //过期时间小于当前时间，token 失效
        if (exp <= time) {
            response.addHeader("X-Log-Out", "token expire time");
            try {
                String json = objectMapper.writeValueAsString(Result.error(401, "token expire time"));
                response.getWriter().write(json);
            } catch (Exception ex) {
                log.error("返回 JSON 错误", ex);
            }
            return false;
        }
        //15 分钟刷新 token
        if ((exp - time) / 1000 / 60 <= 15) {
            response.addHeader("X-Refresh-Token", "token expiring in 15 minutes");
        }
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
