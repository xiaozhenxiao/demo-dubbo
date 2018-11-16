package com.wz.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Leader Election
 * wangzhen23
 * 2018/1/15.
 */
public class CuratorDemo {
    public static void main(String[] args) {
        String leaderSelectorPath = "/leaderSelect";
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181,127.0.0.1:2182")
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        LeaderSelector leaderSelector = new LeaderSelector(client, leaderSelectorPath, new MyLeaderSelectorListener());
        leaderSelector.autoRequeue();

        client.start();
        leaderSelector.start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "===================================" + leaderSelector.hasLeadership());
        int i = 0;
        while (true) {
            if (i++ == 100)
                System.out.println(Thread.currentThread().getName() + "===================================" + leaderSelector.hasLeadership());
        }
    }
}
