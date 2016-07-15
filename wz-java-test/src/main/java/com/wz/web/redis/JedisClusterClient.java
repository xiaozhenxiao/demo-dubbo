package com.wz.web.redis;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by wangzhen on 2016-07-12.
 */
public class JedisClusterClient {
    private static JedisCluster jc;

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(100);
        config.setMaxIdle(100);
        config.setMinIdle(100);
        config.setMaxWaitMillis(6 * 1000);
        // config.setMaxWait(1000 * 4);
        config.setTestOnBorrow(true);
        Set<HostAndPort> jedisClusterNodes = new HashSet<>();

        jedisClusterNodes.add(new HostAndPort("10.125.31.7", 6379));
        jedisClusterNodes.add(new HostAndPort("10.125.31.7", 6380));
        jedisClusterNodes.add(new HostAndPort("10.125.31.7", 6381));
        jedisClusterNodes.add(new HostAndPort("10.125.31.8", 6379));
        jedisClusterNodes.add(new HostAndPort("10.125.31.8", 6380));
        jedisClusterNodes.add(new HostAndPort("10.125.31.8", 6381));

        jc = new JedisCluster(jedisClusterNodes, 2000, 2, config);

    }

    public static void main(String[] args) {
        try {
			/*for (int i = 0; i < 50; i++) {
				long t1 = System.currentTimeMillis();
				jc.set("" + i, "" + i);
				long t2 = System.currentTimeMillis();
				String value = jc.get("" + i);
				long t3 = System.currentTimeMillis();
				System.out.println("" + value);
				System.out.println((t2 - t1) + "mills");
				System.out.println((t3 - t2) + "mills");
			}*/

            // list
            for (int i = 0; i < 50; i++) {
                long t1 = System.currentTimeMillis();
                jc.lpush("Xist", "" + i);
                long t2 = System.currentTimeMillis();
//				String value = jc.rpop("list");
                long t3 = System.currentTimeMillis();
//				System.out.println("" + value);
                System.out.println((t2 - t1) + "mills");
                System.out.println((t3 - t2) + "mills");
            }

            // hash
//			for (int i = 0; i < 50; i++) {//
//				long t1 = System.currentTimeMillis();
//				jc.hset("hash", "" + i, "hash_" + i);
//				long t2 = System.currentTimeMillis();
//				String value = jc.hget("hash", "" + i);
//				long t3 = System.currentTimeMillis();
//				System.out.println("" + value);
//				System.out.println((t2 - t1) + "mills");
//				System.out.println((t3 - t2) + "mills");
//			}

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());

        } finally {
            System.out.println("jedis finished");
            //TODO
        }

    }
}
