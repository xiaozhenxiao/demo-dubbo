package com.wz.ali;

import java.io.IOException;

public class PalindromeSubstring {
    public static void main(String[] args) throws IOException {
        String testString = "aabccdefedcbaad";
        String palindromeString = palindromeSubstring(testString);
        System.out.println("===========" + palindromeString);
        System.out.println("-----------" + getLongPalindromicSubstring(testString));
    }

    public static String palindromeSubstring(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        int length = s.length();
        String resultString = String.valueOf(s.charAt(0));
        for (int i = 0; i < length - 1; i++) {
            String sub1 = getString(s, i, i);
            String sub2 = getString(s, i, i + 1);
            String subString = sub1.length() > sub2.length() ? sub1 : sub2;
            if (subString.length() > resultString.length()) {
                resultString = subString;
            }
        }
        return resultString;
    }

    public static String getString(String s, int start, int end) {
        while (s.charAt(start) == s.charAt(end)) {
            start = start - 1;
            end = end + 1;
            if (start < 0 || end >= s.length()) {
                return s.substring(start + 1, end);
            }
        }
        return s.substring(start + 1, end);
    }

    public static String getLongPalindromicSubstring(String str) {
        // 字符串为空或只有一个字符直接返回
        if (str.length() == 0 || str.length() < 2)
            return str;
        int len = str.length();
// TreeMap<Integer,String> PSMap = new TreeMap<Integer, String>();
        boolean[] preResult = new boolean[len];  // 存储上一次的计算结果
        int start = 0, end = 0, maxLen = 0;// 记录最长的回文子串的起始和结束索引
        for (int j = 0; j < str.length(); j++) {
            for (int i = 0; i <= j; i++) {
// 对应数学模型 - F(i , i) = true
                if (i == j) {
                    preResult[i] = true;
                    if (1 > maxLen) {
                        start = i;
                        end = j;
                        maxLen = 1;
                    }
//PSMap.put(1, String.valueOf(str.charAt(i)));
                }
// 对应数学模型 - F(i, i + 1) = true （S[i] == S[i+1]）
                else if (j == i + 1 && str.charAt(i) == str.charAt(j)) {
                    preResult[i] = true;
                    if (2 > maxLen) {
                        start = i;
                        end = j;
                        maxLen = 2;
                    }
// PSMap.put(2,str.substring(i,j+1));
                }
                // 对应数学模型 - F(i, j) = true （F(i + 1, j -1) && S[i] == S[j]）
                else if (preResult[i + 1] && str.charAt(i) == str.charAt(j)) {
                    preResult[i] = true;
                    if (j - i + 1 > maxLen) {
                        start = i;
                        end = j;
                        maxLen = j - i + 1;
                    }
                    //PSMap.put(j-i+1,str.substring(i,j+1));
                } else {
                    preResult[i] = false;
                }
            }
        }
        //String result = PSMap.lastEntry().getValue();
        String result = str.substring(start, end + 1);
        return result;
    }
}
