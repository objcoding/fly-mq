package cn.zhangchenghui.template;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by chenghui.zhang on 2018/1/5.
 */
@Component
public class ListRedisTemplate extends BaseRedisTemplate {

    public ListRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
    }

    public long lpush(String key, String value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    public String lpop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    public long rpush(String key, String value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    public String rpop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    public long rpush(String key, List<String> values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    public long lpush(String key, List<String> values) {
        return redisTemplate.opsForList().leftPushAll(key, values);

    }
    public long llen(String key) {
        return redisTemplate.opsForList().size(key);
    }

    public List<String> range(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }
}
