package com.hong.security.common;

public enum UserStatus {
    DISABLE(0, "账号禁用"), ENABLE(1, "注册成功--未激活"), ACTIVE(2, "注册成功--钱包已激活");

    private int value;
    private String desc;

    UserStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static UserStatus from(int value) {
        for (UserStatus userStatu : UserStatus.values()) {
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
        System.out.println(UserStatus.from(2));
    }
}
