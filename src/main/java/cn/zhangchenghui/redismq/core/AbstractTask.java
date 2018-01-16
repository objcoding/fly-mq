package cn.zhangchenghui.redismq.core;

import cn.zhangchenghui.redismq.message.Message;
import cn.zhangchenghui.redismq.utils.AsyncUtil;
import cn.zhangchenghui.redismq.utils.HandlerUtil;
import cn.zhangchenghui.redismq.utils.RedisUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * Created by chenghui.zhang on 2018/1/5.
 */
public abstract class AbstractTask<T extends Message> implements Task {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    Class<? extends Message> messageClass;

    String message;

    AbstractTask(Class<? extends Message> messageClass) {
        this.messageClass = messageClass;
    }

    public Long obtainTask() {
        Jedis jedis = null;
        try {

            jedis = RedisUtil.getConnect();

            String message = jedis.rpop(HandlerUtil.getListKey(messageClass));
            this.message = message;
            logger.info("execute cn.zhangchenghui.redismq.message => " + message);
            if (StringUtils.isNotBlank(message)) {
                T msg = (T) JSON.parseObject(message, messageClass);

                AsyncUtil.submit(() -> {
                    if (handle(msg)) {
                        success();
                    } else {
                        fail();
                    }
                });

                return 0L;

            } else {
                return System.currentTimeMillis();
            }
        } catch (Exception e) {
            logger.error("消息处理失败 >>> {}  消息: {}", e.getMessage(), message);
            return System.currentTimeMillis();
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    public abstract boolean handle(T message);

    public abstract void success();

    public abstract void fail();
}
