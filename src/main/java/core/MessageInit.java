package core;

import message.EventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import template.ListRedisTemplate;
import template.SetRedisTemplate;
import utils.RedisUtil;
import utils.SpringUtil;

import java.util.Set;

/**
 * Created by chenghui.zhang on 2018/1/5.
 */
public class MessageInit {

    private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    public static void init(Class<EventMessage>[] messageClasses) {
        for (Class<EventMessage> messageClass : messageClasses) {
            ListRedisTemplate listRedisTemplate = SpringUtil.getBean(ListRedisTemplate.class);
            listRedisTemplate.del(RedisUtil.getListKey(messageClass));

            // 删除list key，加载set到list
            try {
                SetRedisTemplate setRedisTemplate = SpringUtil.getBean(SetRedisTemplate.class);
                if (setRedisTemplate.exists(RedisUtil.getSetKey(messageClass))) {
                    Set<String> eventSet = setRedisTemplate.member(RedisUtil.getSetKey(messageClass));
                    if (null != eventSet && eventSet.size() > 0) {
                        for (String message : eventSet) {
                            listRedisTemplate.lpush(RedisUtil.getListKey(messageClass), message);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("重新加载事件队列失败");
            }
        }

    }
}
