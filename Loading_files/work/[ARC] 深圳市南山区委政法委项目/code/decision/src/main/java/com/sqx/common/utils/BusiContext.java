package com.sqx.common.utils;


import com.sqx.common.exception.*;
import org.apache.commons.io.*;
import org.slf4j.*;

import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class BusiContext {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusiContext.class);
    private static final ThreadLocal<BusiContext> CONTEXTS = new ThreadLocal<BusiContext>();

    public static void set(HttpServletRequest request) {
        CONTEXTS.set(new BusiContext(request));
    }

    private String id;

    @SuppressWarnings("unused")
    private HttpServletRequest request;

    private Map<String, String> params = new ConcurrentHashMap<String, String>();

    private Map<String, String> headers = new ConcurrentHashMap<String, String>();

    private Map<String, Object> attrs = new ConcurrentHashMap<String, Object>();

    private String requestEncryptBody;

    private String requestBody;

    private String responseEncryptBody;

    private String responseBody;

    public static void remove() {
        CONTEXTS.remove();
    }

    public static BusiContext get() {
        return CONTEXTS.get();
    }

    private BusiContext(HttpServletRequest request) {
        this.id = UUID.randomUUID().toString().replace("-","");
        this.request = request;

        if (null == request) {
            return;
        }


        /*Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            headers.put(name, value);
        }*/
        String val = null;
        if ((val = request.getHeader("userToken")) != null) {
            headers.put("userToken", val);
        }

        if ((val = request.getHeader("version")) != null) {
            headers.put("version", val);
        }
        if ((val = request.getHeader("serviceType")) != null) {
            headers.put("serviceType", val);
        }
        if ((val = request.getHeader("Accept-Language")) != null) {
            headers.put("Accept-Language", val);
        }
        if ((val = request.getHeader("BRAND")) != null) {
            headers.put("BRAND", val);
        }
        if ((val = request.getHeader("MODEL")) != null) {
            headers.put("MODEL", val);
        }
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = request.getParameter(name);
            params.put(name, value);
        }
        InputStream is = null;
        ByteArrayOutputStream os = null;
        try {
            is = request.getInputStream();
            if (is != null) {
                os = new ByteArrayOutputStream();
                IOUtils.copy(is, os);
                String encoding = request.getCharacterEncoding() != null ? "utf-8" : request.getCharacterEncoding();
                this.requestBody = os.toString(encoding);

            }
        } catch (Exception e) {
            LOGGER.error("{} - 请求解析异常", this.getId(), e);
            throw new SqxException("request error");
        }finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(is);
        }
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getParam(String name) {
        return params.get(name);
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setAttr(String name, Object value) {
        if (value != null) {
            attrs.put(name, value);
        }
    }

    public Object getAttr(String name, Object defval) {
        Object retval = attrs.get(name);
        return retval == null ? defval : retval;
    }

    public Object getAttr(String name) {
        return attrs.get(name);
    }

    public String getRequestBody(boolean encrypt) {
        if (encrypt)
            return requestEncryptBody;

        return requestBody;
    }

    public String getRequestBody() {
        return getRequestBody(false);
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getResponseBody(boolean encrypt) {
        return responseBody;
    }

    public String getResponseBody() {
        return getResponseBody(true);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
