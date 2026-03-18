package com.sqx.config;

import org.springframework.context.annotation.*;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.*;
import springfox.documentation.spring.web.plugins.*;
import springfox.documentation.swagger2.annotations.*;

//@Configuration
//@EnableSwagger2
public class SwaggerConfig {
    /*public Docket UserApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("")
                .apiInfo(UserApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sqx.modules.decision.controller"))
                .paths(PathSelectors.any())
                .build().enable(false);
    }

    private ApiInfo UserApiInfo(){
        return new ApiInfoBuilder()
                .title("我的API文档") // 标题
                .description("本文档描述了用户相关的接口定义") // 描述
                .version("1.0") // 版本
                .contact(new Contact("联系人名字", "联系人访问链接", "联系人邮箱")) // 联系人信息
                .build();
    }*/


}
