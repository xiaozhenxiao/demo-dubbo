package com.wz.netty.future.wz.netty.client;

import com.wz.netty.future.wz.rpc.WZResponseFuture;
import com.wz.netty.tcp.length_field_based_frame_decoder_client.TimeClientHandler;
import com.wz.netty.tcp.length_field_based_frame_decoder_server.HeaderHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.nio.ByteOrder;
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
                        public void initChannel(SocketChannel ch){
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(ByteOrder.BIG_ENDIAN, Integer.MAX_VALUE, 4, 4, 12, 0, true));
                            ch.pipeline().addLast(new HeaderHandler());// TODO: 2018/3/3
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new TimeClientHandler());
                        }
                    });

            // 发起异步连接操作
            ChannelFuture future = b.connect(host, port).sync();

            if(future.awaitUninterruptibly(3000, TimeUnit.MILLISECONDS) && future.isSuccess()){
                channel = future.channel();
            }
            // 等待客户端链路关闭
            future.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放NIO线程组
            group.shutdownGracefully();
        }
    }

    public WZNettyClient(String host) {
        try {
            connect(8080, host);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WZResponseFuture request(Object message, int timeout){
        channel.writeAndFlush(message);

        return null;
    }
}
