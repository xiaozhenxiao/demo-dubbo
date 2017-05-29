package com.wz.java.lambda;

import java.util.Random;

/**
 * TODO
 *
 * @author wangzhen
 * @version 1.0
 * @date 2017-03-12 17:45
 **/

public class Contact {
    private String name;
    private String firstName;
    private String lastName;
    private long number;
    private Random random;

    public Contact() {
        random = new Random();
    }

    public String getName() {
        return name;
    }

    public Contact setName(String name) {
        this.name = name;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Contact setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Contact setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public long getNumber() {
        return number;
    }

    public Contact setNumber(long number) {
        this.number = number;
        return this;
    }

    public boolean call() {
        return random.nextBoolean();
    }
}
