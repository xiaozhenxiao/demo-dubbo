package com.jd.console;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 * wangzhen23
 * 2017/9/13.
 */
public class JavaConsole {
    public static void main(String[] args) throws InterruptedException {
        fillHeap(5000);
    }

    private static void fillHeap(int i) throws InterruptedException {
        List<OOMObject> list = new ArrayList<>();
        for (int j = 0; j < i; j++) {
             Thread.sleep(50);
             list.add(new OOMObject());
        }
        System.gc();
    }

    static class OOMObject{
        public byte[] placeholder = new byte[64 * 1024];
    }
}
