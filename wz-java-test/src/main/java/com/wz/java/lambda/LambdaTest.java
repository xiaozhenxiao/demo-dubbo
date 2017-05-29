package com.wz.java.lambda;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author wangzhen
 * @version 1.0
 * @date 2017-03-12 17:15
 **/

public class LambdaTest {
    public static void main(String[] args) {
        /*ArrayList<String> contactList = Lists.newArrayList();
        List<Contact> contacts = Lists.newArrayList();
        contactList.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                Contact contact = new Contact();
                contact.setName(s);
                contacts.add(contact);
            }
        });

        long validContactCounter =
                contactList.stream()
                        .map(s -> new Contact().setName(s))
                        .filter(c -> c.call())
                        .count();*/

        List<Contact> contacts = new ArrayList<Contact>() {{
            add(new Contact().setFirstName("Foo").setLastName("Jack"));
            add(new Contact().setFirstName("Bar").setLastName("Ma"));
            add(new Contact().setFirstName("Olala").setLastName("Awesome"));
        }};

        Comparator<Contact> byFirstName = new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return Character.compare(o1.getFirstName().charAt(0), o2.getFirstName().charAt(0));
            }
        };

        //--- Using Lambda form ---//
        Comparator<Contact> byFirstNameLambdaForm = (o1, o2) ->
                Character.compare(o1.getFirstName().charAt(0), o2.getFirstName().charAt(0));

        Function<Contact, Character> keyExtractor = o -> o.getFirstName().charAt(0);
        Comparator<Character> keyComparator = (c1, c2) -> Character.compare(c1, c2);
        Comparator<Contact> byFirstNameAdvanced = (o1, o2) -> keyComparator.compare(keyExtractor.apply(o1), keyExtractor.apply(o2));

        Comparator<Contact> compareByFirstName = Comparator.comparing(keyExtractor);
        Comparator<Contact> compareByNameLength = Comparator.comparing(p -> (p.getFirstName() + p.getLastName()).length());

        contacts.stream()
//                .sorted(compareByNameLength)
                .sorted(byFirstNameAdvanced)
                .forEach(c -> System.out.println(c.getFirstName() + " " + c.getLastName()));
    }
}
