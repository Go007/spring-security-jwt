package com.hong.security.common;

public enum UserSaveType {
    INSERT(1, "插入用户"), COPY(2, "拷贝用户");

    private int type;
    private String desc;

    UserSaveType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int type() {
        return type;
    }

    public String desc() {
        return desc;
    }
}
