package com.push.comps;

import com.alibaba.fastjson.JSON;
import com.push.sdk.model.Message;
import com.push.constants.Constants;
import com.push.dao.domain.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * kafka工具类
 */
@Component
public class KafkaTemplateComps {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 发到集群中所有实例
     *
     * @param message
     */
    public void push(Message message) {
        kafkaTemplate.send(Constants.PUSH_MESSAGE_INNER_QUEUE, JSON.toJSONString(message));
    }

    /**
     * 发到集群中所有实例
     *
     * @param session
     */
    public void bind(Session session) {
        kafkaTemplate.send(Constants.BIND_MESSAGE_INNER_QUEUE, JSON.toJSONString(session));
    }
}
