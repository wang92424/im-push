package com.push.dao.domain;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "t_session")
public class Session implements Serializable {

    private static final transient long serialVersionUID = 1L;

    /**
     * 数据库主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * session绑定的用户账号
     */
    @Column(name = "uid")
    private String uid;

    /**
     * session在本台服务器上的ID
     */
    @Column(name = "nid",length = 32,nullable = false)
    private String nid;

    /**
     * 客户端ID (设备号码+应用包名),ios为deviceToken
     */

    @Column(name = "device_id",length = 64,nullable = false)
    private String deviceId;

    /**
     * 终端设备型号
     */
    @Column(name = "device_name")
    private String deviceName;

    /**
     * session绑定的服务器IP
     */
    @Column(name = "host",length = 15,nullable = false)
    private String host;

    /**
     * 终端设备类型
     */
    @Column(name = "channel",length = 10,nullable = false)
    private String channel;

    /**
     * 终端应用版本
     */
    @Column(name = "app_version")
    private String appVersion;

    /**
     * 终端系统版本
     */
    @Column(name = "os_version")
    private String osVersion;

    /**
     * 登录时间
     */
    @Column(name = "bind_time")
    private Long bindTime;

    /**
     * 经度
     */
    @Column(name = "longitude")
    private Double longitude;

    /**
     * 维度
     */
    @Column(name = "latitude")
    private Double latitude;

    /**
     * 位置
     */
    @Column(name = "location")
    private String location;

    /**
     * 状态
     */
    private int state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public Long getBindTime() {
        return bindTime;
    }

    public void setBindTime(Long bindTime) {
        this.bindTime = bindTime;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
