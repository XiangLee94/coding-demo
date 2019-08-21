package http;

public class Dto {
    private Integer lUserId;
    private Integer lLiveId;
    private Boolean bIsLive;
    private Long lBeginTimestamp;
    private Long lEndTimestamp;
    private Integer iDevice;
    private String  sUserAgent;
    private Integer lDuration;
    private String sIp;

    public Integer getlUserId() {
        return lUserId;
    }

    public void setlUserId(Integer lUserId) {
        this.lUserId = lUserId;
    }

    public Integer getlLiveId() {
        return lLiveId;
    }

    public void setlLiveId(Integer lLiveId) {
        this.lLiveId = lLiveId;
    }

    public Boolean getbIsLive() {
        return bIsLive;
    }

    public void setbIsLive(Boolean bIsLive) {
        this.bIsLive = bIsLive;
    }

    public Long getlBeginTimestamp() {
        return lBeginTimestamp;
    }

    public void setlBeginTimestamp(Long lBeginTimestamp) {
        this.lBeginTimestamp = lBeginTimestamp;
    }

    public Long getlEndTimestamp() {
        return lEndTimestamp;
    }

    public void setlEndTimestamp(Long lEndTimestamp) {
        this.lEndTimestamp = lEndTimestamp;
    }

    public Integer getiDevice() {
        return iDevice;
    }

    public void setiDevice(Integer iDevice) {
        this.iDevice = iDevice;
    }

    public String getsUserAgent() {
        return sUserAgent;
    }

    public void setsUserAgent(String sUserAgent) {
        this.sUserAgent = sUserAgent;
    }

    public Integer getlDuration() {
        return lDuration;
    }

    public void setlDuration(Integer lDuration) {
        this.lDuration = lDuration;
    }

    public String getsIp() {
        return sIp;
    }

    public void setsIp(String sIp) {
        this.sIp = sIp;
    }
}
