package com.sqx.config;

import com.sqx.common.xss.XssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;

/**
 * Filter配置
 *
 */
@Configuration
public class FilterConfig {

    /*@Bean
    public FilterRegistrationBean shiroFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        //该值缺省为false，表示生命周期由SpringApplicationContext管理，设置为true则表示由ServletContainer管理
        registration.addInitParameter("targetFilterLifecycle", "true");
        registration.setEnabled(true);
        registration.setOrder(Integer.MAX_VALUE - 1);
        registration.addUrlPatterns("/*");
        return registration;
    }*/

    @Bean
    public FilterRegistrationBean requestWapperFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new RequestWapperFilter());
        registration.addUrlPatterns("/event/*","/key/*","/index/*");
        registration.setName("requestWapperFilter");
        registration.setOrder(1);//order越低优先级越高
        return registration;
    }

    /**
     * 第三方接口 过滤器
     */
    @Bean
    public FilterRegistrationBean thirdFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new ThirdRequestFilter());
        registration.addUrlPatterns("/thirdPart/*");
        registration.setName("thirdFilter");
        registration.setOrder(Integer.MAX_VALUE);
        return registration;
    }

}
