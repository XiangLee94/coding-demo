package com.sunlands.datacenter.applog;

public class DataObject {
    private String userId;
    private String userState;
    private String netType;
    private  String province;
    private String city;
    private String deviceModel;
    private String osVersion;
    private String appVersion;
    private String appSource;
    private String actionId;
    private String actionTime;
    private  String actionKey;
    private String pageKey;
    private String deleteFlag ="0";
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserState() {
        return userState;
    }
    public void setUserState(String userState) {
        this.userState = userState;
    }
    public String getNetType() {
        return netType;
    }
    public void setNetType(String netType) {
        this.netType = netType;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getDeviceModel() {
        return deviceModel;
    }
    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }
    public String getOsVersion() {
        return osVersion;
    }
    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }
    public String getAppVersion() {
        return appVersion;
    }
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
    public String getAppSource() {
        return appSource;
    }
    public void setAppSource(String appSource) {
        this.appSource = appSource;
    }
    public String getActionId() {
        return actionId;
    }
    public void setActionId(String actionId) {
        this.actionId = actionId;
    }
    public String getActionTime() {
        return actionTime;
    }
    public void setActionTime(String actionTime) {
        this.actionTime = actionTime;
    }
    public String getActionKey() {
        return actionKey;
    }
    public void setActionKey(String actionKey) {
        this.actionKey = actionKey;
    }
    public String getPageKey() {
        return pageKey;
    }
    public void setPageKey(String pageKey) {
        this.pageKey = pageKey;
    }
    public String getDeleteFlag() {
        return deleteFlag;
    }
    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public DataObject(){}
    public DataObject(String[] line) {
        this.userId = line[0];
        this.userState = line[1];
        this.netType = line[2];
        this.province = line[3];
        this.city = line[4];
        this.deviceModel = line[5];
        this.osVersion = line[6];
        this.appVersion = line[7];
        this.appSource = line[8];
        this.actionId = line[9];
        this.actionTime = line[10];
        this.actionKey = line[11];
        this.pageKey = line[12];
        this.deleteFlag = line[13];
    }
}

