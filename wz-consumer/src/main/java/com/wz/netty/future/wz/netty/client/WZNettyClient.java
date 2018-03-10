package com.wz.netty.future.wz.netty.client;

import com.alibaba.fastjson.JSON;
import com.wz.netty.future.Request;
import com.wz.netty.future.wz.rpc.DefaultWZFuture;
import com.wz.netty.future.wz.rpc.WZResponseFuture;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.TimeUnit;

/**
 * RPC netty client
 * wangzhen23
 * 2018/2/8.
 */
public class WZNettyClient {
    private Channel channel;

    public void connect(int port, String host) throws Exception {
        // 配置客户端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
//                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(ByteOrder.BIG_ENDIAN, Integer.MAX_VALUE, 4, 4, 12, 0, true));
//                            ch.pipeline().addLast(new HeaderHandler());// TODO: 2018/3/3
//                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new ClientHandler());
                        }
                    });

            // 发起异步连接操作
            ChannelFuture future = b.connect(host, port);

            try {
                boolean ret = future.awaitUninterruptibly(3000, TimeUnit.MILLISECONDS);

                if (ret && future.isSuccess()) {
                    Channel newChannel = future.channel();
                    try {
                        // Close old channel
                        Channel oldChannel = WZNettyClient.this.channel; // copy reference
                        if (oldChannel != null) {
                            try {
                                System.out.println("Close old netty channel " + oldChannel + " on create new netty channel " + newChannel);
                                oldChannel.close();
                            } finally {
//                                NettyChannel.removeChannelIfDisconnected(oldChannel);
                            }
                        }
                    } finally {
                        this.channel = newChannel;
                    }
                } else if (future.cause() != null) {
                    throw new RuntimeException("");
                } else {
                    throw new RuntimeException("");
                }
            } finally {

            }
        } finally {
        }
    }

    public WZNettyClient(String host) {
        try {
            connect(8080, host);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WZResponseFuture request(Object message, int timeout) {
        System.out.println("client send message:" + JSON.toJSONString(message));
        Request req = new Request();
        req.setVersion("2.0.0");
        req.setTwoWay(true);
        req.setData(message);
        DefaultWZFuture future = new DefaultWZFuture(channel, req, timeout);
        channel.writeAndFlush(req);
        return future;
    }
}
