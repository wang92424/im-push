package com.push.handler;

import com.push.sdk.consts.ChannelAttr;
import com.push.sdk.group.SessionGroup;
import com.push.sdk.handler.IRequestHandler;
import com.push.handler.annotation.KIMHandler;
import com.push.service.SessionService;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 断开连接
 */
@KIMHandler(key = "client_closed")
public class ClosedHandler implements IRequestHandler {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionGroup sessionGroup;

    @Override
    public void process(Channel channel, Object message) {

        String uid = channel.attr(ChannelAttr.UID).get();

        if (uid == null) {
            return;
        }

        String nid = channel.attr(ChannelAttr.ID).get();

        sessionGroup.remove(channel);

        sessionService.delete(uid, nid);

    }

}
