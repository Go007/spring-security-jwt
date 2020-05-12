package com.hong.security.common;

/**
 * 绑定状态
 */
public enum BindType {
    LOGIN(1, "登录"), ACTIVE(2, "激活");

    private int value;
    private String desc;

    BindType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static BindType from(int value) {
        for (BindType userStatu : BindType.values()) {
            if (userStatu.value == value) {
                return userStatu;
            }
        }
        return null;
    }

    public int value() {
        return value;
    }

    public String desc() {
        return desc;
    }

    public static void main(String[] args) {
        System.out.println(BindType.from(2));
    }
}
