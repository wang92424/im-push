package com.push.service;

import cn.teaey.apns4j.Apns4j;
import cn.teaey.apns4j.network.ApnsChannel;
import cn.teaey.apns4j.network.ApnsChannelFactory;
import cn.teaey.apns4j.network.ApnsGateway;
import cn.teaey.apns4j.protocol.ApnsPayload;
import com.push.sdk.model.Message;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ApnService{

	private static final Logger LOGGER = LoggerFactory.getLogger(ApnService.class);

	private final ApnsChannelFactory apnsChannelFactory;

	@Autowired
	public ApnService(@Value("${apple.apns.p12.file}") String p12File,
					  @Value("${apple.apns.p12.password}") String password,
					  @Value("${apple.apns.debug}") boolean isDebug){
		apnsChannelFactory = Apns4j.newChannelFactoryBuilder()
				.keyStoreMeta(getClass().getResourceAsStream(p12File))
				.keyStorePwd(password)
				.apnsGateway(isDebug ? ApnsGateway.DEVELOPMENT : ApnsGateway.PRODUCTION)
				.build();
	}


	public void push(Message message, String deviceToken) {

		if(StringUtils.isBlank(deviceToken)) {
			return ;
		}

		ApnsChannel apnsChannel = apnsChannelFactory.newChannel();
		ApnsPayload apnsPayload = new ApnsPayload();

		apnsPayload.alert("您有一条新的消息");

		apnsPayload.sound("default");
		apnsPayload.badge(1);
		apnsPayload.extend("id",message.getId());
		apnsPayload.extend("action",message.getAction());
		apnsPayload.extend("content",message.getContent());
		apnsPayload.extend("sender",message.getSender());
		apnsPayload.extend("receiver",message.getReceiver());
		apnsPayload.extend("format",message.getFormat());
		apnsPayload.extend("extra",message.getExtra());
		apnsPayload.extend("timestamp",message.getTimestamp());

		try {
			apnsChannel.send(deviceToken, apnsPayload);
			LOGGER.info("APNs push done.\ndeviceToken : {} \napnsPayload : {}",deviceToken,apnsPayload.toJsonString());
		}catch(Exception exception) {
			LOGGER.error("APNs push failed",exception);
		}finally {
			IOUtils.closeQuietly(apnsChannel);
		}
	}
}
