package wz.smile.future.wz.netty.client;

import com.alibaba.fastjson.JSON;
import wz.smile.future.Response;
import wz.smile.future.wz.rpc.DefaultWZFuture;
import wz.smile.future.wz.rpc.WZResponseFuture;
import io.netty.buffer.ByteBuf;
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

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        System.out.println("client exception!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("client connection success");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Response response = (Response) msg;
        System.out.println("client recived response:" + JSON.toJSONString(response));
        if (response != null && !response.isHeartbeat()) {
            DefaultWZFuture.received(response);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        ctx.flush();
    }
}
