package com.push.sdk.handler;

import io.netty.channel.Channel;

/**
 * @author wgj
 * @description
 * @date 2021/6/17 15:35
 * @modify
 */
public interface IRequestHandler {

    /**
     * 处理收到客户端从长链接发送的数据
     */
    void process(Channel channel, Object body);
}
