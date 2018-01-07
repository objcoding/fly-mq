
## 使用步骤

### 1.新建消息载体

```java

// 只需实现Message接口
public class TestMessage implements Message {
  private String event;

  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }
}
```



### 2.新建任务

```java

// 继承HandlerTask
public class TestEventHandler extends HandlerTask<TestMessage> {
  public TestEventHandler() {
    super(TestMessage.class);
  }
  
  // 实现handle方法
  @Override
  public boolean handle(TestMessage message) {
    System.out.println("execute hanlde >>> ", this.getClass().getName());
    return true;
  }
}
```



### 3.初始化与启动

```java
public static void run() {

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
```


### 4.把消息推到队列中

```java
public void pushMessage(String messageJson, Long userId) {
  
  TestMessage testMessage = new TestMessage();
  testMessage.setEvent("这是一个消息事件");
  
  RedisUtil.pushMessage(testMessage);
    
}
```

