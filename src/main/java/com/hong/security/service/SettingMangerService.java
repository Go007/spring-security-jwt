package com.hong.security.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hong.security.bean.LoginDeviceIdStatistic;
import com.hong.security.bean.LoginLog;
import com.hong.security.bean.LoginUserFlagStatistic;
import com.hong.security.common.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * app版本,广告,等杂七杂八的管理实现类
 *
 * @author wanghong
 * @date 2020/05/12 15:11
 **/
@Slf4j
@Service
public class SettingMangerService {
    /**
     * 获取设备登录次数
     *
     * @param params
     * @return
     */
    public Result<JSONObject> getLoginOrActiveTimes(Map<String, String> params) {
        Result<JSONObject> result = new Result<>();
        try {
            JSONObject jsonObject = new JSONObject();
            String certNo = params.get(Constants.PARAM_USER_CERTNO);
            String deviceId = params.get(Constants.PARAM_USER_DEVICEID);
            String userName = params.get(Constants.PARAM_USER_NAME);
            String userFlag = getUserFlag(userName, certNo);
            int oneDeviceLoginUserTimes = 0;
            int oneUserLoginDeviceTimes = 0;

            //默认为不阻止登录
            boolean preventOneDeviceLoginUserFlag = false;
            boolean preventOneUserFlagLoginDevice = false;

            // LoginDeviceIdStatistic loginDeviceIdStatistic = loginDeviceIdStatisticMapper.selectByDeviceId(deviceId);// 查询单个设备登录的账户个数

            LoginDeviceIdStatistic loginDeviceIdStatistic = new LoginDeviceIdStatistic();
            if (loginDeviceIdStatistic != null && loginDeviceIdStatistic.getLoginTimes() != null) {
                oneDeviceLoginUserTimes = loginDeviceIdStatistic.getLoginTimes();
            }

            if (loginDeviceIdStatistic != null && StringUtils.isNotBlank(loginDeviceIdStatistic.getLoginUserFlagDesc())) {
                String[] userFlagArr = loginDeviceIdStatistic.getLoginUserFlagDesc().split(",");
                //销售可以帮客户激活成功,但是不能让销售继续操作,不能登录,最前面的两个账号匹配允许其登录,否则阻止登录
                if (userFlagArr.length >= 2) {
                    if (StringUtils.contains(userFlagArr[0], userFlag) || StringUtils.contains(userFlagArr[1], userFlag)) {
                        preventOneDeviceLoginUserFlag = false;
                    } else {
                        preventOneDeviceLoginUserFlag = true;
                    }
                    //preventOneDeviceLoginUserFlag = !Arrays.asList(userFlagArr).contains(userFlag);
                }
            }

            // LoginUserFlagStatistic loginUserFlagStatistic = loginUserFlagStatisticMapper.selectByUserFlag(userFlag);// userFlag
            LoginUserFlagStatistic loginUserFlagStatistic = new LoginUserFlagStatistic();

            if (loginUserFlagStatistic != null && loginUserFlagStatistic.getLoginTimes() != null) {
                oneUserLoginDeviceTimes = loginUserFlagStatistic.getLoginTimes();
            }

            if (loginUserFlagStatistic != null && StringUtils.isNotBlank(loginUserFlagStatistic.getLoginDeviceIdDesc())) {
                //从登录当天往前一年内只能5个设备登录
                long currTime = System.currentTimeMillis();
                Date lastYearToday = new Date(); // DateUtil.todayAfterYear(-1);
                long startTime = 0L; // DateUtil.getMiliseconds(lastYearToday);
                JSONArray jsonArray = JSONArray.parseArray(loginUserFlagStatistic.getLoginDeviceIdDesc());
                //在范围之内登录次数
                int loginCount = 0;
                //是否包含自身,包含自身让其登录
                boolean containSelf = false;
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject eachJsonobj = jsonArray.getJSONObject(i);
                    for (Map.Entry<String, Object> entry : eachJsonobj.entrySet()) {
                        //数据组织之前不太合理,遍历所有的key,只需要读取设备id的key,排除其余的
                        if (!StringUtils.equals(entry.getKey(), Constants.PARAM_TYPE)) {
                            long loginTime = Long.valueOf(String.valueOf(entry.getValue()));
                            //在一年这个范围之内
                            if ((loginTime > startTime && loginTime < currTime)) {
                                loginCount++;
                                if (entry.getKey().equals(deviceId)) {
                                    containSelf = true;
                                }
                            }
                        }
                    }
                }

                if (!containSelf) {
                    //不包含,去判断是否超过5次限制,如果超过阻止
                    if (loginCount >= 5) {
                        preventOneUserFlagLoginDevice = true;
                    }
                }
            }

            jsonObject.put(Constants.PARAM_DEVICEID_LOGINUSER_TIMES, oneDeviceLoginUserTimes);
            jsonObject.put(Constants.PARAM_USER_LOGINONDEVICEID_TIMES, oneUserLoginDeviceTimes);

            jsonObject.put(Constants.PARAM_PREVENT_ONEDEVICE_LOGINUSERFLAG, preventOneDeviceLoginUserFlag);
            jsonObject.put(Constants.PARAM_PREVENT_ONEUSERFLAG_LOGINDEVICE, preventOneUserFlagLoginDevice);
            result.setData(jsonObject);
        } catch (Exception e) {
            log.error("获取设备登录统计信息异常[{}]", params, e);
            return new Result<>(ResultCode.SYS_GET_LOGIN_TIMES_FAIL.getCode(), ResultCode.SYS_GET_LOGIN_TIMES_FAIL.getMsg());
        }
        return result;
    }

    /**
     * 添加绑定个日志,事务,增加设备登录或者记录
     */
    public Result<Boolean> addLoginOrActiveLog(Map<String, String> params) {
        Result<Boolean> result = new Result<>();
        try {
            // 需要先查询一次
            addDeviceLoginLog(params);
        } catch (Exception e) {
            log.error("插入登录日志记录异常[{}]", params, e);
            return new Result<>(ResultCode.SYS_ADD_LOGIN_LOG_FAIL.getCode(), ResultCode.SYS_ADD_LOGIN_LOG_FAIL.getMsg());
        }
        return result;
    }

    private String getUserFlag(String userName, String certNo) {
        return String.format("%s#%s", userName, certNo);
    }

    /**
     * 添加登录流水记录
     * */
    private void addDeviceLoginLog(Map<String, String> params) {
        String certNo = params.get(Constants.PARAM_USER_CERTNO);
        String deviceId = params.get(Constants.PARAM_USER_DEVICEID);
        String userId = params.get(Constants.PARAM_USER_ID);
        String userName = params.get(Constants.PARAM_USER_NAME);
        String ip = params.get(Constants.PARAM_IP);
        String platform=params.get(Constants.PARAM_PLATFORM);
        String type=params.get(Constants.PARAM_TYPE);

        BindType bindType=BindType.from(Integer.parseInt(type));
        LoginLog loginLog = new LoginLog();
        loginLog.setUserId(Long.parseLong(userId));
        loginLog.setName(userName);
        loginLog.setCertNo(certNo);
        loginLog.setDeviceId(deviceId);
        //设置类型,1登录,2激活
        loginLog.setType(bindType.value());
        loginLog.setCreateTime(0L); //DateUtil.getCurrMiliseconds()
        loginLog.setStatus(DataEnableType.ENABLE.value());
        loginLog.setRemark(StringUtils.EMPTY);
        loginLog.setIp(ip);
        loginLog.setPlatform(platform);
        int effctRow = 1; //bindLogMapper.insert(loginLog);// 插入绑定流水表
        if (effctRow > 0) {
            updateDeviceIdStatisLog(deviceId, certNo, StringUtils.EMPTY, userName,bindType);
            updateUserFlagStatisLog(certNo, deviceId, StringUtils.EMPTY, userName,bindType);
        }
        // 操作缓存 移除缓存操作
    }

    /**
     * 设备登录分析表日志 将身份证和用户名做为一个用户标识(userFlag:李三#430381199906187452), 1个设备ID最多只能成功注册或登录2个用户
     * */
    private void updateDeviceIdStatisLog(String deviceId, String certNo, String remark, String userName,BindType bindType) {
        LoginDeviceIdStatistic deviceStats = new LoginDeviceIdStatistic(); //loginDeviceIdStatisticMapper.selectByDeviceId(deviceId);
        long currTime = System.currentTimeMillis();
        int bindTimes = 1;
        String userFlag = getUserFlag(userName, certNo)+"#"+bindType.value();
        String simpleUserFlag=getUserFlag(userName, certNo);
        // 判断记录是否存在--不存在insert--存在(并且之前没有过记录)update绑定次数
        if (deviceStats == null) {
            deviceStats = new LoginDeviceIdStatistic();
            deviceStats.setDeviceId(deviceId);
            deviceStats.setLoginTimes(bindTimes);
            deviceStats.setLoginUserFlagDesc(userFlag);
            deviceStats.setRemark(remark);
            deviceStats.setCreateTime(currTime);
            deviceStats.setUpdateTime(currTime);
            deviceStats.setStatus(DataEnableType.ENABLE.value());
           // loginDeviceIdStatisticMapper.insert(deviceStats);
        } else {
            String[] userFlagArr = deviceStats.getLoginUserFlagDesc().split(",");
            //兼容以前数据,以前未加#bindType标识区分
            if (!Arrays.asList(userFlagArr).contains(userFlag) && !Arrays.asList(userFlagArr).contains(simpleUserFlag)) {
                StringBuilder stringBuilder = new StringBuilder();
                bindTimes += deviceStats.getLoginTimes();
                stringBuilder.append(deviceStats.getLoginUserFlagDesc()).append(",").append(userFlag);
                deviceStats.setLoginUserFlagDesc(stringBuilder.toString());
                deviceStats.setLoginTimes(bindTimes);
                deviceStats.setUpdateTime(currTime);
              //  loginDeviceIdStatisticMapper.updateByPrimaryKey(deviceStats);
            }
        }
    }

    /**
     * 用户分析表日志  将身份证和用户名做为一个用户标识(userFlag) 一年之内5个 ,    1个用户1年之内最多可在5个设备ID上登录
     * */
    private void updateUserFlagStatisLog(String certNo, String deviceId, String remark, String userName,BindType bindType) {
        String userFlag = getUserFlag(userName, certNo);
        LoginUserFlagStatistic userFlagStatistic = new LoginUserFlagStatistic(); // loginUserFlagStatisticMapper.selectByUserFlag(userFlag);
        int bindTimes = 1;
        long currTime = System.currentTimeMillis();
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonObject=new JSONObject();
        //json 数据格式 一年之前数据删除掉 避免数据过大  [{deviceId:time}]
        if (userFlagStatistic == null) {
            userFlagStatistic = new LoginUserFlagStatistic();

//            jsonObject.put(deviceId, currTime);
//            jsonObject.put("type", bindType.value());
            setJoson(jsonObject, deviceId, currTime, bindType);

            jsonArray.add(jsonObject);
            userFlagStatistic.setUserFlag(userFlag);
            userFlagStatistic.setLoginTimes(bindTimes);
            userFlagStatistic.setLoginDeviceIdDesc(jsonArray.toString());
            userFlagStatistic.setCreateTime(currTime);
            userFlagStatistic.setUpdateTime(currTime);
            userFlagStatistic.setStatus(DataEnableType.ENABLE.value());
           // loginUserFlagStatisticMapper.insert(userFlagStatistic);
        } else {
            //如果 有update 时间 没有 添加操作
            jsonArray=JSONArray.parseArray(userFlagStatistic.getLoginDeviceIdDesc());
            boolean isExist=false;
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject eachJsonobj=jsonArray.getJSONObject(i);
                if(StringUtils.isNotBlank(eachJsonobj.getString(deviceId))) {//存在 update
//                    eachJsonobj.put(deviceId, currTime);
//                    eachJsonobj.put("type", bindType.value());
                    setJoson(eachJsonobj, deviceId, currTime, bindType);
                    isExist=true;
                }
            }
            if(!isExist) {
                setJoson(jsonObject, deviceId, currTime, bindType);
                jsonArray.add(jsonObject);
            }
            userFlagStatistic.setLoginTimes(jsonArray.size());
            userFlagStatistic.setLoginDeviceIdDesc(jsonArray.toString());
            userFlagStatistic.setUpdateTime(currTime);//每次需要更改
           // loginUserFlagStatisticMapper.updateByPrimaryKey(userFlagStatistic);
        }
    }

    private void setJoson(JSONObject jsonObject,String deviceId,long currTime,BindType bindType) {
        jsonObject.put(deviceId, currTime);
        jsonObject.put(Constants.PARAM_TYPE, bindType.value());
    }
}
