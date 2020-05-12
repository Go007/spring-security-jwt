package com.hong.security.common;

/**
 * 角色类型
 */
public enum RoleType {
    ROLE_USER(1, "USER", "普通用户角色"), ROLE_ADMIN(2, "ADMIN", "权限较高角色");

    private int type;
    private String roleName;
    private String desc;

    private RoleType(int type, String roleName, String desc) {
        this.type = type;
        this.desc = desc;
        this.roleName = roleName;
    }

    public int type() {
        return type;
    }

    public String desc() {
        return desc;
    }

    public String roleName() {
        return roleName;
    }

    public static void main(String[] args) {
        System.out.println(RoleType.ROLE_ADMIN.roleName);
    }
}
