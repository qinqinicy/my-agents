package com.sqx.config;

import com.sqx.common.utils.*;
import lombok.extern.slf4j.*;

import javax.servlet.FilterConfig;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.*;

@Slf4j
//@WebFilter({"/thirdPart/*"})
public class ThirdRequestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("In Third Part Filter!");
        ServletRequest requestWapper = new RequestWapper((HttpServletRequest) servletRequest);
        BusiContext.set((HttpServletRequest) requestWapper);
        filterChain.doFilter(requestWapper,servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
