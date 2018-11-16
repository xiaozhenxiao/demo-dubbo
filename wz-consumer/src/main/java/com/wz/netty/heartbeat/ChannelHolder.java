package com.wz.netty.heartbeat;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;

/**
 * TODO
 * wangzhen23
 * 2018/7/27.
 */
public class ChannelHolder {
    private static Channel channel;

    public static Channel getChannel() {
        return channel;
    }

    public static void setChannel(Channel channel) {
        ChannelHolder.channel = channel;
    }
}
