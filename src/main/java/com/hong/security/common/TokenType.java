package com.hong.security.common;

/**
 * <br>token状态,0 禁用  1 启用</br>
 */
public enum TokenType {

    USER(1, "USER", "用户已经登录token"), GUEST(2, "GUEST", "访客token");

    private int type;
    private String typeName;
    private String desc;

    private TokenType(int type, String typeName, String desc) {
        this.type = type;
        this.typeName = typeName;
        this.desc = desc;
    }

    public int type() {
        return type;
    }

    public String desc() {
        return desc;
    }

    public String typeName() {
        return typeName;
    }

    public static TokenType from(int type) {
        for (TokenType refund : TokenType.values()) {
            if (refund.type == type) {
                return refund;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(TokenType.from(2));
    }

}
