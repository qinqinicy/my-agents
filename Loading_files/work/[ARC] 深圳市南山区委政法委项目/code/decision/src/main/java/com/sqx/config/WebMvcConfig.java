package com.sqx.config;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
         registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");

       registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApiInterceptor())
                .addPathPatterns("/event/**")
                .addPathPatterns("/key/**")
                .addPathPatterns("/index/**")
                .excludePathPatterns("/thirdPart/**")
                .excludePathPatterns("/event/eventDetailFromThird")
                /*.excludePathPatterns("/doc.html/**")
                .excludePathPatterns("/swagger-resources/**")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/v2/**")
                .excludePathPatterns("/favicon.ico")
                .excludePathPatterns("/swagger-ui.html/**")*/;

        registry.addInterceptor(new ThirdInterceptor())
                .addPathPatterns("/thirdPart/**")
                /*.excludePathPatterns("/thirdPart/getToken")
                .excludePathPatterns("/doc.html/**")
                .excludePathPatterns("/swagger-resources/**")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/v2/**")
                .excludePathPatterns("/favicon.ico")
                .excludePathPatterns("/swagger-ui.html/**")*/;


    }




}
