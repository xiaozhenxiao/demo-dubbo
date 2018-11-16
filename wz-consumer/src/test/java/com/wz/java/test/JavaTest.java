package com.wz.java.test;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by wangzhen on 2016-08-05.
 */
public class JavaTest {
    public static void main(String[] args) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat format1 = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        try {
            Date date = format.parse("2016-06-15 13:30:10");
            String datas = format.format(new Date());
            Date date1 = format1.parse("2016-06-15 13:30:10");
            String datas1 = format1.format(date1);
            System.out.println(date);
            System.out.println(datas);
            System.out.println("========================");
            System.out.println(date1);
            System.out.println(datas1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long[] array = new long[]{5, 9, 6, 7, 8, 2, 4, 3, 1, 5, 7, 85, 42, 63, 54, 27, 45};
        Arrays.sort(array, 0, 10);
        for (int i = 0; i < array.length; i++) {
            long l = array[i];
            System.out.print(l + "\t");
        }
        System.out.println();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            list.add("num_" + i);
        }
//        for (String s : list) {
//            System.out.println(s);
//        }
        System.out.println("===========================");
        List<String> subList = list.subList(0, 11);
        for (int i = 0; i < subList.size(); i++) {
            System.out.println(subList.get(i));
        }

        //2875566949
        Integer sign = 0xAB65AB65;
        long sign1 = 0xAB65AB65FFFFFFFFL;
        System.out.println((sign1 >>> 32) + "***********************" + sign);
        long signLong = sign & 0xffffffffL;
        System.out.println(signLong + "-----------------------" + sign);

        System.out.println("#############" + Long.toHexString(signLong));
        System.out.println("#############" + Long.toUnsignedString(signLong, 16));
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.asIntBuffer().put(1);
        System.out.println(byteBuffer.array()[0] + " " + byteBuffer.array()[1] + " " + byteBuffer.array()[2] + " " + byteBuffer.array()[3]);
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        byteBuffer.asIntBuffer().put(1);
        System.out.println(byteBuffer.array()[0] + " " + byteBuffer.array()[1] + " " + byteBuffer.array()[2] + " " + byteBuffer.array()[3]);

        byte[] bytes = {(byte) 0xAB, (byte) 0x65, (byte) 0xAB, (byte) 0x65};
        ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
        long r = byteBuf.getUnsignedInt(0);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + r);

        int code = 0x80008002;
        int co = 0x00008002;
        System.out.println(" code > 0 " + (code > 0));
        System.out.println(" code < 0 " + (code < 0));
        System.out.println(" co > 0 " + (co > 0));
        System.out.println(" co < 0 " + (co < 0));

        String interfaceName = "com.wz.dubbo.api.DemoServiceAsync";
        String asyncInterfaceName = null;
        if (interfaceName.endsWith("Async")) {
            asyncInterfaceName = interfaceName;
            interfaceName = interfaceName.substring(0, interfaceName.length() - 5);
        }
        System.out.println(interfaceName + " = " + asyncInterfaceName);


    }
}
