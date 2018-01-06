package cn.zhangchenghui.utils;

import com.alibaba.fastjson.JSON;
import cn.zhangchenghui.message.EventMessage;
import cn.zhangchenghui.template.ListRedisTemplate;
import cn.zhangchenghui.template.SetRedisTemplate;


/**
 * Created by junjie.liu on 2017/12/13.
 */

public class RedisUtil {

    public static void pushMessage(EventMessage message) {
        ListRedisTemplate listRedisTemplate = SpringUtil.getBean(ListRedisTemplate.class);
        SetRedisTemplate setRedisTemplate = SpringUtil.getBean(SetRedisTemplate.class);
        String jsonStr = JSON.toJSONString(message);
        listRedisTemplate.lpush(message.getClass().getName().concat(".list"), jsonStr);
        setRedisTemplate.sadd(message.getClass().getName().concat(".set"), jsonStr);
    }

    // list key
    public static String getListKey(Class<? extends EventMessage> messageClass) {
        return messageClass.getName().concat(".list");
    }
    // set key
    public static String getSetKey(Class<? extends EventMessage> messageClass) {
        return messageClass.getName().concat(".set");
    }
}
