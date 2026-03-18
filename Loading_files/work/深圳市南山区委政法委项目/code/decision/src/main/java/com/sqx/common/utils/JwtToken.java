package com.sqx.common.utils;

import com.sqx.common.exception.*;
import com.sqx.modules.decision.entity.event.*;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.*;

import java.util.*;

@Slf4j
public class JwtToken {
    public static UserInfo getUserInfo(String token){
        UserInfo userInfo1 = new UserInfo();
//        userInfo1.setToken(token);
        /*userInfo1.setUserId("d7b2465b2b624429ab214098a2d923a7");
        userInfo1.setUserName("吴锡涛");
        userInfo1.setDepartmentCode("440305002");
        userInfo1.setDepartmentName("南山街道");*/
        try {
            String SECRET_KEY = "Zggdszs-PansZfw-znvSjgzpt@#$%&*._2026";
            JwtParser parser = Jwts.parser().setSigningKey(SECRET_KEY);
            Claims claims = parser.parseClaimsJws(token)
                    .getBody();
            Object userId = claims.get("userId");
            log.info("UserId:" + userId);
            Object userName = claims.get("userName");
            log.info("UserName:" + userName);
            Object exp = claims.get("exp");
            if (userId!=null){
                userInfo1.setUserId((String) userId);
            }
            if (userName!=null){
                userInfo1.setUserName((String) userName);
            }
            if (exp!=null){
                long l = Long.valueOf((Integer)exp) * 1000;
                userInfo1.setExp(l);
            }
            //查询用户信息
            return userInfo1;
        }catch (Exception e){
            log.error("解析token错误",e);
            throw new SqxException("token失效！",401);
        }
    }

    public static void main(String[] args) {
        String  token ="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIiLCJpc3MiOiJsenp4IiwidXNlclR5cGUiOiJzeXN0ZW0iLCJ1c2VyTmFtZSI6Iua1gei9rOW5s-WPsCIsImV4cCI6MTczMTAzNzg2MCwidXNlcklkIjoiMWUzMWUzMTZkZTFiNWU0YWIxNmJhNGZmNDFhYzc0NjIiLCJpYXQiOjE3MzA5NTE0NjB9.a1XBkkpe1u2_WiWwGioSSj62nsX_jEq0boFEEy-df5Y";
        String SECRET_KEY = "Zggdszs-PansZfw-znvSjgzpt@#$%&*._2026";

        JwtParser parser = Jwts.parser().setSigningKey(SECRET_KEY);
        Claims claims = parser.parseClaimsJws(token).getBody();
        System.out.println("UserId:" + claims.get("userId"));
        System.out.println("UserName:" + claims.get("userName"));
    }


}
