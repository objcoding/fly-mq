package template;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by chenghui.zhang on 2018/1/5.
 */
@Component
public class SetRedisTemplate extends BaseRedisTemplate {

    public SetRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
    }

    public void sadd(String key, String member) {
        redisTemplate.opsForSet().add(key, member);
    }

    public void sadd(String key, String member, Long timeOut) {
        redisTemplate.opsForSet().add(key, member);
        redisTemplate.expire(key, timeOut, TimeUnit.SECONDS);
    }

    public Set<String> member(String key) {
        if(null == key)
            return null;
        return redisTemplate.opsForSet().members(key);
    }

    public long scard(String key) {
        if(null == key)
            return 0;
        else
            return redisTemplate.opsForSet().size(key);
    }

    public void srem(String key, String member) {
        redisTemplate.opsForSet().remove(key, member);
    }

    public boolean isMember(String key, String member) {
        return redisTemplate.opsForSet().isMember(key, member);
    }

    public String srandmember(String key) {
        return redisTemplate.opsForSet().randomMember(key);
    }

    public String spop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    public Set<String> sunion(String key, List<String> otherKeys) {
        return redisTemplate.opsForSet().union(key, otherKeys);
    }
}
