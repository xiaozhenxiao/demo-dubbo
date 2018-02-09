package com.wz.netty.tcp.length_field_based_frame_decoder_server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * wangzhen23
 * 2018/2/2.
 */
public class HeaderHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        int sign = buf.readInt();
        int dataSize = buf.readInt();
        int companyId = buf.readInt();
        int type = buf.readInt();
        int id = buf.readInt();
        System.out.println(Thread.currentThread().getName() + " Header : sign-" + ByteBufUtil.hexDump(buf, 0,4) + " length-" + dataSize + " companyId-" + companyId + " type-" + type + " id-" + id);
        ctx.fireChannelRead(buf.readBytes(dataSize));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
