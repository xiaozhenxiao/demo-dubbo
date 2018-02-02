/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.wz.netty.tcp.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.util.logging.Logger;

/**
 * @author lilinfeng
 * @version 1.0
 * @date 2014年2月14日
 */
public class TimeClientHandler extends ChannelHandlerAdapter {

    private static final Logger logger = Logger
            .getLogger(TimeClientHandler.class.getName());

    private int counter;

    private int sendCounter;

    private String req;

    /**
     * System.getProperty("line.separator")
     * Creates a client-side handler.
     */
    public TimeClientHandler() {
        req = "QUERY TIME ORDER" + ++sendCounter;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Thread[] sendThreads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            /*message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);*/
            ByteBuf message = Unpooled.buffer(req.length() + 1);
            sendThreads[i] = new Thread(() -> {
                message.writeBytes((req + ++sendCounter).getBytes());
                ctx.writeAndFlush(message);
            });
        }
        for (Thread sendThread : sendThreads) {
            sendThread.start();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("Now is : " + body + " ; the counter is : " + ++counter);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 释放资源
        logger.warning("Unexpected exception from downstream : " + cause.getMessage());
        ctx.close();
    }
}
