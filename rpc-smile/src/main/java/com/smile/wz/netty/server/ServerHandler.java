package com.smile.wz.netty.server;

import com.alibaba.dubbo.common.utils.NamedThreadFactory;
import com.alibaba.fastjson.JSON;
import com.smile.wz.handler.ChannelHandler;
import com.smile.wz.netty.server.ChannelEventRunnable.ChannelState;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * wangzhen23
 * 2018/3/9.
 */
@Sharable
public class ServerHandler extends ChannelHandlerAdapter {
    protected static final ExecutorService SHARED_EXECUTOR = Executors.newCachedThreadPool(new NamedThreadFactory("WZSharedHandler", true));
    private ChannelHandler handler;

    public ServerHandler(ChannelHandler handler) {
        this.handler = handler;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("+++++++++++++++++++++++++++");
        System.out.println("exception");
//        super.exceptionCaught(ctx, cause);
//        cause.printStackTrace();
        System.out.println("+++++++++++++++++++++++++++");
//        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("The server receive message request: " + JSON.toJSONString(msg));

        ExecutorService cexecutor = getExecutorService();
        try {
            cexecutor.execute(new ChannelEventRunnable(ctx.channel(), handler, ChannelState.RECEIVED, msg));
        }catch (Exception e){
            System.out.println("rpc server Thread execute error");
            e.printStackTrace();
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        ctx.flush();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
//        ctx.close();
    }

    private ExecutorService getExecutorService() {
        return SHARED_EXECUTOR;
    }

    public void close() {
        try {
            SHARED_EXECUTOR.shutdown();
        } catch (Throwable t) {
            System.out.println("fail to destroy thread pool of server: " + t.getMessage());
            t.printStackTrace();
        }
    }
}
