package com.push.sdk.model;

import com.push.sdk.consts.KimConstant;

import java.io.Serializable;

/**
 * @author wgj
 * @description 客户端心跳
 * @date 2021/6/17 15:32
 * @modify
 */
public class Pong implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String TAG = "PONG";
    private static final Pong INSTANCE = new Pong();
    private final int type = KimConstant.DATA_TYPE_PONG;

    private Pong() {
    }

    public static Pong getInstance() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return TAG;
    }

    public int getType() {
        return type;
    }
}
