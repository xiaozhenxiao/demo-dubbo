package com.wz.java.lambda;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;

/**
 * supplier accumulator combiner  finisher
 * wangzhen23
 * 2017/7/19.
 */
public class ReusingStreams<T> {
    public static void main(String[] args) {
        Stream<String> stream = Stream.of("d2", "a2", "b1", "b3", "c").filter(s -> s.startsWith("a"));

//        System.out.println("+++++++++++++" + stream.allMatch(s -> true));    // ok
        System.out.println("+++++++++++++" + stream.noneMatch(String::isEmpty));   // exception
//        System.out.println("+++++++++++++" + stream.anyMatch(s -> true));    // ok

        Supplier<Stream<String>> streamSupplier = () -> Stream.of("d2", "a2", "b1", "b3", "c").filter(s -> s.startsWith("a"));

        streamSupplier.get().anyMatch(s -> true);   // ok
        streamSupplier.get().noneMatch(s -> true);  // ok

        Objects.isNull(stream);
//        System.out.println("++++++++" + test().size());

        Predicate<Student> ss = Student::isStudent;
        Predicate<Student> sd = (student) -> true;


        List<String> str = Arrays.asList("a", "b", "A", "B");
        str.sort(String::compareToIgnoreCase);

        Function<String, Integer> stringToInteger = Integer::parseInt;
        BiPredicate<List<String>, String> contains = (list, element) -> list.contains(element);
        BiPredicate<List<String>, String> contains1 = List::contains;
//        Optional<String> optional = stream.findAny();


        List<String> words = new ArrayList<String>() {{
            add("Goodbye");
            add("World");
        }};
        //map 和flatMap
        List<Stream<String>> result = words.stream()
                .map(word -> word.split(""))
                .map(Arrays::stream)
                .distinct()
                .collect(toList());
        List<String> resultString = words.stream()
                .map(word -> word.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(toList());

        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(3, 4);
        List<int[]> pairs = numbers1.stream().flatMap(i ->
                numbers2.stream().filter(j -> (i + j) % 3 == 0).map(j -> new int[]{i, j})
        ).collect(toList());
        pairs.forEach(a -> System.out.println(a[0] + "," + a[1]));

        Stream<int[]> pythagoreanTriples = IntStream.rangeClosed(1, 100).boxed().flatMap(a ->
                IntStream.rangeClosed(a, 100).filter(b -> Math.sqrt(a * a + b * b) % 1 == 0).mapToObj(b ->
                        new int[]{a, b, (int) Math.sqrt(a * a + b * b)})
        );

        pythagoreanTriples.limit(5).forEach(t -> System.out.println(t[0] + ", " + t[1] + ", " + t[2]));

        long uniqueWords = 0;
        try (Stream<String> lines = Files.lines(Paths.get("data.txt"), Charset.defaultCharset())) {
            uniqueWords = lines.flatMap(line -> Arrays.stream(line.split(" "))).distinct().count();
        } catch (IOException e) {
        }
        Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]}).limit(20).forEach(t -> System.out.println("(" + t[0] + "," + t[1] + ")"));

        ReusingStreams rs = new ReusingStreams();
        System.out.println(rs.partitionPrimes(100));

    }
    public Map<Boolean, List<Integer>> partitionPrimes ( int n){
        return IntStream.rangeClosed(2, n).boxed()
                .collect(partitioningBy(candidate -> isPrime(candidate)));
    }
    public boolean isPrime(int candidate) {
        int candidateRoot = (int) Math.sqrt((double) candidate);
        return IntStream.rangeClosed(2, candidateRoot)
                .noneMatch(i -> candidate % i == 0);
    }

    public List<Student> test() {
        List<Student> studentList = new ArrayList<Student>() {{
            add(new Student());
            add(new Student());
        }};
        // Predicate返回了一个boolean
        Predicate<Student> p = s -> studentList.add(s);
        // Consumer 返回了一个void
        Consumer<Student> b = s -> studentList.add(s);
        //按年龄排序
        studentList.sort(comparing(Student::getAge));

        return studentList.stream().filter(s -> s.isGood()).collect(toList());
//        return studentList.stream().filter(s -> s.isGood()).anyMatch(Student::isStudent);
    }

    public BiConsumer<List<T>, T> accumulator(){
        return List::add;
    }
}
