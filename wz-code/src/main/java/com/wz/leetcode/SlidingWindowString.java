package com.wz.leetcode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。
 * 示例 1:
 * 输入: "abcabcbb"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 * 示例 2:
 * 输入: "bbbbb"
 * 输出: 1
 * 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
 * 示例 3:
 * 输入: "pwwkew"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
 *      请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
 */
public class SlidingWindowString {

    public static void main(String[] args) {
        String inString = "abcabcdebbabc";

        System.out.println("=================" + lengthOfLongestSubstring(inString));
        System.out.println("=================" + lengthOfLongestSubstring0(inString));
    }

    public static int lengthOfLongestSubstring(String s) {
        int n = s.length(), ans = 0;
        Map<Character, Integer> map = new HashMap<>();
        for (int end = 0, start = 0; end < n; end++) {
            char alpha = s.charAt(end);
            if (map.containsKey(alpha)) {
                start = Math.max(map.get(alpha), start);
            }
            ans = Math.max(ans, end - start + 1);
            map.put(s.charAt(end), end + 1);
        }
        return ans;
    }

    static int lengthOfLongestSubstring0(String s) {
        int n = s.length();
        Set<Character> set = new HashSet<>();
        int ans = 0, start = 0, end = 0;
        while (start < n && end < n) {
            if (!set.contains(s.charAt(end))) {
                set.add(s.charAt(end++));//如果不包含，j就自增
                ans = Math.max(ans, end - start);//j - i = 最大的不重复的长度。
            } else {
                set.remove(s.charAt(start++));//如果包含，i就增,并把窗口后滑
            }
        }
        return ans;
    }

}
