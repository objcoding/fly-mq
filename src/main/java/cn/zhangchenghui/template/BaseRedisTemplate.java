package cn.zhangchenghui.template;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by chenghui.zhang on 2018/1/5.
 */
public class BaseRedisTemplate {

    protected RedisTemplate<String, String> redisTemplate;

    public BaseRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean exists(String key) {
        Boolean result = redisTemplate.hasKey(key);
        return result == null ? false : result;
    }

    public void del(String key) {
        redisTemplate.delete(key);
    }

    public void delAll(Set<String> list){
        redisTemplate.delete(list);
    }

    public void delAll(List<String> list){
        redisTemplate.delete(list);
    }

    public void expire(String key, long timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    public void expire(String key, long timeout, TimeUnit timeUnit) {
        redisTemplate.expire(key, timeout, timeUnit);
    }

    public long ttl(String key) {
        return redisTemplate.getExpire(key);
    }

}
