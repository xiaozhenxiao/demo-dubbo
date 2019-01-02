package com.smile.wz.processor;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 名称分析
 * wangzhen23
 * 2018/12/29.
 */
public class NameAnalysis {

    public static String analysis(String name) {
        Collection<List<String>> dic = Dictionary.dic.values();
        for (List<String> entry : dic) {
            for (String key : entry) {
                Pattern p = Pattern.compile(".*" + key + ".*");
                Matcher m = p.matcher(name.toLowerCase());
                if (m.matches()) {
                    return key;
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String result = analysis("cellularPhoneT");
        System.out.println("=========" + result);
    }
}
