package com.objcoding.flymq.utils;

import com.objcoding.flymq.config.RedisConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenghui.zhang on 2018/1/7.
 */
public class RedisUtil {

    private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    private static Map<String, JedisPool> maps = new HashMap<>();

    private static JedisPool JEDISPOOL;

    private RedisUtil() {

    }

    /**
     * 初始化redis连接池
     *
     * @param redisConfig 配置类
     */
    public static void initPool(RedisConfig redisConfig) {
        String key = redisConfig.getHost() + ":" + redisConfig.getPort();
        JedisPool pool = null;
        if (!maps.containsKey(key)) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(redisConfig.getMaxActive());
            config.setMaxIdle(redisConfig.getMaxIdle());
            config.setMaxWaitMillis(redisConfig.getMaxWait());
            config.setTestOnBorrow(true);
            config.setTestOnReturn(true);
            try {
                pool = new JedisPool(config, redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeout());
                maps.put(key, pool);
            } catch (Exception e) {
                logger.error(" >>>>>> redis连接池获取失败 >>>>>>>>");
                e.printStackTrace();
            }
        } else {
            pool = maps.get(key);
        }
        RedisUtil.JEDISPOOL = pool;
    }

    /**
     * 获取redis连接
     */
    public static Jedis getConnect() throws Exception {
        Jedis jedis;
        int count = 0;
        do {
            try {
                jedis = JEDISPOOL.getResource();
            } catch (Exception e) {
                logger.error("get redis master1 failed!", e);
                throw new Exception("获取Redis实例失败");
            }
            count++;
        } while (jedis == null && count < 10);

        return jedis;
    }

}
