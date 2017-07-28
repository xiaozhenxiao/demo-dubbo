package com.wz.java.lambda.nullpointer;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * wangzhen23
 * 2017/7/19.
 */
public class NullPointer {
    public static void main(String[] args) {
        Outer outer = new Outer();
        //常规判断null指针的做法
        if (outer != null && outer.nested != null && outer.nested.inner != null) {
            System.out.println(outer.nested.inner.foo);
        }

        Optional.of(new Outer()).map(Outer::getNested).map(Nested::getInner).map(Inner::getFoo).ifPresent(System.out::println);

        Outer obj = new Outer();
        resolve(() -> obj.getNested().getInner().getFoo()).ifPresent(System.out::println);

    }

    public static <T> Optional<T> resolve(Supplier<T> resolver) {
        try {
            T result = resolver.get();
            return Optional.ofNullable(result);
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

}

class Outer {
    Nested nested;

    Nested getNested() {
        return nested;
    }
}

class Nested {
    Inner inner;

    Inner getInner() {
        return inner;
    }
}

class Inner {
    String foo;

    String getFoo() {
        return foo;
    }
}
