/*
 * Copyright 2013-2018 Lilinfeng.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wz.netty.heartbeat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author lilinfeng
 * @version 1.0
 * @date 2014年2月14日
 */
public class TimeClient {
    private static Bootstrap bootstrap;
    private static ChannelFutureListener channelFutureListener = null;

    public void connect(int port, String host) throws Exception {
        // 配置客户端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(new HeartBeatHandler());
                        }
                    });
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            bootstrap.option(ChannelOption.SO_TIMEOUT, 5000);

            channelFutureListener = new ChannelFutureListener() {
                public void operationComplete(ChannelFuture f) throws Exception {
                    //  Log.d(Config.TAG, "isDone:" + f.isDone() + "     isSuccess:" + f.isSuccess() +
                    //          "     cause" + f.cause() + "        isCancelled" + f.isCancelled());

                    if (f.isSuccess()) {
                        System.out.println("重新连接服务器成功");
                    } else {
                        System.out.println("重新连接服务器失败");
                        //  3秒后重新连接
                        f.channel().eventLoop().schedule(() -> {
                                doConnect();
                            }, 3, TimeUnit.SECONDS);
                    }
                }
            };
        } finally {
        }
    }

    //  连接到服务端
    public static void doConnect() {
        System.out.println("doConnect");
        ChannelFuture future = null;
        try {
            future = bootstrap.connect(new InetSocketAddress("127.0.0.1", 8080));
            future.addListener(channelFutureListener);

        } catch (Exception e) {
            e.printStackTrace();
            //future.addListener(channelFutureListener);
            System.out.println("关闭连接");
        }

    }
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        int port = 80;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }
        TimeClient timeClient = new TimeClient();
        timeClient.connect(port, "192.168.187.128");
        timeClient.doConnect();
        while (true)
        Thread.sleep(1000);
    }
}
