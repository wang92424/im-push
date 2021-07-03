package com.push.config;

import com.push.sdk.acceptor.KimAcceptor;
import com.push.sdk.group.SessionGroup;
import com.push.sdk.group.TagSessionGroup;
import com.push.sdk.handler.IRequestHandler;
import com.push.sdk.model.SentBody;
import com.push.handler.annotation.KIMHandler;
import com.push.service.SessionService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class KIMConfig implements IRequestHandler, ApplicationListener<ApplicationStartedEvent> {

    private final HashMap<String, IRequestHandler> handlerMap = new HashMap<>();
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private SessionService sessionService;

    @Bean
    public SessionGroup sessionGroup() {
        return new SessionGroup();
    }

    @Bean
    public TagSessionGroup tagSessionGroup() {
        return new TagSessionGroup();
    }


    @Bean(destroyMethod = "destroy")
    public KimAcceptor getNioSocketAcceptor(@Value("${kim.server.port}") int port, @Value("${kim.server.webPort}") int webPort) {

        return new KimAcceptor(port, webPort, this);

    }

    @Override
    public void process(Channel channel, Object body) {
        log.info("收到消息:{}", body);
        SentBody body1 = (SentBody) body;
        IRequestHandler handler = handlerMap.get(body1.getKey());

        if (handler == null) {
            return;
        }

        handler.process(channel, body);

    }

    /**
     * springboot启动完成之后再启动im
     */
    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {

        Map<String, IRequestHandler> beans = applicationContext.getBeansOfType(IRequestHandler.class);

        for (Map.Entry<String, IRequestHandler> entry : beans.entrySet()) {

            IRequestHandler handler = entry.getValue();

            KIMHandler annotation = handler.getClass().getAnnotation(KIMHandler.class);

            if (annotation != null) {
                handlerMap.put(annotation.key(), handler);
            }
        }


        applicationContext.getBean(KimAcceptor.class).bind();

        sessionService.deleteLocalhost();
    }
}