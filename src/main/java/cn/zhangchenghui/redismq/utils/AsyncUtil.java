package cn.zhangchenghui.redismq.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by chenghui.zhang on 2018/1/16.
 */
public class AsyncUtil {

    private static ExecutorService executorService = null;

    private static ExecutorService getPool() {
        if (null == executorService) {
            executorService = Executors.newFixedThreadPool(500);
        }
        return executorService;
    }

    public static void submit(Runnable runnable) {
        getPool().submit(runnable);
    }
}
