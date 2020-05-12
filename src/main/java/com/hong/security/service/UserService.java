package com.hong.security.service;

import com.hong.security.common.Constants;
import com.hong.security.common.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author wanghong
 * @date 2020/05/12 14:09
 **/
@Service
public class UserService {

    @Autowired
    private RedisService redisService;

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

}
