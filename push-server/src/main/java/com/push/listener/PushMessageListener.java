package com.push.listener;

import com.alibaba.fastjson.JSON;
import com.push.sdk.group.SessionGroup;
import com.push.sdk.model.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


/**
 * 监听redis队列
 * 最好替换为MQ消息队列
 */
@Component
public class PushMessageListener {

    @Autowired
    private SessionGroup sessionGroup;
    @KafkaListener(topics = {"messageTopic"})
    public void onMessage(ConsumerRecord<String, String> record) {

        Message message = JSON.parseObject(record.value(), Message.class);

        String uid = message.getReceiver();

        sessionGroup.write(uid, message);

    }
}
