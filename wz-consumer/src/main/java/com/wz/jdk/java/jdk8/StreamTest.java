package com.wz.jdk.java.jdk8;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by wangzhen on 2016-08-05.
 */
public class StreamTest {
    public static void main(String[] args) {
        /**
         * 一个 Stream 只可以使用一次
         */
        IntStream stream = IntStream.of(new int[]{1, 2, 3});
//        stream.forEach(System.out::print);同时只能运行一个
        OptionalInt max = stream.max();
        System.out.println(max);

        IntStream.of(new int[]{1, 2, 3}).forEach(System.out::print);
        System.out.println("\n=====================");
        IntStream.range(1, 3).forEach(System.out::print);
        System.out.println("\n=====================");
        IntStream.rangeClosed(1, 3).forEach(System.out::print);
        System.out.println("\n=====================");

        List<String> resultList = Stream.of("one", "two", "three", "four")
                .filter(e -> e.length() > 3)
                .peek(e -> System.out.println("Filtered value: " + e))
//                .map(String::toUpperCase)
                .map(e -> e.toUpperCase())
                .peek(e -> System.out.println("Mapped value: " + e))
                .collect(Collectors.toList());

        String strA = " abcd ", strB = null;
        print(strA);
        print("");
        print(strB);
        getLength(strA);
        getLength("");
        getLength(strB);
    }
    public static void print(String text) {
        // Java 8
        Optional.ofNullable(text).ifPresent(System.out::println);
        // Pre-Java 8
        if (text != null) {
            System.out.println(text);
        }
    }
    public static int getLength(String text) {
        // Java 8
        return Optional.ofNullable(text).map(String::length).orElse(-1);
        // Pre-Java 8
        // return if (text != null) ? text.length() : -1;
    }
}

