package com.wz.netty.demo;/**
 * Created by wangzhen on 2016-08-18.
 */

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

/**
 * TODO
 *
 * @author wangzhen
 * @version 1.0
 * @date 2016-08-18 20:17
 **/
public class PooledByteBuffer {
    public static void main(String[] args) {
        int loop = 1000000;
        int length = 1024;
        ByteBuf buffer = null;
        Long currTime = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            // 在此使用堆缓冲区是为了更快速的访问缓冲区内的数组，如果使用堆外缓冲区会多一次内核向堆内存的内存拷贝，这样会降低性能。
            try {
                buffer = PooledByteBufAllocator.DEFAULT.heapBuffer(length);
                byte[] context = new byte[length];
                buffer.writeBytes(context);
                // 高效率访问堆缓冲区的方式，具体原因随后会讲。
            } finally {
                // 使用完成后一定要记得释放到内存池中。
                buffer.release();
            }
        }
        System.out.println("heapBuffer use time : " + (System.currentTimeMillis() - currTime));
        currTime = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            //在此使用堆外缓冲区是为了将数据更快速的写入内核中，如果使用堆缓冲区会多一次堆内存向内核进行内存拷贝，这样会降低性能。
            try {
                buffer = PooledByteBufAllocator.DEFAULT.directBuffer(length);
                byte[] context = new byte[length];
                buffer.writeBytes(context);
            } finally {
                // 必须释放自己申请的内存池缓冲区，否则会内存泄露。
                //outBuffer是Netty自身Socket发送的ByteBuf系统会自动释放，用户不需要做二次释放。
                buffer.release();
            }
        }
        System.out.println("directBuffer use time : " + (System.currentTimeMillis() - currTime));
        currTime = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            buffer = Unpooled.directBuffer(length);
            byte[] context = new byte[length];
            buffer.writeBytes(context);
        }
        System.out.println("Unpooled use time : " + (System.currentTimeMillis() - currTime));
    }
}
