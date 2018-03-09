package com.wz.netty.future.wz.netty.client;

import com.wz.netty.future.Response;
import com.wz.netty.future.wz.rpc.DefaultWZFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端信息处理
 * wangzhen23
 * 2018/3/3.
 */
public class ClientHandler extends ChannelHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        logger.info("");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Response response = (Response) msg;
        if (response != null && !response.isHeartbeat()) {
            DefaultWZFuture.received(response);
        }
    }
}
