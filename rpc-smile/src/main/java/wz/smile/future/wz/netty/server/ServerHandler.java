package wz.smile.future.wz.netty.server;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import wz.smile.future.Request;
import wz.smile.future.Response;
import wz.smile.future.wz.handler.ChannelHandler;

/**
 * wangzhen23
 * 2018/3/9.
 */
public class ServerHandler extends ChannelHandlerAdapter {
    private ChannelHandler handler;

    public ServerHandler(ChannelHandler handler) {
        this.handler = handler;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("+++++++++++++++++++++++++++");
        System.out.println("exception");
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        System.out.println("+++++++++++++++++++++++++++");
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Request request = (Request) msg;
        System.out.println("The server receive message request: " + JSON.toJSONString(request));

        Response res = new Response(request.getId(), request.getVersion());
        res.setStatus(Response.OK);
        res.setResult(handler.reply(ctx.channel(), request.getData()));
        ChannelFuture future = ctx.writeAndFlush(res);
        System.out.println("response success:" + future.isSuccess());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        ctx.flush();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        ctx.close();
    }
}
