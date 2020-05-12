package com.hong.security.common;

/**
 * 用户状态
 */
public enum DataEnableType {
    DISABLE(0, "禁用"), ENABLE(1, "启用");

    private int value;
    private String desc;

    private DataEnableType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static DataEnableType from(int value) {
        for (DataEnableType userStatu : DataEnableType.values()) {
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
        System.out.println(DataEnableType.from(2));
    }
}
