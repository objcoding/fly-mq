package core;

import message.EventMessage;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import template.SetRedisTemplate;
import utils.RedisUtil;

import javax.annotation.Resource;

/**
 * Created by chenghui.zhang on 2018/1/5.
 */
@Component
public abstract class HandlerTask<T extends EventMessage> extends AbstractTask<T> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    protected SetRedisTemplate setRedisTemplate;

    public HandlerTask(Class<? extends EventMessage> messageClass) {
        super(messageClass);
        this.messageClass = messageClass;
    }

    public void success() {
        if (StringUtils.isNotBlank(message)) {
            setRedisTemplate.srem(RedisUtil.getSetKey(messageClass), message);
        }

    }

    public void fail() {
        if (StringUtils.isNotBlank(message)) {
            listRedisTemplate.lpush(RedisUtil.getListKey(messageClass), message);
            logger.error("队列消费失败 key:{}, message:{}", RedisUtil.getListKey(messageClass), message);
        }
    }
}
