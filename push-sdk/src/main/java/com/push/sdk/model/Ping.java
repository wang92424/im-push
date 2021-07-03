package com.push.sdk.model;

import com.alibaba.fastjson.JSON;
import com.push.sdk.consts.KimConstant;

import java.io.Serializable;

/**
 * @author wgj
 * @description 服务端心跳
 * @date 2021/6/17 15:32
 * @modify
 */
public class Ping implements Serializable, IMessage {

    private static final long serialVersionUID = 1L;
    private static final String TAG = "PING";
    private static final String DATA = "PING";
    private static final Ping INSTANCE = new Ping();
    private final int type = KimConstant.DATA_TYPE_PING;

    private Ping() {

    }

    public static Ping getInstance() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return TAG;
    }

    @Override
    public String body() {
        return JSON.toJSONString(this);
    }

}
