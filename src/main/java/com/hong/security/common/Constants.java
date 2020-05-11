package com.hong.security.common;

/**
 * @author wanghong
 * @date 2020/05/11 22:54
 **/
public class Constants {

    /*-----------------------------oauth服务相关-----------------------------*/
    /**
     * 用户名
     */
    public static final String PARAM_USER_NAME = "userName";

    /**
     * 微信商城用户名
     */

    public static final String PARAM_WX_MALL_USER_NAME = "wxMallUserName";

    /**
     * 用户ID
     */
    public static final String PARAM_USER_ID = "userId";

    /**
     * 用户设备号
     */
    public static final String PARAM_USER_DEVICEID = "deviceId";

    /**
     * accessToken
     */
    public static final String PARAM_USER_ACCESS_TOKEN = "accessToken";

    /**
     * refreshToken
     */
    public static final String PARAM_USER_REFRESH_TOKEN = "refreshToken";

    /**
     * 用户权限
     */
    public static final String PARAM_USER_OAUTHS = "oauths";

    /**
     * 对应数据库权限属性
     * */
    public static final String PARAM_USER_ROLES = "roles";

    /**
     * 用户token类型
     */
    public static final String PARAM_USER_TOKEN_TYPE = "tokenType";

    /**
     * 签名
     */
    public static final String PARAM_SIGN= "sign";

    /**
     * timestamp
     */
    public static final String PARAM_TIME_STAMP= "timestamp";

    /**
     *data
     */
    public static final String PARAM_DATA= "data";

    /**
     * ip
     */
    public static final String PARAM_IP= "ip";

    /**
     * 平台 android ios qq
     */
    public static final String PARAM_PLATFORM= "platform";

    /**
     * h5url
     * */
    public static final String PARAM_H5URL= "h5url";

    /**
     *  2腾讯QQ 3微信 默认为2
     * */
    public static final String PARAM_SOURCE= "source";

    /**
     * qq open_id
     */
    public static final String PARAM_OPENID= "openID";

    public static final String PARAM_QQ_WECHAT_OPENID="openId";

    /**
     * 第三方消息推送设备Id
     * */
    public static final String PARAM_PUSHID="pushId";

    /**
     * token相关固定参数值
     */
    public static final String SECRET_KEY = "!$Xuop@^&%x$Sci(hs_+$"; // 加密key
    public static final String SECRET_SIGN_KEY = "!$*6uOuop@^&%,.$*ci(hf_&]|"; // 签名key 可以放到数据库保存
    public static final String AUTHORITIES = "authorities"; //权限名称
    public static final int EXPIRE_DAY = 30; // 30天
    public static final String TOKEN_PREFIX="Authorization: Bearer ";
    public static final String TOKEN_HEADER="token";//head中名称
    public static final String TOKEN_COOKIE="openIdToken";//openId的token,放在cookie里面
    public static final String DEVICE_ID="deviceId";//唯一设备id
    public static final String LOGIN_NAME="userName";//登录名
    public static final String TOKEN_TYPE="tokenType";//登录类型

    /**
     * 缓存相关
     * */

    /** 键：oauth模块前缀 **/
    private static final String KEY_PREFIX = "oauth.";

    public static final String CACHE_TOKEN_PREFIX=KEY_PREFIX+"applyToken_token";//登录名

    /**
     * 用户缓存key:用户登录态
     */
    public static final String REDIS_KEY_USER_LOGIN_STATUS = KEY_PREFIX+"user_login_status.";

    /**
     * 用户登录设备key:token
     */
    public static final String REDIS_KEY_USER_LOGIN_DEVICEID_TOKEN = KEY_PREFIX+"user_login_deviceId.";

    /**
     * key:value
     * 腾讯手QopenId:token
     */
    public static final String REDIS_KEY_USER_LOGIN_OPENID_TOKEN = KEY_PREFIX+"user_openid.";


    /** 值：用户缓存登录态过期时间为7天，单位：秒 **/
    public static final int EXPIRE_USER_LOGIN_STATUS_DAY = 60*60*24*7;//24*60*60
}
