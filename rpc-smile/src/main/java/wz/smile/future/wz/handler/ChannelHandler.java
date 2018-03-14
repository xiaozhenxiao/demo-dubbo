package wz.smile.future.wz.handler;

import com.alibaba.dubbo.remoting.RemotingException;
import io.netty.channel.Channel;

/**
 * wangzhen23
 * 2018/3/13.
 */
public interface ChannelHandler {

    Object reply(Channel channel, Object msg) throws RemotingException;
}
