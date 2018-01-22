package cn.zhangchenghui.flymq.core;

/**
 * Created by chenghui.zhang on 2018/1/5.
 */
public interface Task {

    String DO_METHOD = "obtainTask";

    Long obtainTask();
}
