package com.wz.netty.future.wz.export;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * wangzhen23
 * 2018/3/13.
 */
public interface WZServer extends Server{

    /**
     * get channels.
     *
     * @return channels
     */
    Collection<Channel> getWZChannels();

    /**
     * get channel.
     *
     * @param remoteAddress
     * @return channel
     */
    Channel getWZChannel(InetSocketAddress remoteAddress);
}
