package com.hong.security.filter;

import com.alibaba.fastjson.JSONObject;
import com.hong.security.common.Constants;
import com.hong.security.common.Result;
import com.hong.security.common.WrappedRequest;
import com.hong.security.config.PropsConfig;
import com.hong.security.service.OauthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wanghong
 * @date 2020/05/11 22:02
 **/
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private PropsConfig propsConfig;

    @Autowired
    private OauthService oauthService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        System.out.println("JwtAuthenticationTokenFilter Begin");
        setAllowOrigin(request, response);

        String requestParams = StringUtils.EMPTY;
        String requestUrl = request.getRequestURI();
        boolean isPost = RequestMethod.POST.name().equals(request.getMethod());
        ServletRequest requestWrapper = new WrappedRequest(request);

        // 1、验证签名参数--签名校验开关打开,验签所有post方法参数
        if (propsConfig.checkSignOpen && isPost) {
            // 请求URL不在跳过验证签名url列表中
            if (!propsConfig.skipSignUrls.contains(requestUrl)) {
                String sign = request.getHeader(Constants.PARAM_SIGN);
                requestParams = readJSONString(requestWrapper);
                if (StringUtils.isBlank(sign)) {
                    log.info("签名为空:参数  [{}]" + requestParams);
                    accessDenied(response, "000403", "签名不能为空");
                    return;
                }

                // 签名：MD5 + 盐
                String checkSign = DigestUtils.md5Hex(requestParams + Constants.SECRET_SIGN_KEY);
                if (!checkSign.equals(sign)) {// 签名不一致
                    log.info("签名不一致:客户端签名:[{}],服务端生成的签名:[{}]", sign, checkSign);
                    accessDenied(response, "000403", "签名不一致");
                    return;
                }
            }
        }

        String requestMethod = request.getMethod();
        log.info("请求url:[{}],method:[{}],params:[{}]", requestUrl, requestMethod, requestParams);

        // 2、验证头部token--普通token,带权限token
        String deviceId = request.getHeader(Constants.PARAM_USER_DEVICEID);
        String userName = StringUtils.EMPTY;
        String roles = StringUtils.EMPTY;
        String authHeader = request.getHeader(Constants.TOKEN_HEADER);//头部的token信息,app有,web无
        if (propsConfig.checkTokenOpen && isPost) {// token校验开关打开
            if (!propsConfig.skipTokenUrls.contains(requestUrl)) {// 需要判断token才校验
                // App 校验token
                // 校验token格式start
                String token_prefix = Constants.TOKEN_PREFIX;
                if (StringUtils.isBlank(authHeader)) {// 判断头部是否带token参数
                    accessDenied(response, "000406", "头部token缺失");
                    return;
                } else {
                    if (!StringUtils.startsWith(authHeader, token_prefix.trim())) {
                        accessDenied(response, "000407", "token格式不正确");
                        return;
                    }
                }

                String tokenValue = StringUtils.trim(authHeader.replace(token_prefix.trim(), StringUtils.EMPTY));
                if (StringUtils.isBlank(tokenValue)) {
                    accessDenied(response, "000401", "token值不能为空");
                    return;
                }
                // 校验token格式end

                // 校验token是否存在
                final String clientToken = authHeader.substring(token_prefix.length()); // The part after "Bearer "
                Map<String, String> tokenParamMap = new HashMap<>();
                tokenParamMap.put(Constants.PARAM_USER_ACCESS_TOKEN, clientToken);
                Result<Map<String, String>> tokenResult = oauthService.validateToken(tokenParamMap);

            }
        }

    }

    private void setAllowOrigin(HttpServletRequest request, HttpServletResponse response) {
        //origin 来源
        String currOrigin = request.getHeader("origin");
        log.info("当前访问源:{}", currOrigin);

        //配置相关跨域请求
        List<String> allowUrls = propsConfig.allowOriginUrls;
        if (StringUtils.isNotBlank(currOrigin) && (allowUrls != null && allowUrls.size() > 0)) {
            for (String url : allowUrls) {
                if (StringUtils.equals(url.trim(), currOrigin.trim())) {
                    response.setHeader("Access-Control-Allow-Origin", currOrigin);
                    break;
                }
            }
        }

        if (propsConfig.isDevEnv && StringUtils.isNotBlank(currOrigin)) {//如果是本地开发环境,放开内网限制
            response.setHeader("Access-Control-Allow-Origin", currOrigin);
        }

        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, X-Custom-Header, Authorization,token,platform");
        //Access-Control-Max-Age 表明该响应的有效时间为 86400 秒，也就是 24 小时。在有效时间内，浏览器无须为同一请求再次发起预检请求
        response.setHeader("Access-Control-Max-Age", "86400");
    }

    public String readJSONString(ServletRequest request) {
        StringBuffer json = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                json.append(StringUtils.trim(line));
            }
        } catch (Exception e) {
            log.info("readJSONString方法异常:[{}]", request, e);
        }
        return json.toString();
    }

    /**
     * 自定义拒绝访问
     *
     * @param response
     * @param code
     * @param msg
     */
    public void accessDenied(HttpServletResponse response, String code, String msg) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", code);
            jsonObject.put("message", msg);
            jsonObject.put("data", StringUtils.EMPTY);
            jsonObject.put("result", new Result<>(Result.CODE_FAILURE, Result.MSG_FAILURE));
            response.getWriter().println(jsonObject);
        } catch (Exception e) {
            logger.info("accessDenied方法异常:[{}]", e);
        }
    }
}
