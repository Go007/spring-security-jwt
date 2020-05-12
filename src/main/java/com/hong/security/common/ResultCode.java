package com.hong.security.common;

/**
 * @author wanghong
 * @date 2020/05/12 11:02
 **/
public enum ResultCode {
    SIGN_EMPTY(1000, "签名为空"),
    SIGN_DISAGREE(1001, "签名不一致"),
    TOKEN_DELETION(1002, "头部token缺失"),
    TOKEN_FORMAT_ERROR(1003, "token格式不正确"),
    TOKEN_EMPTY(1004, "token为空"),


    // 基本错误码
    ILLEGAL_ARGS_ERR(-9999, "参数异常"),
    //oauth调用相关异常--服务调用异常 (710101-710199)
    APPLYTOKEN_FAILED(710101, "申请token失败"),
    VALIDATE_FAILED(710102, "token校验不通过"),
    TOKEN_EXPIRED(710103, "token已过期"),
    TOKEN_SIGNATURE_FAILED(710104, "token签名异常"),
    LOGIN_DEVICE_NOT_REG(710105, "登录设备未注册"),
    QUERY_USER_TOKEN_FAILED(710106, "查询用户token异常"),

    //熔断
    APPLYTOKEN_OVERTIME(710107, "申请token超时"),
    VALIDATETOKEN_OVERTIME(710108, "token校验超时"),
    QUERYUSERINFO_OVERTIME(710109, "查询用户信息超时"),


    VALIDATETOKEN_FAILED(710110, "校验token异常"),

    APPLY_OPENID_TOKEN_OVERTIME(710111, "申请openIdtoken超时"),

    //========================================================================================
    // 基本错误码
    SYSTEM_EXCEPTION(-9998, "系统异常"),

    // sys相关错误码--用户管理错误码(500101-500199)
    SYS_ADDUSER_FAIL(500101, "添加用户失败"),
    SYS_USER_EXIST(500102, "用户已存在"),
    SYS_USERLOGIN_FAIL(500103, "用户登录失败"),
    SYS_USERPWD_ERROR(500104, "用户名或密码不正确"),
    SYS_USER_NOTEXIST(500105, "用户不存在"),
    SYS_VALIDATGE_MSG_FAIL(500106, "校验短信码失败"),
    SYS_VALIDATE_USER_TOKEN_FAIL(500107, "校验用户token失败"),
    SYS_VALIDATE_USER_REALNAME_CERNO_FAIL(500108, "校验姓名身份证失败"),
    SYS_REST_PWD(500109, "重置用户密码失败。"),
    SYS_REST_PWD_EXPIRE(500110, "本次随机校验码过期"),
    SYS_USER_NOT_ACTIVE(500111, "该用户暂未激活钱包"),
    SYS_USER_WALLET_LINK_FAIL(500112, "关联用户钱包失败"),
    SYS_USER_MODIFY_FNMOBILE(500113, "蜂鸟手机号和注册手机号不一致已经修改"),
    SYS_USER_NOT_MODIFY_FNMOBILE(500114, "蜂鸟手机号和注册手机号一致不用改"),
    SYS_USER_ACTIVED(500115, "用户已经激活钱包资质。"),

    //sys 熔断超时
    SYS_USERREG_OVERTIME(500116, "用户注册超时"),
    SYS_USERLOGIN_OVERTIME(500117, "用户登录超时"),
    SYS_APPLAY_TOKEN_OVERTIME(500118, "申请用户token超时"),
    SYS_CHECK_SMS_CODE_OVERTIME(500119, "校验短信码超时"),
    SYS_CHECK_USERINFO_OVERTIME(500120, "校验用户信息超时"),
    SYS_REST_USER_PWD_OVERTIME(500121, "重置用户登录密码超时"),
    SYS_LINK_WALLET_OVERTIME(500122, "激活用户钱包超时"),
    SYS_QUERY_USER_BY_IDPSERSON_OVERTIME(500123, "查询用户信息超时"),
    SYS_USER_WALLET_LINK_OVERTIME(500124, "关联用户钱包失败"),

    SYS_USER_IDPSERSON_NOTEXIST(500125, "idPerson未找到"),
    SYS_USER_CHECKUSER_FAIL(500126, "用户名或者身份证不正确"),

    SYS_USER_QUERY_OVERTIME(500127, "用户查询超时"),

    SYS_USER_LINKED(500128, "idPerson已经被关联"),
    SYS_APPLY_USER_TOKEN_FAIL(500129, "申请用户token失败"),
    SYS_USER_QUERY_FAIL(500130, "用户查询异常"),
    SYS_USER_QUERY_RAND_EXPIRE(500131, "本次随机校验码过期"),
    SYS_SYNC_FN_MOBILE_OVERTIME(500132, "同步蜂鸟手机号码超时"),
    SYS_SYNC_FN_MOBILE_FAIL(500133, "同步蜂鸟手机号码异常"),
    SYS_SYNC_FN_MOBILE_LINDED(500134, "手机号已关联其他用户"),
    SYS_USER_LOGIN_OTHER_DEVICE(500135, "该账号在其他设备登录,请重新登录"),
    SYS_USER_LOGOUT_FAIL(500136, "退出登录异常"),
    SYS_USER_LOGOUT_OVERTIME(500137, "退出登录超时"),
    SYS_USER_KICK_OVERTIME(500138, "用户是否踢出超时"),
    SYS_MOBILEQQ_ACTIVATEWALLET_FAIL(500139, "激活手Q钱包异常"),
    SYS_MOBILEQQ_ACTIVATEWALLET_OVERTIME(500140, "蜂鸟激活手Q钱包超时"),
    SYS_MOBILEQQ_CHECK_USERINFO_FAIL(500141, "手Q获取用户信息异常"),
    SYS_MOBILEQQ_CHECK_USERINFO_OVERTIME(500142, "手Q获取用户信息超时"),
    SYS_REST_PWD_EQUAL(500143, "密码与旧密码相同"),
    SYS_USER_UPDATE_FAIL(500144, "修改用户信息失败"),
    SYS_USER_UPDATE_OVERTIME(500145, "修改用户信息超时"),
    SYS_USER_FNMOBILE_LINKED(500146, "手机号码已经和其他idPerson被关联"),
    SYS_MOBILEQQ_REVEIVE_RESULT_OVERTIME(500147, "接收蜂鸟手Q激活结果超时"),
    SYS_MOBILEQQ_QUERY_FIAL(500148, "手Q查询已激活用户异常"),

    SYS_MOBILEQQ_RECEIVE_REPAYMENT_OVERTIME(500149, "接收已还款数据超时"),
    SYS_MOBILEQQ_RECEIVE_EXPIRE_OVERTIME(500150, "接收逾期数据回传超时"),
    SYS_MOBILEQQ_RECEIVE_RECORDREVOCATION_OVERTIME(500151, "接收负面撤销数据超时"),

    SYS_GET_AD_OVERTIME(500152, "获取广告超时"),
    SYS_GET_APPVERSION_OVERTIME(500153, "获取app版本超时"),
    SYS_ADD_LOGIN_LOG_FAIL(500154, "新增登录日志异常"),
    SYS_ADD_LOGIN_LOG_OVERTIME(500155, "新增登录日志超时"),
    SYS_GET_LOGIN_TIMES_OVERTIME(500156, "获取登录次数信息超时"),
    SYS_GET_LOGIN_TIMES_FAIL(500157, "获取登录次数信息异常"),

    SYS_DEVICE_LOGIN_USER_TIMES_FAIL(500158, "单个设备最多只能登录或激活2个用户"),
    SYS_USER_LOGIN_DEVICE_TIMES_FAIL(500159, "单个用户1年之内最多可在5个设备上登录或激活"),

    SYS_GET_APPVERSION_FAIL(500160, "获取app版本失败"),
    SYS_GET_AD_FAIL(500161, "获取广告失败"),

    SYS_GET_REPAYMENT_FAIL(500162, "获取还款地址异常"),
    SYS_GET_REPAYMENT_OVERTIME(500163, "获取还款地址超时"),
    SYS_MOBILEQQ_REVEIVE_HISTORY_OVERTIME(500164, "接收蜂鸟历史数据结果超时"),
    SYS_MOBILEQQ_REVEIVE_APPLYSUCCESS_OVERTIME(500165, "接收蜂鸟成功数据结果超时"),
    SYS_MOBILEQQ_REVEIVE_APPLYREFUSED_OVERTIME(500166, "接收蜂鸟拒绝数据结果超时"),

    SYS_WXMALL_USERREG_OVERTIME(500167, "微信商城用户注册超时"),
    SYS_WXMALL_USERLOGIN_OVERTIME(500168, "微信商城用户登录超时"),

    SYS_GET_MAINTENANCE_FAIL(500169, "获取维护通知失败"),
    SYS_GET_MAINTENANCE_OVERTIME(500170, "微信维护通知超时"),

    //sys相关错误码--发送短信错误码(500201-500299)
    /**
     * 错误码-验证码过期
     */
    SYS_CHKVALICODE_EMPTY(500201, "验证码已过期,请重新获取"),
    SYS_CHKVALICODE_NULL(500202, "未找到该类型的验证码信息"),

    /**
     * 错误码-验证码校验失败
     */
    SYS_CHKVALICODE_FAIL(500202, "短信验证码校验失败,请重新确认"),

    /**
     * 错误码-短信发送异常
     */
    SYS_SENDBYSMSBIZ_FAIL(500203, "短信发送异常"),

    /**
     * 错误代码-短信发送接口调用超时
     */
    SYS_SENDSMS_OVERTIME(500204, "短信发送接口调用超时"),

    //sys相关错误码--发送邮件错误码  (500301-500399)
    /**
     * 错误码-邮件发送失败
     */
    SYS_SENDMAIL_FAIL(500301, "邮件发送失败"),

    /**
     * 错误代码-邮件发送接口调用超时
     */
    SYS_SENDMAIL_OVERTIME(500302, "邮件发送接口调用超时"),


    //sys相关错误码--消息相关错误码 (500401-500499)

    /**
     * app消息推送失败
     */
    SYS_APP_MSG_SEND_FAIL(500402, "app消息推送失败"),

    /**
     * app消息推送超时
     */
    SYS_APP_MSG_SEND_OVERTIME(500403, "app消息推送超时"),

    /**
     * app消息推送失败
     */
    SYS_MSG_READ_FAIL(500404, "阅读消息失败"),

    /**
     * app消息推送超时
     */
    SYS_MSG_READ_FAIL_OVERTIME(500405, "阅读消息超时"),

    /**
     * 获取pushId 失败
     */
    SYS_ADD_PUSH_ID_FAIL(500406, "添加推送Id失败"),

    /**
     * 添加pushId 异常
     */
    SYS_ADD_PUSH_ID_OVERTIME(500407, "添加推送Id超时"),

    /**
     * 获取消息列表 失败
     */
    SYS_GET_MSG_LIST_FAIL(500408, "获取消息列表失败"),

    /**
     * 添加pushId 异常
     */
    SYS_GET_MSG_LIST_OVERTIME(500409, "获取消息列表超时"),

    /**
     * idpserson不一致需要更改
     */
    SYS_IDPSERON_NOT_EQUAL(500410, "假激活idPseron未变更,需重新登录"),

    /**
     * 消息id不存在
     */
    SYS_MSG_NO_NOT_EXIST(500411, "消息编号不存在,请检查"),

    /**
     * pushId不存在
     */
    SYS_PUSH_ID_NOT_EXIST(500412, "设备的registerId不存在,请检查"),

    /**
     * 白名单不存在
     */
    SYS_WHITE_LIST_NO_EXISTS(500500, "白名单不存在"),

    SYS_WHITE_LIST_FAIL(500501, "添加白名单失败"),
    ;

    private int code;

    private String msg;

    private ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
