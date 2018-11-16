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
package com.wz.netty.heartbeat.server;

import com.wz.netty.heartbeat.ChannelHolder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.haproxy.HAProxyMessage;

import java.io.File;
import java.util.Random;

/**
 * 断线重连server
 */
public class HeartBeatServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        ChannelHolder.setChannel(ctx.channel());
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");

        System.out.println(ctx.channel().remoteAddress() + " The time server receive order : " + body);
        ByteBuf resp = Unpooled.copiedBuffer("heartbeat server send message to client!".getBytes());
        ctx.writeAndFlush(resp);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接断开了！！！！！");
        long before = System.currentTimeMillis();
        Thread.sleep(new Random().nextInt(100));
        System.out.println("sleep:" + (System.currentTimeMillis() - before));
        System.out.println((ChannelHolder.getChannel() == ctx.channel()) + " ChannelHandlerContext.channel:" + ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
