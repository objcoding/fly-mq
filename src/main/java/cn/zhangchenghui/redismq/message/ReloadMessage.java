package cn.zhangchenghui.redismq.message;

import cn.zhangchenghui.redismq.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * Created by chenghui.zhang on 2018/1/5.
 */
public class ReloadMessage {

    private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    public static void reload(Class<? extends Message>[] messageClasses) {

        Jedis jedis = null;

        try {

            jedis = RedisUtil.getConnect();

            for (Class<? extends Message> messageClass : messageClasses) {
                jedis.del(RedisUtil.getListKey(messageClass));

                // 删除list key，加载set到list
                try {
                    if (jedis.exists(RedisUtil.getSetKey(messageClass))) {
                        Set<String> eventSet = jedis.smembers(RedisUtil.getSetKey(messageClass));
                        if (null != eventSet && eventSet.size() > 0) {
                            for (String message : eventSet) {
                                jedis.lpush(RedisUtil.getListKey(messageClass), message);
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
}
