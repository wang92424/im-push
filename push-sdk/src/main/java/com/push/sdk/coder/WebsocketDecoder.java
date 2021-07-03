package com.push.sdk.coder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

/**
 * @author wgj
 * @description 发送消息解码
 * @date 2021/6/17 15:50
 * @modify
 */
public class WebsocketDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame socketFrame, List<Object> out) {
        out.add(socketFrame.text());
    }

}
