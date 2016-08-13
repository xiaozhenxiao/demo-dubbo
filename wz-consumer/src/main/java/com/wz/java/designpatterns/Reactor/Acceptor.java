package com.wz.java.designpatterns.Reactor;

import java.io.IOException;
import java.nio.channels.SocketChannel;
/**
 * Created by Administrator on 2016/8/13.
 * Date:2016-08-13
 */
public class Acceptor implements Runnable {
    private Reactor reactor;

    public Acceptor(Reactor reactor) {
        this.reactor = reactor;
    }

    @Override
    public void run() {
        try {
            SocketChannel socketChannel = reactor.serverSocketChannel.accept();
            if (socketChannel != null)// 调用Handler来处理channel
                new SocketReadHandler(reactor.selector, socketChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}