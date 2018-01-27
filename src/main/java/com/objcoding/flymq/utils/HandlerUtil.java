package com.objcoding.flymq.utils;

import com.objcoding.flymq.message.Message;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * Created by chenghui.zhang on 2018/1/11.
 */
public class HandlerUtil {

    private static final Logger logger = LoggerFactory.getLogger(HandlerUtil.class);

    /**
     * 将消息push到队列中
     *
     * @param message 消息
     */
    public static void pushMessage(Message message) {
        String jsonStr = JSON.toJSONString(message);
        Jedis jedis = null;
        try {
            jedis = RedisUtil.getConnect();
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

    /**
     * 重新加载事件队列
     *
     * @param messageClasses 消息类
     */
    public static void reloadMessage(Class<? extends Message>[] messageClasses) {

        Jedis jedis = null;

        try {

            jedis = RedisUtil.getConnect();

            for (Class<? extends Message> messageClass : messageClasses) {
                jedis.del(getListKey(messageClass));

                // 删除list key，加载set到list
                try {
                    if (jedis.exists(getSetKey(messageClass))) {
                        Set<String> eventSet = jedis.smembers(getSetKey(messageClass));
                        if (null != eventSet && eventSet.size() > 0) {
                            for (String message : eventSet) {
                                jedis.lpush(getListKey(messageClass), message);
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("重新加载事件队列失败 >>>> " + e.getMessage());
                }
            }

        } catch (Exception e) {
            logger.error("redis连接失败 >>> " + e.getMessage());
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
