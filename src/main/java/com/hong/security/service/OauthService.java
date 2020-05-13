package com.hong.security.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hong.security.bean.UserToken;
import com.hong.security.common.*;
import com.hong.security.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RedisService redisService;

    private String splitKey = "||";

    private int cacheTime = 60 * 60 * 2;//2小时

    public Result<Map<String, String>> validateToken(@RequestBody Map<String, String> params) {
        Result<Map<String, String>> result = new Result<>();
        try {
            String token = StringUtils.trim(params.get(Constants.PARAM_USER_ACCESS_TOKEN));
            String cachedAccessTokenVal = redisService.getStr(token);// 直接从缓存中取
            if (StringUtils.isBlank(cachedAccessTokenVal)) {
                return new Result<>(ResultCode.TOKEN_EXPIRED.getCode(), ResultCode.TOKEN_EXPIRED.getMsg());
            }

            String[] simpleInfoArr = StringUtils.split(cachedAccessTokenVal, splitKey);
            String cachedToken = simpleInfoArr[0];
            String userName = simpleInfoArr[1];
            String deviceId = simpleInfoArr[2];
            String oauths = simpleInfoArr[3];

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

    /**
     * 申请用户token
     *
     * <pre>
     *  {
     *    deviceId=0, 设备id不能为空
     *    userName=Jhon,
     *    oauths=Admin,
     *    userId=10,
     *    tokenType=1
     *  }
     * </pre>
     */
    public Result<String> applyToken(Map<String, String> params) {
        Result<String> result = new Result<>();
        try {
            String deviceId = params.get(Constants.PARAM_USER_DEVICEID);
            String ipAddress = params.get(Constants.PARAM_IP);
            String platform = params.get(Constants.PARAM_PLATFORM);
            /**
             * 获取登录态,有登录态:登录的用户,无登录态:访客
             */
            String oauths;
            String loginStatusKey = Constants.REDIS_KEY_USER_LOGIN_STATUS + deviceId;
            JSONObject cacheUserJson = JSON.parseObject(redisService.getStr(loginStatusKey)); //获取登录态
            if (cacheUserJson == null) {
                oauths = TokenType.GUEST.typeName();
            } else {
                oauths = cacheUserJson.getString(Constants.PARAM_USER_ROLES);
                redisService.set(loginStatusKey, JSON.toJSONString(cacheUserJson), Constants.EXPIRE_USER_LOGIN_STATUS_DAY);//延长设备登录态token时间
            }
            // 2、 生成用户token
            UserToken userToken = setUserTokenInfo(cacheUserJson, deviceId, ipAddress, platform);
            cacheUserToken(userToken, oauths, platform);//缓存token,严谨的化应该使用户下的token失效,避免redis存入过多数据
            log.info("用户[{}],设备号[{}],平台[{}],生成的token[{}]", userToken.getUserName(), userToken.getDeviceId(), platform, userToken.getAccessToken());
            result.setData(userToken.getAccessToken());
        } catch (Exception e) {
            log.error("申请token异常[{}]", params, e);
            return new Result<>(ResultCode.APPLYTOKEN_FAILED.getCode(), ResultCode.APPLYTOKEN_FAILED.getMsg());
        }
        return result;
    }

    private UserToken setUserTokenInfo(JSONObject cacheUser, String deviceId, String ipAddress, String platform) {
        String userName = null;
        long userId = 0;
        int tokenType;
        long currTime = 123456789L;  //DateUtil.getCurrMiliseconds();
        long expiressTime = 1234567845656759L; //DateUtil.getBeforeMin(2*60, new Date()).getTime();//过期时间2小时
        UserToken userToken = new UserToken();
        // 有这个用户才给分配token--存缓存
        if (cacheUser != null) {//分配用户token
            userName = cacheUser.getString("loginName");
            if (StringUtils.isBlank(userName)) {
                userName = cacheUser.getString("wxMallLoginName");
            }
            userId = cacheUser.getLong("id");
            tokenType = TokenType.USER.type();
        } else {
            userName = TokenType.GUEST.typeName();
            tokenType = TokenType.GUEST.type();
        }
        String accessToken = TokenUtil.createToken(userName, deviceId);
        String refreshToken = StringUtils.EMPTY;
        userToken.setUserId(userId);
        if (StringUtils.isBlank(userName)) {
            userName = TokenType.USER.typeName();
        }
        userToken.setUserName(userName);
        userToken.setAccessToken(accessToken);
        userToken.setRefreshToken(refreshToken);
        userToken.setDeviceId(deviceId);
        userToken.setStatus(TokenStatus.ENABLE.status());
        userToken.setType(tokenType);
        userToken.setCreateTime(currTime);
        userToken.setExpiresTime(expiressTime);//用户token默认缓存两个小时
        if (StringUtils.isNotBlank(ipAddress)) {
            userToken.setIpAddress(ipAddress);
        }
        if (StringUtils.isNotBlank(platform)) {
            userToken.setPlatform(platform);
        }
        return userToken;
    }

    /**
     * 缓存用户 or 访客token,2小时
     */
    private String cacheUserToken(UserToken userToken, String oauths, String platform) {
        String cacheValue = String.format("%s" + splitKey + "%s" + splitKey + "%s" + splitKey + "%s",
                userToken.getAccessToken(), userToken.getUserName(), userToken.getDeviceId(), oauths);// token缓存的格式:token||用户名||设备id||权限or角色
        //1、移除该设备id之前申请的token
        String currDeviceIdTokenKey = Constants.REDIS_KEY_USER_LOGIN_DEVICEID_TOKEN + userToken.getDeviceId();
        String currDeviceIdToken = redisService.getStr(currDeviceIdTokenKey);//最后设备下发的token
        if (StringUtils.isNotBlank(currDeviceIdToken) && !StringUtils.equals(platform, PlatformType.WECHATMALL.desc())) {// 微信302多次重定向,token会被覆盖,导致失效,故加这个判断
            redisService.del(currDeviceIdToken);
        }
        //2、缓存用户token
        // userTokenMapper.insert(userToken);
        redisService.set(userToken.getAccessToken(), cacheValue, cacheTime);// token存入缓存,时间2小时,放入配置文件中
        //3、记录当前设备下发的token
        redisService.set(currDeviceIdTokenKey, userToken.getAccessToken());
        return cacheValue;
    }

}
