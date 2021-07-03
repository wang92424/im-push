package com.push.sdk.model;

import com.alibaba.fastjson.JSON;
import com.push.sdk.consts.KimConstant;

import java.io.Serializable;

/**
 * @author wgj
 * @description 请求
 * @date 2021/6/17 15:32
 * @modify
 */
public class SentBody implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int type = KimConstant.DATA_TYPE_SENT;
    private String key;
    private String uid;

    private String channel;
    private String appVersion;
    private String osVersion;
    private String packageName;
    private String deviceId;
    private String deviceName;
    private long timestamp;

    public SentBody() {
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
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

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getType() {
        return type;
    }
}
