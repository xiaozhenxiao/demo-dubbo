package com.wz.web.redis;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * Created by wangzhen on 2016-07-12.
 */
public class RedisClusterClient {
//    @Resource
    private RedisTemplate redisTemplate;

    public void set(final byte[] key, final byte[] value, final long liveTime) {
        redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                connection.set(key, value);
                if (liveTime > 0) {
                    connection.expire(key, liveTime);
                }
                return 1L;
            }
        });
    }
    public byte[] get(final byte[] key) {
        return (byte[])redisTemplate.execute(new RedisCallback(){
            public byte[] doInRedis(RedisConnection connection)
                    throws DataAccessException {
                return connection.get(key);
            }
        });
    }
    public Long del(final String...keys) {
        return (Long)redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                Long result = 0L;
                for (int i = 0; i < keys.length; i++) {
                    result = connection.del(keys[i].getBytes());
                }
                return result;
            }
        });
    }
}
