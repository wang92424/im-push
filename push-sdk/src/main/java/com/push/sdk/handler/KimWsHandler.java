package com.push.sdk.handler;

import com.alibaba.fastjson.JSON;
import com.push.sdk.consts.ChannelAttr;
import com.push.sdk.consts.KimConstant;
import com.push.sdk.model.Ping;
import com.push.sdk.model.SentBody;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wgj
 * @description 处理websocket传输消息
 * @date 2021/6/17 15:34
 * @modify
 */
@Sharable
public class KimWsHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(KimWsHandler.class);
    private static final Object LOCK = new Object();
    public static KimWsHandler kimHttpHandler;
    private final IRequestHandler iRequestHandler;

    public KimWsHandler(IRequestHandler iRequestHandler) {

        this.iRequestHandler = iRequestHandler;

    }

    public static KimWsHandler instance(IRequestHandler iRequestHandler) {
        if (kimHttpHandler == null) {
            synchronized (LOCK) {
                if (kimHttpHandler == null) {
                    kimHttpHandler = new KimWsHandler(iRequestHandler);
                }
            }
        }
        return kimHttpHandler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) {
        LOGGER.info("收到消息：" + msg);
        SentBody sentBody = JSON.parseObject(msg, SentBody.class);
        iRequestHandler.process(channelHandlerContext.channel(), sentBody);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("与客户端建立连接，通道开启！");
        ctx.channel().attr(ChannelAttr.ID).set(ctx.channel().id().asShortText());
    }

    /**
     * 退出
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (ctx.channel().attr(ChannelAttr.UID) == null) {
            return;
        }

        SentBody body = new SentBody();
        body.setKey(KimConstant.CLIENT_CONNECT_CLOSED);
        iRequestHandler.process(ctx.channel(), body);
    }

    /**
     * 发送心跳
     * @param ctx
     * @param evt
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {

        if (!(evt instanceof IdleStateEvent)) {
            return;
        }

        IdleStateEvent idleEvent = (IdleStateEvent) evt;

        String uid = ctx.channel().attr(ChannelAttr.UID).get();

        /*
         * 关闭未认证连接
         */
        if (idleEvent.state() == IdleState.WRITER_IDLE && uid == null) {
            ctx.close();
            return;
        }

        /*
         * 已经认证的连接发送心跳请求
         */
        if (idleEvent.state() == IdleState.WRITER_IDLE && uid != null) {

            Integer pingCount = ctx.channel().attr(ChannelAttr.PING_COUNT).get();
            ctx.channel().attr(ChannelAttr.PING_COUNT).set(pingCount == null ? 1 : pingCount + 1);

            ctx.channel().writeAndFlush(Ping.getInstance());

            return;
        }

        /*
         * 30秒内没收到响应 关闭连接
         */
        Integer pingCount = ctx.channel().attr(ChannelAttr.PING_COUNT).get();
        if (idleEvent.state() == IdleState.READER_IDLE && pingCount != null && pingCount >= KimConstant.PONG_TIME_OUT_COUNT) {
            ctx.close();
            LOGGER.info("{} pong timeout.", ctx.channel());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.warn("EXCEPTION", cause);
    }
}
