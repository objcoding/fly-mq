package com.objcoding.flymq.core;

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

    private Map<Class<? extends Task>, Task> classes = new HashMap<Class<? extends Task>, Task>();

    private Map<Class<? extends Task>, Long> lastTimestamp = new HashMap<Class<? extends Task>, Long>();

    private Selector() {

    }

    private final static Selector singleSelector = new Selector();

    public static Selector newSelector() {
        return singleSelector;
    }

    public void addTask(Class<? extends Task>[] taskClasses) throws Exception {

        if (null != taskClasses && taskClasses.length > 0) {

            for (Class<? extends Task> taskClass : taskClasses) {

                Task task;
                try {
                    task = taskClass.newInstance();
                } catch (InstantiationException ie) {
                    throw new InstantiationException(ie.getMessage());
                } catch (IllegalAccessException ile) {
                    throw new IllegalAccessException(ile.getMessage());
                }

                classes.put(taskClass, task);
                lastTimestamp.put(taskClass, 0L);

            }
        }

    }

    public void start(Long delay) throws Exception {

        Long d = (null == delay || delay <= 0) ? 3000L : delay;

        while (true) {
            for (Map.Entry<Class<? extends Task>, Task> taskClass : classes.entrySet()) {
                try {
                    try {

                        Method method = taskClass.getKey().getMethod(Task.DO_METHOD);

                        if (System.currentTimeMillis() - lastTimestamp.get(taskClass.getKey()) >= d) {
//                            System.out.println("fly-mq 正在执行 ==> " + taskClass.getKey().getName());
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
