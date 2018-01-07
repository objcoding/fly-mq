package cn.zhangchenghui.redismq.utils;

import cn.zhangchenghui.redismq.config.RedisConfig;
import cn.zhangchenghui.redismq.message.Message;
import com.alibaba.fastjson.JSON;
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

    private static Map<String, JedisPool> maps = new HashMap<String, JedisPool>();

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

    /**
     * 将消息push到队列中
     *
     * @param message 消息
     */
    public static void pushMessage(Message message) {
        String jsonStr = JSON.toJSONString(message);
        Jedis jedis = null;
        try {
            jedis = getConnect();
            jedis.lpush(message.getClass().getName().concat(".list"), jsonStr);
            jedis.sadd(message.getClass().getName().concat(".set"), jsonStr);
        } catch (Exception e) {
            logger.error("消息push失败原因： " + e.getMessage() + "  消息：" + jsonStr);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    // list key
    public static String getListKey(Class<? extends Message> messageClass) {
        return messageClass.getName().concat(".list");
    }
    // set key
    public static String getSetKey(Class<? extends Message> messageClass) {
        return messageClass.getName().concat(".set");
    }
}
