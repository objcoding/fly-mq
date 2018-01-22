package cn.zhangchenghui.flymq.core;

import cn.zhangchenghui.flymq.message.Message;
import cn.zhangchenghui.flymq.utils.HandlerUtil;
import cn.zhangchenghui.flymq.utils.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * Created by chenghui.zhang on 2018/1/5.
 */
public abstract class HandlerTask<T extends Message> extends AbstractTask<T> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public HandlerTask(Class<? extends Message> messageClass) {
        super(messageClass);
        this.messageClass = messageClass;
    }

    @Override
    public void success() {
        if (StringUtils.isNotBlank(message)) {
            Jedis jedis = null;
            try {
                jedis = RedisUtil.getConnect();
                jedis.srem(HandlerUtil.getSetKey(messageClass), message);
            } catch (Exception e) {
                logger.error("卸载消息势失败 >>> {}  消息: {}", e.getMessage(), message);
            } finally {
                if (null != jedis) {
                    jedis.close();
                }
            }
        }

    }

    @Override
    public void fail() {
        if (StringUtils.isNotBlank(message)) {
            Jedis jedis = null;
            try {
                jedis = RedisUtil.getConnect();
                jedis.lpush(HandlerUtil.getListKey(messageClass), message);
            } catch (Exception e) {
                logger.error("队列消费失败 >>> {}  消息: {}", e.getMessage(), message);
            } finally {
                if (null != jedis) {
                    jedis.close();
                }
            }
        }
    }
}
