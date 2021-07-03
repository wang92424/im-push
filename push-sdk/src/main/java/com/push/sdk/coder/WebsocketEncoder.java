package com.push.sdk.coder;

import com.push.sdk.model.IMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

/**
 * @author wgj
 * @description 发送消息编码
 * @date 2021/6/17 15:50
 * @modify
 */
public class WebsocketEncoder extends MessageToMessageEncoder<IMessage> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, IMessage message, List<Object> out) throws Exception {
        out.add(new TextWebSocketFrame(message.body()));
    }
}
