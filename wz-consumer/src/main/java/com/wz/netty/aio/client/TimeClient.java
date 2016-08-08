package com.wz.netty.aio.client;

/**
 * @author lilinfeng
 * @date 2014年2月14日
 * @version 1.0
 */
public class TimeClient {

    /**
     * @param args
     */
    public static void main(String[] args) {
	int port = 8088;
	if (args != null && args.length > 0) {
	    try {
		port = Integer.valueOf(args[0]);
	    } catch (NumberFormatException e) {
		// 采用默认值
	    }

	}
	new Thread(new AsyncTimeClientHandler("127.0.0.1", port),
		"AIO-AsyncTimeClientHandler-001").start();

    }
}
