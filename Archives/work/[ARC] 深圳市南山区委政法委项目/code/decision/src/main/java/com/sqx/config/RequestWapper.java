package com.sqx.config;

import com.sqx.common.utils.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.nio.charset.*;

public class RequestWapper extends HttpServletRequestWrapper {
    //存储输入流数据
    private final byte[] body;
    public RequestWapper(HttpServletRequest request) {
        super(request);
        body = RequestParseUtil.getBodyString( request).getBytes(Charset.defaultCharset());
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }
}
