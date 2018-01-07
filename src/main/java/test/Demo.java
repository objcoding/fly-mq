package test;

import cn.zhangchenghui.redismq.config.RedisConfig;
import cn.zhangchenghui.redismq.core.Selector;
import cn.zhangchenghui.redismq.utils.RedisUtil;

/**
 * Created by chenghui.zhang on 2018/1/7.
 */
public class Demo {

    public static void main(String[] args) {

        // 1.初始化redis连接池
        RedisConfig redisConfig = new RedisConfig()
                .setHost("217.0.0.1")
                .setPort(6379)
                .setMaxActive(500)
                .setMaxWait(-1)
                .setMaxIdle(200)
                .setMinIdle(50)
                .setTimeout(5000);

        RedisUtil.initPool(redisConfig);

        // 2. 重载消息队列
        Class[] eventMessages = {
                TestMessage.class
        };
        RedisUtil.reloadMessage(eventMessages);

        Selector selector = Selector.newSelector();
        try {

            // 3.加载任务列表
            Class[] task = {
                    TestEventHandler.class
            };
            selector.addTask(task);

            // 4.启动redis-mq
            selector.start(3000L);

        } catch (Exception e) {
            e.fillInStackTrace();
        }


    }
}
