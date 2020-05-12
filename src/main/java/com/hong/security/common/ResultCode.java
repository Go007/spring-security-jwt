package com.hong.security.common;

/**
 * @author wanghong
 * @date 2020/05/12 11:02
 **/
public enum ResultCode {
    // 基本错误码
    ILLEGAL_ARGS_ERR(-9999, "参数异常"),
    //oauth调用相关异常--服务调用异常 (710101-710199)
    APPLYTOKEN_FAILED(710101,"申请token失败"),
    VALIDATE_FAILED(710102,"token校验不通过"),
    TOKEN_EXPIRED(710103,"token已过期"),
    TOKEN_SIGNATURE_FAILED(710104,"token签名异常"),
    LOGIN_DEVICE_NOT_REG(710105,"登录设备未注册"),
    QUERY_USER_TOKEN_FAILED(710106,"查询用户token异常"),

    //熔断
    APPLYTOKEN_OVERTIME(710107, "申请token超时"),
    VALIDATETOKEN_OVERTIME(710108, "token校验超时"),
    QUERYUSERINFO_OVERTIME(710109, "查询用户信息超时"),


    VALIDATETOKEN_FAILED(710110, "校验token异常"),

    APPLY_OPENID_TOKEN_OVERTIME(710111, "申请openIdtoken超时"),
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
