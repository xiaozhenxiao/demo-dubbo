package com.jd.util.encrypt;

import java.util.Date;
import java.util.zip.ZipEntry;


public class EncryptZipEntry extends ZipEntry {
    String name;
    long time = -1L;
    long crc = -1L;
    long size = -1L;
    long csize = -1L;
    int method = -1;
    byte[] extra;
    String comment;
    int flag;
    int version;
    long offset;


    public EncryptZipEntry(String name) {
        super(name);
        this.name = name;

    }


    public void setTime(long time) {
        this.time = javaToDosTime(time);
    }


    @SuppressWarnings("deprecation")
    private static long javaToDosTime(long time) {

        Date d = new Date(time);

        int year = d.getYear() + 1900;

        if (year < 1980) {

            return 2162688L;

        }

        return (year - 1980 << 25 | d.getMonth() + 1 << 21 |
                d.getDate() << 16 |
                d.getHours() << 11 |
                d.getMinutes() << 5 |
                d.getSeconds() >> 1);

    }

}