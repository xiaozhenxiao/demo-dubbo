package com.smile.wz.netty.server;

import com.alibaba.dubbo.remoting.RemotingException;
import com.smile.wz.Constants;
import com.smile.wz.export.WZServer;
import com.smile.wz.handler.ChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.Collection;
import java.util.Enumeration;
import java.util.concurrent.Executors;

/**
 * RPC netty server
 * wangzhen23
 * 2018/2/8.
 */
public class WZNettyServer implements WZServer {
    private Logger logger = LoggerFactory.getLogger(WZNettyServer.class);
    private Integer port;
    private ServerBootstrap bootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private ChannelHandler handler;

    private Channel channel;


    private void bind(int port) throws Exception {
        // 配置服务端的NIO线程组
        bossGroup = new NioEventLoopGroup(1, Executors.newSingleThreadExecutor(new DefaultThreadFactory("NettyServerBoss", true)));
        workerGroup = new NioEventLoopGroup(Constants.DEFAULT_IO_THREADS,
                Executors.newCachedThreadPool(new DefaultThreadFactory("NettyServerWorker", true)));
        ServerHandler serverHandler = new ServerHandler(handler);
        try {
            bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE,
                                    ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast(serverHandler);
                        }
                    });
            // 绑定端口，同步等待成功
            ChannelFuture channelFuture = bootstrap.bind(port);
            channelFuture.syncUninterruptibly();
            channel = channelFuture.channel();
        } finally {
            System.out.println("netty server bind at port:" + port);
        }
    }

    public WZNettyServer(Integer port, ChannelHandler handler) {
        try {
            this.handler = handler;
            this.port = port;
            bind(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<Channel> getWZChannels() {
        return null;
    }

    @Override
    public Channel getWZChannel(InetSocketAddress remoteAddress) {
        return channel;
    }

    @Override
    public boolean isBound() {
        return false;
    }

    @Override
    public Collection<Channel> getChannels() {
        return null;
    }

    @Override
    public Channel getChannel(InetSocketAddress remoteAddress) {
        return null;
    }

    @Override
    public io.netty.channel.ChannelHandler getChannelHandler() {
        return null;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return new InetSocketAddress(inetAddr, getPort());
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return new InetSocketAddress(candidateAddress, getPort());
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            return new InetSocketAddress(jdkSuppliedAddress, getPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void send(Object message) throws RemotingException {

    }

    @Override
    public void send(Object message, boolean sent) throws RemotingException {

    }

    @Override
    public void close() {
        try {
            if (channel != null) {
                // unbind.
                channel.close();
            }
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
        try {
            if (bootstrap != null) {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
    }

    @Override
    public void close(int timeout) {

    }

    @Override
    public void startClose() {

    }

    @Override
    public boolean isClosed() {
        return false;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
