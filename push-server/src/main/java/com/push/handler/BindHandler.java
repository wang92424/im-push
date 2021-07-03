package com.push.handler;

import com.push.sdk.consts.ChannelAttr;
import com.push.sdk.group.SessionGroup;
import com.push.sdk.handler.IRequestHandler;
import com.push.sdk.model.ReplyBody;
import com.push.sdk.model.SentBody;
import com.push.handler.annotation.KIMHandler;
import com.push.comps.KafkaTemplateComps;
import com.push.dao.domain.Session;
import com.push.service.SessionService;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
 * 绑定账号
 */
@KIMHandler(key = "client_bind")
public class BindHandler implements IRequestHandler {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionGroup sessionGroup;

    @Autowired
    private KafkaTemplateComps kafkaTemplateComps;

    @Override
    public void process(Channel channel, Object obj) {
        SentBody body = (SentBody) obj;
        ReplyBody reply = new ReplyBody();
        reply.setKey(body.getKey());
        reply.setCode(HttpStatus.OK.value());
        reply.setTimestamp(System.currentTimeMillis());

        String uid = body.getUid();
        Session session = new Session();
        session.setUid(uid);
        session.setNid(channel.attr(ChannelAttr.ID).get());
        session.setDeviceId(body.getDeviceId());
        session.setChannel(body.getChannel());
        session.setDeviceName(body.getDeviceName());
        session.setAppVersion(body.getAppVersion());
        session.setOsVersion(body.getOsVersion());

        channel.attr(ChannelAttr.UID).set(uid);
        channel.attr(ChannelAttr.CHANNEL).set(session.getChannel());
        channel.attr(ChannelAttr.DEVICE_ID).set(session.getDeviceId());

        /*
         *存储到数据库
         */
        sessionService.add(session);

        /*
         * 添加到内存管理
         */
        sessionGroup.add(channel);

        /*
         *向客户端发送bind响应
         */
        channel.writeAndFlush(reply);

        /*
         * 发送上线事件到集群中的其他实例，控制其他设备下线
         */
        kafkaTemplateComps.bind(session);
    }
}
