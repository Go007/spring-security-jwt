package com.hong.security.common;

/**
 * 平台,fnImport 为蜂鸟直接调用接口导入
 */
public enum PlatformType {

    ANDROID(0, "Android"), IOS(1, "iOS"), MOBILEQQ(2, "mobileQQ"), H5(3, "h5"), WECHAT(4, "weChat"), WECHATMALL(5, "weChatMall"), FNIMPORT(98, "fnImport"), ALL(99, "all");

    private int status;
    private String desc;

    private PlatformType(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int status() {
        return status;
    }

    public String desc() {
        return desc;
    }

    public static PlatformType from(int status) {
        for (PlatformType refund : PlatformType.values()) {
            if (refund.status == status) {
                return refund;
            }
        }
        return null;
    }

    public static PlatformType from(String platformName) {
        for (PlatformType platformType : PlatformType.values()) {
            if (platformType.toString().equals(platformName.toUpperCase())) {
                return platformType;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(PlatformType.from(2));
        System.out.println(PlatformType.from("mobIlEQQ"));
    }

}
