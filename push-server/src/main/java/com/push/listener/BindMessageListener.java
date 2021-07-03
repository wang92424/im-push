package com.push.listener;

import com.alibaba.fastjson.JSON;
import com.push.sdk.consts.ChannelAttr;
import com.push.sdk.group.SessionGroup;
import com.push.sdk.model.Message;
import com.push.constants.Constants;
import com.push.dao.domain.Session;
import io.netty.channel.Channel;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 集群环境 监控登录情况
 */
@Component
public class BindMessageListener {

    private static final String FORCE_OFFLINE_ACTION = "999";

    private static final String SYSTEM_ID = "0";

    private final Map<String, String[]> conflictMap = new HashMap<>();

    @Autowired
    private SessionGroup sessionGroup;

    public BindMessageListener() {
        conflictMap.put(Constants.CHANNEL_WINDOWS, new String[]{Constants.CHANNEL_WINDOWS, Constants.CHANNEL_WEB, Constants.CHANNEL_MAC});
        conflictMap.put(Constants.CHANNEL_WEB, new String[]{Constants.CHANNEL_WINDOWS, Constants.CHANNEL_WEB, Constants.CHANNEL_MAC});
        conflictMap.put(Constants.CHANNEL_MAC, new String[]{Constants.CHANNEL_WINDOWS, Constants.CHANNEL_WEB, Constants.CHANNEL_MAC});
    }

    @KafkaListener(topics = {"sessionTopic"})
    public void onMessage(ConsumerRecord<String, String> record) {

        Session session = JSON.parseObject(record.value(), Session.class);
        String uid = session.getUid();
        String[] conflictChannels = conflictMap.get(session.getChannel());

        Collection<Channel> channelList = sessionGroup.find(uid, conflictChannels);

        channelList.removeIf(channel -> session.getNid().equals(channel.attr(ChannelAttr.ID).get()));

        /*
         * 账号在其他终端登录
         */
        channelList.forEach(channel -> {

            if (Objects.equals(session.getDeviceId(), channel.attr(ChannelAttr.DEVICE_ID).get())) {
                channel.close();
                return;
            }

            Message message = new Message();
            message.setAction(FORCE_OFFLINE_ACTION);
            message.setReceiver(uid);
            message.setSender(SYSTEM_ID);
            message.setContent(session.getDeviceName());
            channel.writeAndFlush(message);
            channel.close();
        });


    }
}
