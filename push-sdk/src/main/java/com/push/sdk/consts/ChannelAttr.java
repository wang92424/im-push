package com.push.sdk.consts;

import io.netty.util.AttributeKey;

/**
 * channel AttributeMap集合
 */
public interface ChannelAttr {
    AttributeKey<Integer> PING_COUNT = AttributeKey.valueOf("ping_count");
    AttributeKey<String> UID = AttributeKey.valueOf("uid");
    AttributeKey<String> CHANNEL = AttributeKey.valueOf("channel");
    AttributeKey<String> ID = AttributeKey.valueOf("id");
    AttributeKey<String> DEVICE_ID = AttributeKey.valueOf("device_id");
    AttributeKey<String> TAG = AttributeKey.valueOf("tag");
}
