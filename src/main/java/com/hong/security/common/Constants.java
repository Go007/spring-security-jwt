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

    public static final String PARAM_USER_PWD = "password";

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
    public static final String TOKEN_HEADER="token";//header中名称
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


    /** 键：sys模块前缀 **/
    public static final String SYS_KEY_PREFIX = "sys.";

    /**
     * 被踢出的设备
     * */
    public static final String REDIS_KEY_USER_LAST_KICK_DEVICE = SYS_KEY_PREFIX+"userLastKickDeviceId.";

    /*-----------------------------用户服务相关-----------------------------*/

    /**
     * id_pserson,对应蜂鸟的id_person
     */
    public static final String PARAM_USER_ID_PERSON= "idPerson";

    /**
     * personId,api-biz personId
     */
    public static final String PARAM_USER_PERSON_ID= "personId";



    /**
     * 用户名  对应user对象中的loginName
     */
    public static final String PARAM_LOGIN_NAME= "loginName"; //用户名

    /**
     * 真实姓名
     */
    public static final String PARAM_REAL_NAME = "realName";

    /**
     *姓名
     */
    public static final String PARAM_NAME = "name";


    /**
     * 手机号
     */
    public static final String PARAM_USER_MOBILE = "mobile";

    public static final String PARAM_USER_PHONE = "phone";

    /**
     * 对应蜂鸟手机号
     */
    public static final String PARAM_USER_FN_MOBILE = "fnMobile";

    /**
     * 身份证号码
     */
    public static final String PARAM_USER_CERTNO = "certNo"; // 身份证

    public static final String PARAM_USER_IDCARD = "idCard"; // 身份证

    public static final String PARAM_USER_CREDITID="creditID";//身份证

    public static final String PARAM_USER_IDENT="ident";//身份证

    /**
     * 新密码
     */
    public static final String PARAM_USER_NEW_PWD= "newPwd";

    /**
     * ip地址
     */
    public static final String PARAM_IP_ADDRESS= "ipAddr";

    /**
     * url
     */
    public static final String PARAM_URL= "url";

    public static final String PARAM_SHOW_URL= "showUrl";

    /**
     * 状态
     */
    public static final String PARAM_STATUS= "status";

    /**
     * 额度
     */
    public static final String PARAM_POS_LIMIT= "posLimit";

    /**
     * 原因:拒绝原因
     */
    public static final String PARAM_REASON= "reason";

    /**
     * 腾讯征信分
     */
    public static final String PARAM_TENCENT_CREDIT_SCORE= "tencentCreditScore";

    /**
     * 腾讯消费金融分
     */
    public static final String PARAM_TENCENT_XFJR_SCORE= "tencentXfjrScore";

    /**
     * 即有分
     */
    public static final String PARAM_GIVEU_CREDIT_SCORE= "giveuCreditScore";

    /*腾讯征信二期需求增加 20170829 start*/

    /**
     * 业务类型id
     * */
    public static final String PARAM_BUSINESS="business";

    /**
     *身份识别号码
     * */
    public static final String PARAM_IDENTIFIER="identifier";

    /**
     *业务流水号
     * */
    public static final String PARAM_BUSINESS_SEQ="business_seq";

    /**
     * 状态开始时间
     * */
    public static final String PARAM_STATUS_START_TIME="status_start_time";

    /**
     * 涉及金额:分
     * */
    public static final String PARAM_STATUS_AMOUNT="status_amount";

    /**
     * 状态描述
     * */
    public static final String PARAM_STATUS_DES="status_des";

    /**
     * 备注
     * */
    public static final String PARAM_MEMO="memo";

    /**
     * 开户时间
     * */
    public static final String PARAM_TRANS_START_TIME="trans_start_time";

    /**
     * 产品标识
     * */
    public static final String PARAM_PRODUCT_ID="product_id";

    public static final String PARAM_ACTIVED="isActiveSuccess";
    /*腾讯征信二期需求增加 20170829 end*/

    /**
     * 下次激活时间
     * */
    public static final String PARAM_NEXT_ACTIVE_DATE="nextActivateDate";


    /**
     * 逾期天数
     * */
    public static final String PARAM_EXPIRE_DAYS="expire_days";
    /**
     * 短信码
     */
    public static final String PARAM_SMS_CODE= "smsCode";


    /**
     * 随机code
     */
    public static final String PARAM_IDENTIFY_CODE= "randCode";

    /**
     * 类型
     */
    public static final String PARAM_TYPE= "type";

    /**
     * 版本
     */
    public static final String PARAM_VERSION= "version";

    /**
     * 1个设备登录用户次数
     * */
    public static final String PARAM_DEVICEID_LOGINUSER_TIMES= "deviceIdLoginUserTimes";

    /**
     * 1个用户登录设备id次数
     * */
    public static final String PARAM_USER_LOGINONDEVICEID_TIMES= "userLoginOnDeviceIdTimes";

    /**
     * 阻止一个设备登录多个账号标记
     * */
    public static final String PARAM_PREVENT_ONEDEVICE_LOGINUSERFLAG= "preventOneDeviceLoginUserFlag";

    /**
     * 阻止一个账号登录多个设备
     * */
    public static final String PARAM_PREVENT_ONEUSERFLAG_LOGINDEVICE= "preventOneUserFlagLoginDevice";

    public static final String PARAM_AD_ID= "adId";

    /**
     * 强制更新 1 普通更新 2强制更新
     */
    public static final String PARAM_APP_VERSION_STATUS= "versionStatus";

    /**
     * 描述
     */
    public static final String PARAM_DESC= "desc";

    /**
     * title 标题
     */
    public static final String PARAM_TITLE= "title";

    /**
     * 文件md5值 防篡改
     */
    public static final String PARAM_FILEMD5= "fileMd5";

    /**
     * 频率
     */
    public static final String PARAM_FREQUENCY= "frequency";

    /**
     * 广告可用 enable true 可用
     */
    public static final String PARAM_AD_ENABLE= "enable";

    /**
     * 广告url
     */
    public static final String PARAM_AD_IMGURL= "imgUrl";

    /**
     * 广告点击链接
     */
    public static final String PARAM_AD_IMGURLLINK= "imgUrlLink";

    /**
     * 内容
     */
    public static final String PARAM_CONTENT= "content";

    /**
     * 消息范围
     */
    public static final String PARAM_MSG_SCOPE= "msgScope";

    /**
     * 消息级别
     */
    public static final String PARAM_MSG_LEVEL= "msgLevel";

    /**
     * 跳转链接
     */
    public static final String PARAM_REDIRECTURL= "redirectUrl";

    /**
     * 广告url
     */
    public static final String PARAM_IMG_URL= "imgUrl";

    /**
     * 分页大小 例如:20
     */
    public static final String PARAM_PAGE_SIZE= "pageSize";

    /**
     * 分页之后的 总页数
     */
    public static final String PARAM_PAGES= "pages";
    /**
     * 页码
     */
    public static final String PARAM_PAGE_NUM= "pageNum";

    /**
     * 消息编号 唯一
     */
    public static final String PARAM_MSG_NO= "msgNo";

    /**
     * 消息接收时间
     */
    public static final String PARAM_RECEIVE_TIME= "receiveTime";

    /**
     * 分页结果集
     */
    public static final String PARAM_RESULT_LIST= "resultList";

    /**
     * 消息已读 未读
     */
    public static final String PARAM_IS_READ= "isRead";

    /**
     * 是否存在
     */
    public static final String PARAM_IS_EXIST= "isExist";

    /**
     * 通过登录来操作
     */
    public static final String PARAM_ACCESS_BY_LOGIN= "access_by_login";

    /**
     * 登录白名单后缀
     * */
    public static final String LOGIN_WHITE_LIST_SUFFIX= "login:phone";

    /**
     * 安卓应用市场渠道来源
     * */
    public static final String PARAM_ANDROID_CHANNEL= "androidChannel";

    /**
     * 银行卡号
     * */
    public static final String PARAM_BANK_NO= "bankNo";

    /**
     * 银行卡code
     * */
    public static final String PARAM_BANK_CODE= "bankCode";

    /**
     * 银行卡名称
     * */
    public static final String PARAM_BANK_NAME= "bankName";

    /**
     * 系统维护
     * */
    public static final String PARAM_SYTEM_MAINTENANCE= "maintenance";

    /**
     * 真
     */
    public static final Integer TRUE = 1;

    /**
     * 假
     */
    public static final Integer FALSE = 0;


    /**
     * 点号符 .
     */
    public static final String DIT_CONCAT_SYMBOL = ".";

    /**
     * 逗号连接符:,
     */
    public static final String COMMA_CONCAT_SYMBOL = ",";

    /**
     * 渠道
     */
    public static final String PARAM_CHANNEL = "channel";

    /**
     * 用户最后登录的设备key
     */
    public static final String REDIS_KEY_USER_LAST_LOGIN_DEVICE = KEY_PREFIX+"userLastLoginDeviceId.";
}
