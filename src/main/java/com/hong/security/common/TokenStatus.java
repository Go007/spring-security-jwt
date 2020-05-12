package com.hong.security.common;

/**
 * token状态,0 禁用  1 启用
 */
public enum TokenStatus {

    DISABLE(0, "禁用"), ENABLE(1, "启用");

    private int status;
    private String desc;

    private TokenStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int status() {
        return status;
    }

    public String desc() {
        return desc;
    }

    public static TokenStatus from(int status) {
        for (TokenStatus refund : TokenStatus.values()) {
            if (refund.status == status) {
                return refund;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(TokenStatus.from(2));
    }

}
