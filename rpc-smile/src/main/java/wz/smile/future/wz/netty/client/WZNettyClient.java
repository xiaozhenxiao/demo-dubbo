package wz.smile.future.wz.netty.client;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import wz.smile.future.Request;
import wz.smile.future.wz.rpc.DefaultWZFuture;
import wz.smile.future.wz.rpc.WZResponseFuture;

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
                    .option(ChannelOption.SO_KEEPALIVE, true)
//                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
                            ch.pipeline().addLast(new ClientHandler());
                        }
                    });

            // 发起异步连接操作
            ChannelFuture future = b.connect(host, port);

            try {
                boolean ret = future.awaitUninterruptibly(30000, TimeUnit.MILLISECONDS);

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
                    throw new RuntimeException("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                } else {
                    throw new RuntimeException("cccccccccccccccccccccccccccccccccccccccc");
                }
            } finally {

            }
        } finally {
        }
    }

    public WZNettyClient(String host, Integer port) {
        try {
            connect(port, host);
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
