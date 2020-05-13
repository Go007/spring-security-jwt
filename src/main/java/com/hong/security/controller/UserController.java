package com.hong.security.controller;

import com.alibaba.fastjson.JSONObject;
import com.hong.security.bean.User;
import com.hong.security.common.Constants;
import com.hong.security.common.Result;
import com.hong.security.common.RoleType;
import com.hong.security.service.UserService;
import com.hong.security.util.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author wanghong
 * @date 2020/05/11 21:22
 **/
@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * web--用户登录
     *
     * @param paramMap Constants.PARAM_USER_DEVICEID  设备id不能为空
     *                 Constants.PARAM_USER_NAME  ^((13[0-9])|(15[0-9])|(18[0-9])|(17[0-9])|(14[0-9]))\d{8}$   无效的用户名
     *                 Constants.PARAM_USER_PWD   密码不能为空
     * @param request
     * @return
     */
    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    @ResponseBody
    public Result<?> login(@RequestBody Map<String, String> paramMap, HttpServletRequest request) {
        Result<JSONObject> result = new Result<>();
        try {
            String ipAddress = IPUtils.getClientIP(request);
            paramMap.put(Constants.PARAM_USER_ROLES, RoleType.ROLE_USER.roleName());// 普通用户
            paramMap.put(Constants.PARAM_IP, ipAddress);
            Result<User> userResult = userService.userLogin(paramMap);
            if (Result.CODE_FAILURE == userResult.getCode()) {
                return userResult;
            }
            JSONObject jsonObject = new JSONObject();
            String accessToken = userResult.getData().getToken();
            jsonObject.put("accessToken", accessToken);
            result.setData(jsonObject);
        } catch (Exception e) {
            log.error("用户登录异常[{}]", paramMap, e);
            return new Result<>(-1, "用户登录失败");
        }
        return result;
    }


    /**
     * 注册
     *
     * @param paramMap 设备id
     *                 手机号
     *                 短信校验码
     *                 密码
     * @param request
     * @return
     */
    @RequestMapping(value = "/register", method = {RequestMethod.POST})
    public Result<?> register(@RequestBody Map<String, String> paramMap, HttpServletRequest request) {
        Result<JSONObject> result = new Result<>();
        try {
            JSONObject jsonObject = new JSONObject();
            String accessToken = StringUtils.EMPTY;
            String userName = StringUtils.EMPTY;
            String mobile = StringUtils.EMPTY;
            String userPic = StringUtils.EMPTY;
            String nickName = StringUtils.EMPTY;
            String userId = StringUtils.EMPTY;
            String ipAddress = IPUtils.getClientIP(request);
            String androidChannel = request.getHeader(Constants.PARAM_ANDROID_CHANNEL);
            paramMap.put(Constants.PARAM_IP, ipAddress);
            paramMap.put(Constants.PARAM_USER_ROLES, RoleType.ROLE_USER.roleName());// 普通用户注册
            paramMap.put(Constants.PARAM_ANDROID_CHANNEL, androidChannel);//增加应用市场渠道
            Result<User> regResult = userService.userRegister(paramMap);
            if (Result.CODE_FAILURE == regResult.getCode()) {//出错直接返回
                return regResult;
            }
            User regUser = new User();
            if (regResult.getData() != null) {
                regUser = regResult.getData();
                accessToken = regUser.getToken();
                userId = String.valueOf(regUser.getId());
            }
            if (StringUtils.isNotBlank(regUser.getLoginName())) {
                userName = regUser.getLoginName();
            }

            if (StringUtils.isNotBlank(regUser.getMobile())) {
                mobile = regUser.getMobile();
            }
            if (StringUtils.isNotBlank(regUser.getUserPic())) {
                userPic = regUser.getUserPic();
            }
            if (StringUtils.isNotBlank(regUser.getNickName())) {
                nickName = regUser.getNickName();
            }
            jsonObject.put("accessToken", accessToken);
            jsonObject.put("userName", userName);
            jsonObject.put("mobile", mobile);
            jsonObject.put("userPic", userPic);
            jsonObject.put("nickName", nickName);
            jsonObject.put("userId", userId);
            result.setData(jsonObject);
        } catch (Exception e) {
            log.error("用户注册异常[{}]", paramMap, e);
            return new Result<>(-1, "注册用户失败");
        }
        return result;
    }

    /**
     * 用户登出
     * 设备ID
     * @param paramMap
     * @return
     */
    @RequestMapping(value = "/logout", method = { RequestMethod.POST })
    public Result<?> logout(@RequestBody Map<String, String> paramMap) {
        return userService.userLogout(paramMap);
    }

}
