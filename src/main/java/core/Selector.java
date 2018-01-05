package core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenghui.zhang on 2018/1/5.
 */
public class Selector {

    private Logger logger = LoggerFactory.getLogger(Selector.class);

    private Map<Class<?>, Object> classes = new HashMap<Class<?>, Object>();

    private Map<Class<?>, Long> lastTimestamp = new HashMap<Class<?>, Long>();

    public <T extends AbstractTask> Selector addTask(Class<T> taskClass, Task task) throws NoSuchMethodException {
        classes.put(taskClass, task);
        lastTimestamp.put(taskClass, 0L);
        return this;
    }

    public void start() throws Exception {
        while (true) {
            for (Map.Entry<Class<?>, Object> taskClass : classes.entrySet()) {
                try {
                    try {

                        Method method = taskClass.getKey().getMethod(Task.DO_METHOD);

                        long delay = 3000;
                        if (System.currentTimeMillis() - lastTimestamp.get(taskClass.getKey()) >= delay) {
                            Long time = (Long) method.invoke(taskClass.getValue());
                            lastTimestamp.put(taskClass.getKey(), time);
                        }
                    } catch (Exception e) {
                        logger.error("error", e);
                        lastTimestamp.put(taskClass.getKey(), System.currentTimeMillis());
                    }
                } catch (Exception e) {
                    logger.error("error2", e);
                    lastTimestamp.put(taskClass.getKey(), System.currentTimeMillis());
                }
            }
        }
    }
}
