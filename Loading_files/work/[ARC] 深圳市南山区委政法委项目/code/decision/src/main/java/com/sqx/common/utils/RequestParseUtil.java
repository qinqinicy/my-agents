package com.sqx.common.utils;

import org.springframework.http.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class RequestParseUtil {

    /**
     * 判断是否是json
     * @param request
     * @return
     */
    public static boolean isJson(HttpServletRequest request){
        if (request.getContentType()!=null){
            return request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE) || request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE);
        }
        return false;
    }

    public static String getBodyString(final ServletRequest servletRequest){
        try {
            return inputStreamToString(servletRequest.getInputStream());
        }catch (IOException e){
            throw new RuntimeException();
        }
    }

    private static String inputStreamToString(InputStream inputStream){
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line ;
                while ((line= reader.readLine()) !=null){
                    sb.append(line);
                }
        }catch (IOException e){
            throw new RuntimeException();
        }
        return sb.toString();
    }

}
