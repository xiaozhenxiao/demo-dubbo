package com.wz.java.generic;

/**
 *
 * @param <E>
 */
public class Apple<E> extends Fruit{
    private E e;

    public Apple(E ee){
        e = ee;
    }

    public <T> T delete(T fruit){
        System.out.println("=========delete=========" + e);
        System.out.println("=========delete=========");

        return fruit;
    }
}
