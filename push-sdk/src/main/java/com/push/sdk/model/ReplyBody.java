package com.push.sdk.model;

import com.alibaba.fastjson.JSON;
import com.push.sdk.consts.KimConstant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wgj
 * @description 应答
 * @date 2021/6/17 15:33
 * @modify
 */
public class ReplyBody implements Serializable,IMessage{
    private static final long serialVersionUID = 1L;

    private final int type = KimConstant.DATA_TYPE_REPLY;

    /**
     * key
     */
    private String key;

    /**
     * 返回码
     */
    private int code;

    /**
     * 描述
     */
    private String desc;

    /**
     * 返回数据集合
     */
    private final Map<String,String> dataMap = new HashMap<>();

    /**
     * 时间戳
     */
    private long timestamp;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[ReplyBody]").append("\n");
        builder.append("key:").append(this.getKey()).append("\n");
        builder.append("timestamp:").append(timestamp).append("\n");
        builder.append("code:").append(code).append("\n");

        builder.append("data:{");
        dataMap.forEach((k, v) -> builder.append("\n").append(k).append(":").append(v));
        builder.append("\n}");

        return builder.toString();
    }

    @Override
    public String body() {
        return JSON.toJSONString(this);
    }


    public int getType() {
        return type;
    }
}
