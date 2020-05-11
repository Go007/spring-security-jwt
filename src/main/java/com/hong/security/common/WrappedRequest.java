package com.hong.security.common;

import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author wanghong
 * @date 2020/05/11 22:47
 **/
public class WrappedRequest extends HttpServletRequestWrapper {

    private byte[] requestBody = null;

    public WrappedRequest(HttpServletRequest request) {
        super(request);
        // 缓存请求body
        try {
            requestBody = StreamUtils.copyToByteArray(request.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (requestBody == null) {
            requestBody = new byte[0];
        }
        final ByteArrayInputStream bais = new ByteArrayInputStream(requestBody);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                // TODO Auto-generated method stub
                return true;
            }

            @Override
            public boolean isReady() {
                // TODO Auto-generated method stub
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {
                // TODO Auto-generated method stub

            }

        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

}
