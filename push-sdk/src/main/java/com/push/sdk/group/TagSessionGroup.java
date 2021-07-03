package com.push.sdk.group;


import com.push.sdk.consts.ChannelAttr;
import io.netty.channel.Channel;

public class TagSessionGroup extends SessionGroup {

    @Override
    protected String getKey(Channel channel){
        return channel.attr(ChannelAttr.TAG).get();
    }
}