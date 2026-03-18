package com.sqx.common.utils;

import cn.hutool.crypto.digest.*;
import com.sqx.common.exception.*;
import com.sqx.modules.decision.entity.third.*;
import lombok.extern.slf4j.*;

import java.util.*;

@Slf4j
public class TokenUtil {


    private static String CLIENT_ID = "b90305e0e8449e9dc4e7d4e0005c25be";
    private static String SECRET_KEY = "212e241235e3c7c33bd87e573318a017";
    //9位随机数，判断请求是否是当次请求
    private static HashMap<Integer,Integer> randomMap = new HashMap<>();

    private static HashMap<Integer,Long> timeStmpMap = new HashMap<>();

    public static TokenResponse getToken(){
        Random random = new Random();
        int code = random.nextInt(900000000) + 100000000;
        log.info("请求随机code：{}",code);
        randomMap.put(code,code);
        long l = System.currentTimeMillis();
        timeStmpMap.put(code,l);
        String token = generateToken(code);
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setCode(code);
        tokenResponse.setToken(token);
        return tokenResponse;
    }

    public static String generateToken(int code) {
        String data = CLIENT_ID + SECRET_KEY + code;
        String token = MD5Util.encodeByMD5(data);
        return token;
    }

    public static boolean checkToken(String token,int code){
        if (randomMap.get(code)==null){
            throw new SqxException("code失效,请重新获取!");
        }
        if (token.equals(generateToken(code))){
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        BCrypt bCrypt = new BCrypt();
        String s = MD5Util.encodeByMD5("this is my SECRET_KEY");
        System.out.println(s);
    }

}
