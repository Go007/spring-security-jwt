package com.hong.security.service;

import com.alibaba.fastjson.JSON;
import com.hong.security.bean.User;
import com.hong.security.common.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wanghong
 * @date 2020/05/12 14:09
 **/
@Slf4j
@Service
public class UserService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private SettingMangerService settingMangerService;

    @Autowired
    private OauthService oauthService;

    /**
     * 用户注册
     * 步骤:参数校验--检查注册短信发送次数--检查验证码是否正确(缓存中)--检查用户是否存在--注册--发送短信
     * <pre>
     *  {
     *    userName=18129865237,
     *    smsCode=34555,
     *    roles=USER,
     *    clientIp=183.16.194.73,
     *    password=%$8908xdf3@#.csd
     *  }
     * </pre>
     */
    public Result<User> userRegister(Map<String, String> params) {
        Result<User> result = new Result<>();
        try {
            String mobile = params.get(Constants.PARAM_USER_MOBILE);
            String randCode = params.get(Constants.PARAM_IDENTIFY_CODE);
            String deviceId = params.get(Constants.PARAM_USER_DEVICEID);
            // 1、检查随机码是否正确
            String cacheRandCode = redisService.getStr(Constants.REDIS_KEY_USER_QUERY_CODE + mobile + randCode);
            if (!StringUtils.equals(randCode, cacheRandCode)) {// code校验通过--修改密码
                return new Result<>(ResultCode.SYS_USER_QUERY_RAND_EXPIRE.getCode(), ResultCode.SYS_USER_QUERY_RAND_EXPIRE.getMsg());
            }
            // 2、检查用户是否已经注册
            User queryUser = queryUserInfo(mobile);
            if (queryUser != null) {
                return new Result<>(ResultCode.SYS_USER_EXIST.getCode(), ResultCode.SYS_USER_EXIST.getMsg());
            }
            // 3、密码生成--身份证信息识别--获取ip--插入数据库--缓存用户信息
            User regUser = saveUserInfo(params, UserSaveType.INSERT);
            // 4、是否需要发送注册成功短信or推送注册成功信息

            // 5、设置登录态
            cacheUserLoginStatus(deviceId, regUser);
            // 6、设置登录token
            String token = getUserToken(params, regUser);
            regUser.setToken(token);
            result.setData(regUser);
            return result;
        } catch (Exception e) {
            log.error("添加用户异常[{}]", params, e);
            return new Result<>(ResultCode.SYS_ADDUSER_FAIL.getCode(), ResultCode.SYS_ADDUSER_FAIL.getMsg());
        }
    }

    /**
     * 切换终端登录，踢出设备
     *
     * @param params
     * @return
     */
    public Result<Boolean> deviceIdKicked(@RequestBody Map<String, String> params) {
        Result<Boolean> result = new Result<>();
        boolean isKicked = false;
        String deviceId = StringUtils.trim(params.get(Constants.PARAM_USER_DEVICEID));
        String lastKickDeviceId = redisService.getStr(Constants.REDIS_KEY_USER_LAST_KICK_DEVICE + StringUtils.trim(deviceId));
        if (StringUtils.isNotBlank(lastKickDeviceId)) {
            isKicked = true;
        }
        result.setData(isKicked);
        return result;
    }

    public Result<User> userLogin(@RequestParam Map<String, String> params) {
        try {

            // 检查账号密码不需要检查设备号--匹配成功后下发token
            String userName = params.get(Constants.PARAM_USER_NAME);
            String password = params.get(Constants.PARAM_USER_PWD);
            String deviceId = params.get(Constants.PARAM_USER_DEVICEID);
            String platform = params.get(Constants.PARAM_PLATFORM);
            String ip = params.get(Constants.PARAM_IP);
            String pushId = params.get(Constants.PARAM_PUSHID);

            User queryUser = queryUserInfo(userName);
            queryUser.setLoginName(userName);
            queryUser.setId(110L);
            queryUser.setRoles(RoleType.ROLE_USER.roleName());

            if (queryUser == null) {
                return new Result(ResultCode.SYS_USER_NOTEXIST.getCode(), ResultCode.SYS_USER_NOTEXIST.getMsg());
            }
  /*            if (!StringUtils.equals(encodePwd(password), queryUser.getPassword())) {
                return new Result(ResultCode.SYS_USERPWD_ERROR.getCode(), ResultCode.SYS_USERPWD_ERROR.getMsg());
            }

          // 1个设备ID最多只能成功登录2个用户,1个用户1年之内最多可在5个设备ID上登录 只限制 app
            if (queryUser.getStatus() == UserStatus.ACTIVE.value()) {//激活成功才做判断--避免影响后面流程 捕获异常
                try {
                    Map<String, String> loginTimesParams = new HashMap<>();
                    loginTimesParams.put(Constants.PARAM_USER_CERTNO, queryUser.getCertNo());
                    loginTimesParams.put(Constants.PARAM_USER_DEVICEID, deviceId);
                    loginTimesParams.put(Constants.PARAM_USER_NAME, queryUser.getRealName());
                    loginTimesParams.put(Constants.PARAM_USER_ID, String.valueOf(queryUser.getId()));
                    loginTimesParams.put(Constants.PARAM_IP, ip);
                    loginTimesParams.put(Constants.PARAM_PLATFORM, platform);
                    loginTimesParams.put(Constants.PARAM_TYPE, String.valueOf(BindType.LOGIN.value()));
                    Result<JSONObject> loginTimeResult = settingMangerService.getLoginOrActiveTimes(loginTimesParams);

                    if (loginTimeResult != null) {
                        JSONObject loginJsonObject = loginTimeResult.getData();
                        boolean preventOneDeviceLoginUserFlag = loginJsonObject.getBooleanValue(Constants.PARAM_PREVENT_ONEDEVICE_LOGINUSERFLAG);
                        boolean preventOneUserFlagLoginDevice = loginJsonObject.getBooleanValue(Constants.PARAM_PREVENT_ONEUSERFLAG_LOGINDEVICE);
                        // String type=WhiteListTypePrefixEnum.SYS.getPrefix()+LOGIN_WHITE_LIST_SUFFIX;
                        String type = ""; // WhiteListEnum.LOGIN_WHITE.getType();
                        //被阻止--检查白名单
                        if (preventOneDeviceLoginUserFlag) {//一个设备id 2个登录账号
                            //在白名单中--跳过-否则-返回
                            if (!hitWhiteList(type, userName)) {
                                return new Result<>(ResultCode.SYS_DEVICE_LOGIN_USER_TIMES_FAIL.getCode(), ResultCode.SYS_DEVICE_LOGIN_USER_TIMES_FAIL.getMsg());
                            }
                        }
                        if (preventOneUserFlagLoginDevice) {//一个用户 5个设备id
                            //在白名单中--跳过-否则-返回
                            if (!hitWhiteList(type, userName)) {
                                return new Result<>(ResultCode.SYS_USER_LOGIN_DEVICE_TIMES_FAIL.getCode(), ResultCode.SYS_USER_LOGIN_DEVICE_TIMES_FAIL.getMsg());
                            }
                        }
                    }
                    settingMangerService.addLoginOrActiveLog(loginTimesParams);//插入流水
                } catch (Exception e) {
                    log.error("登录统计信息异常[{}]", params, e);
                }
            }

            //增加消息推送平台 id,有异常不影响主流程
            if (StringUtils.isNotBlank(pushId)) {
                Map<String, String> pushParam = new HashMap<>();
                pushParam.put(Constants.PARAM_PUSHID, pushId);
                pushParam.put(Constants.PARAM_PLATFORM, platform);
                pushParam.put(Constants.PARAM_USER_DEVICEID, deviceId);
                pushParam.put(Constants.PARAM_USER_ID, String.valueOf(queryUser.getId()));
                try {
                    //  messageService.addPushId(pushParam);//插入推送设备表中
                } catch (Exception e) {
                    log.error("插入推送设备表异常[{}]", pushParam, e);
                }
            }*/

            removeLastDeviceLoginInfo(deviceId, userName);

            cacheLastLoginDeviceId(userName, deviceId);//记录最后登录设备
            // 登录成功--设置登录态(缓存7天)EXPIRE_USER_LOGIN_STATUS_DAY
            cacheUserLoginStatus(deviceId, queryUser);
            //移除当前登录设备的踢出状态
            removeLastKickDeviceId(deviceId);


            // 登录成功下发token
            String oauths = queryUser.getRoles();
            String userId = String.valueOf(queryUser.getId());
            Map<String, String> map = new HashMap<>();
            map.put(Constants.PARAM_USER_NAME, userName);
            map.put(Constants.PARAM_USER_OAUTHS, oauths);
            map.put(Constants.PARAM_USER_ID, userId);
            map.put(Constants.PARAM_USER_DEVICEID, deviceId);
            if (StringUtils.isNotBlank(platform)) {
                map.put(Constants.PARAM_PLATFORM, platform);
            }
            Result<String> tokenResult = oauthService.applyToken(map);// 申请token
            if (Result.CODE_FAILURE == tokenResult.getCode()) {
                return new Result<>(ResultCode.SYS_APPLY_USER_TOKEN_FAIL.getCode(), ResultCode.SYS_APPLY_USER_TOKEN_FAIL.getMsg());
            }
            if (tokenResult != null && StringUtils.isNotBlank(tokenResult.getData())) {
                String applyToken = tokenResult.getData();
                queryUser.setToken(applyToken);
                log.info("用户名:[{}],设备号:[{}],申请的token:[{}]", userName, deviceId, tokenResult.getData());
            }

            return new Result(queryUser);
        } catch (Exception e) {
            log.error("用户登录异常[{}]", params, e);
            return new Result<>(ResultCode.SYS_USERLOGIN_FAIL.getCode(), ResultCode.SYS_USERLOGIN_FAIL.getMsg());
        }
    }

    /**
     * md5移动端传递过来密码
     */
    private String encodePwd(String orignPwd) {
        return DigestUtils.md5Hex(orignPwd + Constants.SECRET_KEY);
    }

    //命中白名单
    private boolean hitWhiteList(String type, String userName) {
        //命中白名单-返回true
        return true;
    }

    /**
     * 获取最后登录设备id
     */
    private String getLastLoginDeviceId(String userName) {
        String lastLoginDeviceKey = Constants.REDIS_KEY_USER_LAST_LOGIN_DEVICE + userName;
        return redisService.getStr(lastLoginDeviceKey);
    }

    /**
     * 保持只能在单台设备登录
     * 移除之前登录设备登录态
     * 移除之前登录设备登录token
     * 记录最后被踢出登录设备id
     * 当台设备登录之后需要移除自己的踢出状态
     */
    private void removeLastDeviceLoginInfo(String currDeviceId, String userName) {
        String lastLoginDeviceId = getLastLoginDeviceId(userName);//最后登录设备
        if (StringUtils.isNotBlank(lastLoginDeviceId) && !StringUtils.equals(lastLoginDeviceId, currDeviceId)) {// 如果两次的设备id不一致:说明用户在多处登录,需要移除以前的设备的登录态
            String lastLoginDeviceIdTokenKey = Constants.REDIS_KEY_USER_LOGIN_DEVICEID_TOKEN + lastLoginDeviceId;// key:设备id,value:token,最后登录设备申请的token
            String lastLoginDeviceIdToken = redisService.getStr(lastLoginDeviceIdTokenKey);// [写入值见OauthServiceImpl.cacheUserToken方法]
            redisService.del(Constants.REDIS_KEY_USER_LOGIN_STATUS + lastLoginDeviceId);// 移除之前设备登录态
            if (StringUtils.isNotBlank(lastLoginDeviceIdToken)) {
                redisService.del(lastLoginDeviceIdToken);// 移除之前设备id的token
            }
            log.info("用户:[{}],已切换登录设备:[{}],之前登录设备:[{}],登录态和该设备下登录token:[{}]被移除", userName, currDeviceId, lastLoginDeviceId, lastLoginDeviceIdToken);
            cacheLastKickDeviceId(lastLoginDeviceId);//记录最后那一台被踢出设备
        }
    }

    /**
     * 记录最后被踢出来的设备
     */
    private void cacheLastKickDeviceId(String deviceId) {
        String lastKickDeviceId = Constants.REDIS_KEY_USER_LAST_KICK_DEVICE + deviceId;
        redisService.set(lastKickDeviceId, lastKickDeviceId, Constants.EXPIRE_USER_LOGIN_STATUS_DAY);
    }

    /**
     * 缓存最后登录设备
     */
    private void cacheLastLoginDeviceId(String userName, String deviceId) {
        String lastLoginDeviceKey = Constants.REDIS_KEY_USER_LAST_LOGIN_DEVICE + userName;
        redisService.set(lastLoginDeviceKey, deviceId, Constants.EXPIRE_USER_LOGIN_STATUS_DAY);
    }

    /**
     * 缓存用户设备登录态
     */
    private void cacheUserLoginStatus(String deviceId, User queryUser) {
        redisService.set(Constants.REDIS_KEY_USER_LOGIN_STATUS + deviceId, JSON.toJSONString(queryUser), Constants.EXPIRE_USER_LOGIN_STATUS_DAY);
    }

    /**
     * 移除登录设备的踢出状态
     */
    private void removeLastKickDeviceId(String deviceId) {
        String lastKickDeviceId = Constants.REDIS_KEY_USER_LAST_KICK_DEVICE + deviceId;
        redisService.del(lastKickDeviceId);
    }

    private User queryUserInfo(String userName) {
        // 先查询缓存--缓存没有从数据库查询
        String cacheKey = generateCacheUserKey(userName);
        User queryUser = redisService.get(cacheKey, User.class);
        if (queryUser == null) {
            queryUser = new User();//userMapper.selectByEnableUserName(userName);
        }
        return queryUser;
    }

    /**
     * 生成缓存key:用户名称或者手机号码
     */
    private String generateCacheUserKey(String userName) {
        return Constants.REDIS_KEY_USER + userName;
    }

    private User saveUserInfo(Map<String, String> params, UserSaveType userSaveType) {
        String mobile = params.get(Constants.PARAM_USER_MOBILE);
        User regUser = new User(); // setUserInfo(params, userSaveType,UserStatus.ENABLE);
        int effectNum = 1;// userMapper.insert(regUser);// 用户注册
        if (effectNum > 0) {
            // 缓存到redis
            log.info("用户注册成功[{}]", regUser);
            String cacheKey = generateCacheUserKey(mobile);
            redisService.set(cacheKey, regUser, Constants.EXPIRE_USER_DAY);// 缓存注册用户信息
        }
        return regUser;
    }

    /**
     * 向oauth服务申请用户token
     */
    private String getUserToken(Map<String, String> params, User user) {
        params.put(Constants.PARAM_USER_TOKEN_TYPE, String.valueOf(TokenType.USER.type()));
        params.put(Constants.PARAM_USER_OAUTHS, user.getRoles());
        if (user.getId() != null) {
            params.put(Constants.PARAM_USER_ID, String.valueOf(user.getId()));
        }
        params.put(Constants.PARAM_USER_NAME, user.getLoginName());
        Result<String> result = oauthService.applyToken(params);
        if (Result.CODE_FAILURE == result.getCode()) {
            log.info("getUserToken申请token异常[{}]", result);
        }
        String token = result.getData();
        return token;
    }
}
