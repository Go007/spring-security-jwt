package com.hong.security.bean;

public class LoginUserFlagStatistic{

    private static final long serialVersionUID = -1766632246036049011L;

    private Long id;

    private String userFlag;

    private Integer loginTimes;

    private String loginDeviceIdDesc;

    private Integer status;

    private String remark;

    private Long createTime;

    private Long updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserFlag() {
        return userFlag;
    }

    public void setUserFlag(String userFlag) {
        this.userFlag = userFlag == null ? null : userFlag.trim();
    }

    public Integer getLoginTimes() {
        return loginTimes;
    }

    public void setLoginTimes(Integer loginTimes) {
        this.loginTimes = loginTimes;
    }

    public String getLoginDeviceIdDesc() {
        return loginDeviceIdDesc;
    }

    public void setLoginDeviceIdDesc(String loginDeviceIdDesc) {
        this.loginDeviceIdDesc = loginDeviceIdDesc == null ? null : loginDeviceIdDesc.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}