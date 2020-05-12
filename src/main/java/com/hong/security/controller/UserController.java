package com.hong.security.controller;

import com.alibaba.fastjson.JSONObject;
import com.hong.security.bean.User;
import com.hong.security.common.Constants;
import com.hong.security.common.Result;
import com.hong.security.common.RoleType;
import com.hong.security.service.UserService;
import com.hong.security.util.IPUtils;
import lombok.extern.slf4j.Slf4j;
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
  //  @PreAuthorize("hasAnyAuthority('GUEST','USER')")
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

}
