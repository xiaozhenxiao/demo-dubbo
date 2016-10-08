package com.wz.java.hash;

/**
 * @author wangzhen
 * @version 1.0
 * @date 2016-10-08 14:17
 **/
public interface HashFunction {
    /**
     * 加法hash
     *
     * @param key   字符串
     * @param prime 一个质数
     * @return hash结果
     */
    int additiveHash(String key, int prime);

    /**
     * 旋转hash
     *
     * @param key   输入字符串
     * @param prime 质数
     * @return hash值
     */
    int rotatingHash(String key, int prime);

    /**
     * 一次一个hash
     *
     * @param key 输入字符串
     * @return 输出hash值
     */
    int oneByOneHash(String key);

    /**
     * Bernstein's hash
     *
     * @param key   输入字节数组
     * @return 结果hash
     */
    int bernstein(String key);

    /**
     * Universal Hashing
     */
    int universal(char[] key, int mask, int[] tab);

    /**
     * Zobrist Hashing
     */
    int zobrist(char[] key, int mask, int[][] tab);

    /**
     * 32位的FNV算法
     *
     * @param data 数组
     * @return int值
     */
    int FNVHash(byte[] data);

    /**
     * 改进的32位FNV算法1
     *
     * @param data 数组
     * @return int值
     */
    int FNVHash1(byte[] data);

    /**
     * 改进的32位FNV算法1
     *
     * @param data 字符串
     * @return int值
     */
    int FNVHash1(String data);

    /**
     * Thomas Wang的算法，整数hash
     */
    int intHash(int key);

    /**
     * RS算法hash
     *
     * @param str 字符串
     */
    int RSHash(String str);

    /**
     * JS算法
     */
    int JSHash(String str);
    /**
     * PJW算法
     */
    int PJWHash(String str);
;
    /**
     * ELF算法
     */
    int ELFHash(String str);

    /**
     * BKDR算法
     */
    int BKDRHash(String str);

    /**
     * SDBM算法
     */
    int SDBMHash(String str);

    /**
     * DJB算法
     */
    int DJBHash(String str);

    /**
     * DEK算法
     */
    int DEKHash(String str);

    /**
     * AP算法
     */
    int APHash(String str);

    /**
     * JAVA自己带的算法
     */
    int java(String str);

    /**
     * 混合hash算法，输出64位的值
     */
    long mixHash(String str);
}
