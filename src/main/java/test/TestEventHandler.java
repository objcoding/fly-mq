package test;

import cn.zhangchenghui.redismq.core.HandlerTask;

/**
 * Created by chenghui.zhang on 2018/1/7.
 */
public class TestEventHandler extends HandlerTask<TestMessage> {
    public TestEventHandler() {
        super(TestMessage.class);
    }

    @Override
    public boolean handle(TestMessage message) {

        return false;
    }
}
