package com.push.sdk.model;

import com.alibaba.fastjson.JSON;
import com.push.sdk.consts.KimConstant;

import java.io.Serializable;

/**
 * @author wgj
 * @description 消息
 * @date 2021/6/17 15:32
 * @modify
 */
public class Message implements Serializable, IMessage {

    private static final long serialVersionUID = 1L;

    private final int type = KimConstant.DATA_TYPE_MESSAGE;
    /**
     * 消息编号
     */
    private long id;

    /**
     * 自定义消息类别
     */
    private String action;
    /**
     * 消息标题
     */
    private String title;
    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息发送账号
     */
    private String sender;
    /**
     * 消息接收者
     */
    private String receiver;

    /**
     * 内容格式
     */
    private String format;

    /**
     * 附加内容
     */
    private String extra;

    /**
     * 时间戳
     */
    private long timestamp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "#Message#" + "\n" +
                "id:" + id + "\n" +
                "action:" + action + "\n" +
                "title:" + title + "\n" +
                "content:" + content + "\n" +
                "extra:" + extra + "\n" +
                "sender:" + sender + "\n" +
                "receiver:" + receiver + "\n" +
                "format:" + format + "\n" +
                "timestamp:" + timestamp;
    }

    @Override
    public String body() {
        return JSON.toJSONString(this);
    }


    public int getType() {
        return type;
    }
}
