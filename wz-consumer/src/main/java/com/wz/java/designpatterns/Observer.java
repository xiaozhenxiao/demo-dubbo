package com.wz.java.designpatterns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzhen on 2016-07-06.
 */
public class Observer {
    public static void main(String[] args)
    {
        Watched girl = new ConcreteWatched();

        Watcher watcher1 = new ConcreteWatcher("张三");
        Watcher watcher2 = new ConcreteWatcher("李四");
        Watcher watcher3 = new ConcreteWatcher("王五");

        girl.addWatcher(watcher1);
        girl.addWatcher(watcher2);
        girl.addWatcher(watcher3);

        girl.notifyWatchers("开心");
    }

}

//抽象观察者角色
interface Watcher {
    public void update(String str);

}

//抽象主题角色，watched：被观察
interface Watched {
    public void addWatcher(Watcher watcher);

    public void removeWatcher(Watcher watcher);

    public void notifyWatchers(String str);

}

class ConcreteWatcher implements Watcher {
    private String name;

    public ConcreteWatcher(String name) {
        this.name = name;
    }

    public ConcreteWatcher() {
    }

    public void update(String str) {
        System.out.println(name + "  " + str);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class ConcreteWatched implements Watched {
    // 存放观察者
    private List<Watcher> list = new ArrayList<Watcher>();

    public void addWatcher(Watcher watcher) {
        list.add(watcher);
    }

    public void removeWatcher(Watcher watcher) {
        list.remove(watcher);
    }

    public void notifyWatchers(String str) {
        // 自动调用实际上是主题进行调用的
        for (Watcher watcher : list) {
            watcher.update(str);
        }
    }

}
