package com.sqx.common.utils;

import cn.hutool.core.date.*;
import com.alibaba.druid.support.spring.stat.*;
import io.jsonwebtoken.*;
import sun.java2d.pipe.*;

import java.util.*;

public class GetToken {

    //签发者
    private final static String JWT_ISSUER = "znv"; // 各家应用名称

    //秘钥（需要支持可配置，生产环境会不同）
    public static final String SECRET_KEY = "Zggdszs-PansZfw-znvSjgzpt@#$%&*._2026";

    //有效时间单位秒（需要支持可配置，生产环境会不同）
    public static final int EXPRIRE_TIME = 3600*12;


    public static  String createJwt(){
        String subject = "工单流转";
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId","dc63044a4647ec9b067282191d4c1df2");
        claims.put("userName","海致");
        claims.put("userType","system");
        claims.put("loginKind",0);


        String string = UUID.randomUUID().toString();
        Date date = new Date();
        JwtBuilder builder = Jwts.builder()
                .setId(string)
                .setClaims(claims)
                .setIssuer(JWT_ISSUER)
                .setIssuedAt(date).setSubject(subject).signWith(SignatureAlgorithm.HS256,SECRET_KEY);
        if (EXPRIRE_TIME > 0){
            long l = date.getTime() + EXPRIRE_TIME * 1000;
            Date expir = new Date(l);
            builder.setExpiration(expir);
        }
        return builder.compact();
    }


    public static void main(String[] args) {
        String token = createJwt();
        System.out.println(token);
    }

}
