package com.wz.jdk.java;

/**
 * Created by Administrator on 2016/6/25.
 * Date:2016-06-25
 */
public class FatherSon {

    public static void main(String[] args) {
        Father father = new Son();
        System.out.println(father.name);
        father.getName();
        father.getNick("FatherSon");
    }
}

class Father{
    public String name = "father";

    public Father(){
        System.out.println("Father constructor!");
    }

    public String getName(){
        System.out.println("Father's name is " + this.name + this.getClass());
        return name;
    }
    public String getNick(String name){
        System.out.println("Father's Nick name is " + name + this.getClass());
        return name;
    }
}

class Son extends Father{
    public String name = "son";

    public Son(){
        System.out.println("Son constructor!");
    }

    public String getName(){
        System.out.println("Son's name is " + this.name + this.getClass());
        return name;
    }

    public String getName(String name){
        System.out.println("Son's Overload name is " + name + this.getClass());
        return name;
    }
}
