package com.sunlands.datacenter.applog;

import java.util.List;

public class LineDTO {
    String[] data;

    String[] token;

    String[] version;

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public String[] getToken() {
        return token;
    }

    public void setToken(String[] token) {
        this.token = token;
    }

    public String[] getVersion() {
        return version;
    }

    public void setVersion(String[] version) {
        this.version = version;
    }
}
