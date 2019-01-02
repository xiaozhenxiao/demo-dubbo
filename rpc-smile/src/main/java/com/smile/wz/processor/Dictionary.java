package com.smile.wz.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典类
 * wangzhen23
 * 2018/12/29.
 */
public class Dictionary {
    public static final Map<String, List<String>> dic = new HashMap<>();

    static{
        List<String> pl = new ArrayList<String>(){{add("phone");}};
        List<String> ml = new ArrayList<String>(){{add("mail");}};
        dic.put("phone", pl);
        dic.put("mail", ml);
    }
}