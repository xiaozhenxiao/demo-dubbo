package com.wz.java.designpatterns.basicreactor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Administrator on 2016/8/13.
 * Date:2016-08-13
 */
class Reactor implements Runnable {
    final Selector selector;
    final ServerSocketChannel serverSocket;
    Reactor(int port) throws IOException { //Reactor初始化
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false); //非阻塞
        SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT); //分步处理,第一步,接收accept事件
        sk.attach(new Acceptor()); //attach callback object, Acceptor
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                selector.select();
                Set selected = selector.selectedKeys();
                Iterator it = selected.iterator();
                while (it.hasNext())
                    dispatch((SelectionKey)(it.next())); //Reactor负责dispatch收到的事件
                selected.clear();
            }
        } catch (IOException ex) { /* ... */ }
    }

    void dispatch(SelectionKey k) {
        Runnable r = (Runnable)(k.attachment()); //调用之前注册的callback对象
        if (r != null)
            r.run();
    }

    public static void main(String[] args) {
        int port = 8088;
        try {
            new Thread(new Reactor(port)).start();
            System.out.println("Reactor 服务启动……");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Acceptor implements Runnable { // inner
        public void run() {
            try {
                SocketChannel c = serverSocket.accept();
                if (c != null)
                    new Handler(selector, c);
            }
            catch(IOException ex) { /* ... */ }
        }
    }
}

final class Handler implements Runnable {
    private static final int MAXIN = 1024;
    ByteBuffer output;
    final SocketChannel socket;
    final SelectionKey sk;
    final Selector selector;
    static final int READING = 0, SENDING = 1;
    int state = READING;

    Handler(Selector sel, SocketChannel c) throws IOException {
        selector = sel;
        socket = c; c.configureBlocking(false);
        // Optionally try first read now
        sk = socket.register(sel, 0);
        sk.attach(this); //将Handler作为callback对象
        sk.interestOps(SelectionKey.OP_READ); //第二步,接收Read事件
        sel.wakeup();
    }
    boolean inputIsComplete() { /* ... */ return true;}
    boolean outputIsComplete() { /* ... */ return true;}
    String process(ByteBuffer input) { /* ... */
        input.flip();
        byte[] bytes = new byte[input.remaining()];
        input.get(bytes);
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "error";
    }

    public void run() {
        try {
            if (state == READING) read();
            else if (state == SENDING) send();
        } catch (IOException ex) { /* ... */ }
    }

    void read() throws IOException {
        ByteBuffer input = ByteBuffer.allocate(MAXIN);
        socket.read(input);
        if (inputIsComplete()) {
            String body = process(input);
            System.out.println("recive message:" + body);
            state = SENDING;
            // Normally also do first write now
            sk.interestOps(SelectionKey.OP_WRITE); //第三步,接收write事件

            String res = "QUERY TIME ORDER"
                    .equalsIgnoreCase(body) ? new java.util.Date(
                    System.currentTimeMillis()).toString()
                    : "BAD ORDER";
            output = ByteBuffer.allocate(res.length());
            output.put(res.getBytes());
            selector.wakeup();
        }
    }
    void send() throws IOException {
        output.flip();
        socket.write(output);
        if (outputIsComplete()){
            sk.cancel(); //write完就结束了, 关闭select key
        }
    }
}

//上面 的实现用Handler来同时处理Read和Write事件, 所以里面出现状态判断
//我们可以用State-Object pattern来更优雅的实现
/*class Handler {
    // ...
    final Socket socket;

    Handler(Socket s) {
        socket = s;
    }

    public void run() { // initial state is reader
        socket.read(input);
        if (inputIsComplete()) {
            process();
            sk.attach(new Sender());  //状态迁移, Read后变成write, 用Sender作为新的callback对象
            sk.interest(SelectionKey.OP_WRITE);
            sk.selector().wakeup();
        }
    }
    class Sender implements Runnable {
        public void run(){ // ...
            socket.write(output);
            if (outputIsComplete()) sk.cancel();
        }
    }
}*/
