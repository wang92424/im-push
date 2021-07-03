package com.push.constants;

public interface Constants {

	String PUSH_MESSAGE_INNER_QUEUE = "signal/channel/PUSH_MESSAGE_INNER_QUEUE";

	String BIND_MESSAGE_INNER_QUEUE = "signal/channel/BIND_MESSAGE_INNER_QUEUE";

	String PING_MESSAGE_INNER_QUEUE = "signal/channel/PING_MESSAGE_INNER_QUEUE";

	String APNS_DEVICE_TOKEN = "APNS_OPEN_%s";

	int STATE_ACTIVE = 0;
	int STATE_APNS = 1;
	int STATE_INACTIVE = 2;

	String CHANNEL_IOS = "ios";
	String CHANNEL_ANDROID = "android";
	String CHANNEL_WINDOWS = "windows";
	String CHANNEL_MAC = "mac";
	String CHANNEL_WEB = "web";
}
