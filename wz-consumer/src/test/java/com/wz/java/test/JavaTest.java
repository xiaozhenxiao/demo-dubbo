package com.wz.java.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

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
        long[] array = new long[]{5,9,6,7,8,2,4,3,1,5,7,85,42,63,54,27,45};
        Arrays.sort(array, 0, 10);
        for (int i = 0; i < array.length; i++) {
            long l = array[i];
            System.out.print(l+ "\t");
        }
    }
}
