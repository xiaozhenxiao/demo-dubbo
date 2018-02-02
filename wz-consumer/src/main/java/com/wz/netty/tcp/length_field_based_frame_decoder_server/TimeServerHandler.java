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
package com.wz.netty.tcp.length_field_based_frame_decoder_server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.nio.ByteOrder;

/**
 * @author lilinfeng
 * @version 1.0
 * @date 2014年2月14日
 */
public class TimeServerHandler extends ChannelHandlerAdapter {
//    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        System.out.println(Thread.currentThread().getName() + " The time server receive order : " + body + " length:" + body.getBytes().length);
        String currentTime = DateFormatUtils.format(new java.util.Date(), "yyyy-MM-dd hh:mm:ss") + System.getProperty("line.separator");
        ByteBuf resp = Unpooled.buffer(20 + currentTime.getBytes().length,20 + currentTime.getBytes().length);
//        resp.order(ByteOrder.LITTLE_ENDIAN);
        resp.writeInt(0xAB65AB65);
        resp.writeInt(currentTime.getBytes().length);
        resp.writeInt(1111);
        resp.writeInt(0x0000800);
        resp.writeInt(758954411);
        resp.writeBytes(currentTime.getBytes());
        System.out.println("返回字节数:" + currentTime.getBytes().length);
        ctx.writeAndFlush(resp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
