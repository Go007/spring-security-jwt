package com.hong.security.service;

import com.hong.security.common.Constants;
import com.hong.security.common.Result;
import com.hong.security.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wanghong
 * @date 2020/05/12 10:59
 **/
@Slf4j
@Service
public class OauthService {

    private String splitKey = "||";

    public Result<Map<String, String>> validateToken(@RequestBody Map<String, String> params) {
        Result<Map<String, String>> result = new Result<>();
        try {
            String token = StringUtils.trim(params.get(Constants.PARAM_USER_ACCESS_TOKEN));
            String oauths = StringUtils.EMPTY;
            String userName = StringUtils.EMPTY;
            String deviceId = StringUtils.EMPTY;
            String cachedToken = StringUtils.EMPTY;
            String cachedAccessTokenVal =  "" ;// TODO redisManager.getStr(token);// 直接从缓存中取
            if (StringUtils.isBlank(cachedAccessTokenVal)) {
                return new Result<>(ResultCode.TOKEN_EXPIRED.getCode(), ResultCode.TOKEN_EXPIRED.getMsg());
            }
            String[] simpleInfoArr = StringUtils.split(cachedAccessTokenVal, splitKey);
            cachedToken = simpleInfoArr[0];
            userName = simpleInfoArr[1];
            deviceId = simpleInfoArr[2];
            oauths = simpleInfoArr[3];
            Map<String, String> map = new HashMap<>();
            map.put(Constants.TOKEN_HEADER, cachedToken);
            map.put(Constants.AUTHORITIES, oauths);
            map.put(Constants.DEVICE_ID, deviceId);
            map.put(Constants.LOGIN_NAME, userName);
            result.setData(map);
        } catch (Exception e) {
            log.error("校验token异常[{}]", params, e);
            return new Result<>(ResultCode.VALIDATETOKEN_FAILED.getCode(), ResultCode.VALIDATETOKEN_FAILED.getMsg());
        }
        return result;
    }

}
