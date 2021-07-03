package com.push.push;

import com.push.sdk.model.Message;
import com.push.comps.KafkaTemplateComps;
import com.push.comps.RedisTemplateComps;
import com.push.service.ApnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息发送实现类
 */
@Component
public class DefaultMessagePusher {

    @Autowired
    private ApnService apnService;

    @Autowired
    private KafkaTemplateComps kafkaTemplateComps;

    @Autowired
    private RedisTemplateComps redisTemplateComps;


    /**
     * 向用户发送消息
     *
     * @param message
     */
    public final void push(Message message) {

        String uid = message.getReceiver();

        /*
         * 说明iOS客户端开启了apns
         */
        String deviceToken = redisTemplateComps.getDeviceToken(uid);
        if (deviceToken != null) {
            apnService.push(message, deviceToken);
            return;
        }

        /*
         * 通过发送kafka广播
         */
        kafkaTemplateComps.push(message);

    }

}
