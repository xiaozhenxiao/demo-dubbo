package com.wz.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangzhen
 * @version 1.0
 * @date 2016-10-20 15:44
 **/

public class ZkClient {
    private static List<ACL> acl = new ArrayList<ACL>();
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException, NoSuchAlgorithmException {
        // 创建一个与服务器的连接
        ZooKeeper zk = new ZooKeeper("localhost:2181", 6000, new Watcher() {
            // 监控所有被触发的事件
            public void process(WatchedEvent event) {
                System.out.println("已经触发了" + event.getType() + "事件！");
            }
        });
        String authString = "xiao:xiao";
        zk.addAuthInfo("digest", authString.getBytes());
        acl.add(new ACL(ZooDefs.Perms.ALL, new Id("digest", DigestAuthenticationProvider.generateDigest(authString))));
        acl.add(new ACL(ZooDefs.Perms.READ, ZooDefs.Ids.ANYONE_ID_UNSAFE));
        // 创建一个目录节点
        zk.create("/testRootPath", "testRootData".getBytes(), acl, CreateMode.PERSISTENT);
        // 创建一个子目录节点
        zk.create("/testRootPath/testChildPathOne", "testChildDataOne".getBytes(), acl,CreateMode.PERSISTENT);
        System.out.println("testRootPath data:" + new String(zk.getData("/testRootPath",false,null)));
        // 取出子目录节点列表
        System.out.println("取出子目录节点列表:" + zk.getChildren("/testRootPath",true));
        // 修改子目录节点数据
        zk.setData("/testRootPath/testChildPathOne","modifyChildDataOne".getBytes(),-1);
        System.out.println("目录节点状态：["+zk.exists("/testRootPath",true)+"]");
        // 创建另外一个子目录节点
        zk.create("/testRootPath/testChildPathTwo", "testChildDataTwo".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        System.out.println(new String(zk.getData("/testRootPath/testChildPathTwo",true,null)));

        // 取出子目录节点列表
        System.out.println("取出子目录节点列表:" + zk.getChildren("/testRootPath",true));

        // 删除子目录节点
        zk.delete("/testRootPath/testChildPathTwo",-1);
        zk.delete("/testRootPath/testChildPathOne",-1);
        // 删除父目录节点
        zk.delete("/testRootPath",-1);
        // 关闭连接
        zk.close();
    }
}
