package com.wz.java.lambda;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * supplier accumulator combiner  finisher
 *
 * supplier:是一个容器提供者，提供容器A,比如：List list = new ArrayList()
 *
 * accumulator:是要操作的集合的每个元素以怎样的形式添加到supplier提供的容器A当中，即做累加操作，比如：List.add(item)
 *
 * combiner:用于在多线程并发的情况下，每个线程都有一个supplier和，如果有N个线程那么就有N个supplier提供的容器A，
 * 执行的是类似List.addAll(listB)这样的操作,只有在characteristics没有被设置成CONCURRENT并且是并发的情况下 才会被调用。
 * ps：characteristics被设置成CONCURRENT时，整个收集器只有一个容器，而不是每个线程都有一个容器，此时combiner()方法不会被调用，
 * 这种情况会出现java.util.ConcurrentModificationException异常,此时需要使用线程安全的容器作为supplier返回的对象。
 *
 * finisher:是终止操作，如果收集器的characteristics被设置成IDENTITY_FINISH，那么会将中间集合A牵制转换为结果R类型，
 * 如果A和R没有父子之类的继承关系，会报类型转换失败的错误，如果收集器的characteristics没有被设置成IDENTITY_FINISH，那么finisher()方法会被调用，返回结果类型R。
 *
 * wangzhen23
 * 2017/7/19.
 */
public class AdvancedOperations {
    private static List<Person> persons =
            Arrays.asList(
                    new Person("Max", 18),
                    new Person("Peter", 23),
                    new Person("Pamela", 23),
                    new Person("David", 12));

    public static void main(String[] args) {
        persons.stream().anyMatch(Person::isStudent);

        List<Person> filtered = persons.stream().filter(p -> p.name.startsWith("P")).collect(Collectors.toList());
        System.out.println(filtered);    // [Peter, Pamela]

        Map<Integer, List<Person>> personsByAge = persons.stream().collect(Collectors.groupingBy(p -> p.age));
        personsByAge.forEach((age, p) -> System.out.format("age %s: %s\n", age, p));
        // age 18: [Max]
        // age 23: [Peter, Pamela]
        // age 12: [David]

        Double averageAge = persons.stream().collect(Collectors.averagingInt(p -> p.age));
        System.out.println(averageAge);     // 19.0

        IntSummaryStatistics ageSummary = persons.stream().collect(Collectors.summarizingInt(p -> p.age));
        System.out.println(ageSummary);
        // IntSummaryStatistics{count=4, sum=76, min=12, average=19.000000, max=23}

        String phrase = persons.stream().filter(p -> p.age >= 18).map(p -> p.name).collect(Collectors.joining(" and ", "In Germany ", " are of legal age."));
        System.out.println(phrase);
        // In Germany Max and Peter and Pamela are of legal age.

        Map<Integer, String> map = persons.stream().collect(Collectors.toMap(p -> p.age, p -> p.name, (name1, name2) -> name1 + ";" + name2));
        System.out.println(map);
        // {18=Max, 23=Peter;Pamela, 12=David}

        Collector<Person, StringJoiner, String> personNameCollector =
                Collector.of(
                        () -> new StringJoiner(" | "),          // supplier
                        (j, p) -> j.add(p.name.toUpperCase()),  // accumulator
                        (j1, j2) -> j1.merge(j2),               // combiner
                        StringJoiner::toString);                // finisher
        String names = persons.stream().collect(personNameCollector);
        System.out.println(names);  // MAX | PETER | PAMELA | DAVID

        List<Foo> foos = new ArrayList<>();
        // create foos
        IntStream.range(1, 4).forEach(i -> foos.add(new Foo("Foo" + i)));
        // create bars
        foos.forEach(f -> IntStream.range(1, 4).forEach(i -> f.bars.add(new Bar("Bar" + i + " <- " + f.name))));
        foos.stream().flatMap(f -> f.bars.stream()).forEach(b -> System.out.println(b.name));
        // Bar1 <- Foo1
        // Bar2 <- Foo1
        // Bar3 <- Foo1
        // Bar1 <- Foo2
        // Bar2 <- Foo2
        // Bar3 <- Foo2
        // Bar1 <- Foo3
        // Bar2 <- Foo3
        // Bar3 <- Foo3

        Optional.of(new Outer())
                .flatMap(o -> Optional.ofNullable(o.nested))
                .flatMap(n -> Optional.ofNullable(n.inner))
                .flatMap(i -> Optional.ofNullable(i.foo))
                .ifPresent(System.out::println);
    }
}

class Foo {
    String name;
    List<Bar> bars = new ArrayList<>();

    Foo(String name) {
        this.name = name;
    }
}

class Bar {
    String name;

    Bar(String name) {
        this.name = name;
    }
}

class Outer {
    Nested nested;
}

class Nested {
    Inner inner;
}

class Inner {
    String foo;
}
