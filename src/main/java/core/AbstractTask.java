package core;

import com.alibaba.fastjson.JSON;
import message.EventMessage;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import template.ListRedisTemplate;
import utils.RedisUtil;

import javax.annotation.Resource;

/**
 * Created by chenghui.zhang on 2018/1/5.
 */
@Component
public abstract class AbstractTask<T extends EventMessage> implements Task {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    protected Class<? extends EventMessage> messageClass;

    protected String message;

    @Resource
    protected ListRedisTemplate listRedisTemplate;

    public AbstractTask(Class<? extends EventMessage> messageClass) {
        this.messageClass = messageClass;
    }

    public Long obtainTask() {
        String message = listRedisTemplate.rpop(RedisUtil.getListKey(messageClass));
        this.message = message;
        logger.info("execute message => " + message);
        if (StringUtils.isNotBlank(message)) {
            T msg = (T) JSON.parseObject(message, messageClass);
            if (handle(msg)) {
                success();
            } else {
                fail();
            }
            return 0L;

        } else {
            return System.currentTimeMillis();
        }
    }

    public abstract boolean handle(T message);

    public abstract void success();

    public abstract void fail();
}
