package com.lee.hbasedemo.test;

public class WebLogDTO {
    private String  id;
    private String system_id;
    private String user_identify;
    private String event_id_str;
    private String unfixed_param;
    private String url;
    private String ip;
    private String region;
    private String send_time;
    private String create_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSystem_id() {
        return system_id;
    }

    public void setSystem_id(String system_id) {
        this.system_id = system_id;
    }

    public String getUser_identify() {
        return user_identify;
    }

    public void setUser_identify(String user_identify) {
        this.user_identify = user_identify;
    }

    public String getEvent_id_str() {
        return event_id_str;
    }

    public void setEvent_id_str(String event_id_str) {
        this.event_id_str = event_id_str;
    }

    public String getUnfixed_param() {
        return unfixed_param;
    }

    public void setUnfixed_param(String unfixed_param) {
        this.unfixed_param = unfixed_param;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
