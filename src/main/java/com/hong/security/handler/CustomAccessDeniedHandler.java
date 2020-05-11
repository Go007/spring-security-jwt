package com.hong.security.handler;

import com.alibaba.fastjson.JSONObject;
import com.hong.security.common.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author wanghong
 * @date 2020/05/11 21:48
 * 访问拒绝处理器
 **/
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();

            Result<String> result = new Result<>(Result.CODE_FAILURE, Result.MSG_FAILURE, request.getRequestURI());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", "000405");
            jsonObject.put("message", "您处于访客状态,请登录后再访问");
            jsonObject.put("data", StringUtils.EMPTY);
            jsonObject.put("result", result);

            out.print(jsonObject);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

}
