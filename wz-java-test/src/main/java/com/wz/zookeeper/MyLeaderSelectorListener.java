package com.wz.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

/**
 * TODO
 * wangzhen23
 * 2018/1/15.
 */
public class MyLeaderSelectorListener extends LeaderSelectorListenerAdapter {

    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {
        System.out.println(Thread.currentThread().getName() + " 获取到leader权限后执行！");
        while (true){
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + "takeLeadership exec……");
        }
    }
}
