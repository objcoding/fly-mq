package test;

import cn.zhangchenghui.flymq.message.Message;

/**
 * Created by chenghui.zhang on 2018/1/7.
 */
public class TestMessage implements Message {
    private String event;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
