package com.wz.jdk.java;

/**
 * Created by Administrator on 2016/6/25.
 * Date:2016-06-25
 */
public class JavaBinding {

    public static void main(String[] args) {
        Father father = new Son();
        System.out.println(father.name);
        father.getName();
        father.getNick();
    }
}

class Father{
    public String name = "father";
    protected int i = 0;

    public Father(){
        System.out.println("Father constructor!");
    }

    public String getName(){
        System.out.println("Father's name is " + this.name + this.getClass());
        return name;
    }
    public String getNick(){
        System.out.println(name + " Father's Nick name is " + getName() + " " + this.getClass());
        return name;
    }
    public void increase(){
        System.out.println("increase in father");
    }
    public int printI(){
        return i;
    }
}

class Son extends Father{
    public String name = "son";

    public Son(){
        System.out.println("Son constructor!");
    }

    public String getName(){
        System.out.println("Son's name is " + this.name + " " + this.getClass());
        return name;
    }

    public String getName(String name){
        System.out.println("Son's Overload name is " + name + this.getClass());
        return name;
    }
    public void increase(){
        i++;
    }
    public int printI() {
        return i;
    }
    public void setName(String name){
        this.name = name;
    }
}

class Daughter extends Father{
    public void increase(){
        i += 10;
    }
    public int printI() {
        return i;
    }
}
