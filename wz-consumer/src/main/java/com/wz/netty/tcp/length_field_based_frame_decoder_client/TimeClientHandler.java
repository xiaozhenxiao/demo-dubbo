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
package com.wz.netty.tcp.length_field_based_frame_decoder_client;

import com.wz.netty.pojo.SubscribeReq;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.nio.ByteOrder;
import java.util.logging.Logger;

/**
 * @author lilinfeng
 * @version 1.0
 * @date 2014年2月14日
 */
public class TimeClientHandler extends ChannelHandlerAdapter {

    private static final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());


    /**
     * Creates a client-side handler.
     */
    public TimeClientHandler() {
//        req = ("QUERY TIME ORDER " + ++counter + System.getProperty("line.separator")).getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
//        for (int i = 0; i < 100; i++) {
            String xin = new SubscribeReq(1, "xiaoxiao", "product", "18311381796", "address").toString();
            ByteBuf message = Unpooled.buffer(20 + xin.getBytes().length, 20 + xin.getBytes().length);
//        message.order(ByteOrder.LITTLE_ENDIAN);
            buildHeader(xin, message);
            System.out.println("发送字节数:" + xin.getBytes().length);
            ctx.writeAndFlush(message);
//        }
    }

    private void buildHeader(String xin, ByteBuf message) {
        message.writeInt(0xAB65AB65);
        message.writeInt(xin.getBytes().length);
        message.writeInt(1111);
        message.writeInt(0x0000800);
        message.writeInt(758954412);
        message.writeBytes(xin.getBytes());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        System.out.println(Thread.currentThread().getName() + " Now is : " + body + " length:" + body.getBytes().length);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 释放资源
        logger.warning("Unexpected exception from downstream : " + cause.getMessage());
        ctx.close();
    }
}
