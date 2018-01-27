package com.objcoding.flymq.config;

/**
 * redis配置类
 *
 * Created by chenghui.zhang on 2018/1/7.
 */
public class RedisConfig {

    private String host;

    private int port;

    private String password;

    private int maxActive; // 连接池最大连接数（使用负值表示没有限制）

    private int maxWait; // 连接池最大阻塞等待时间（使用负值表示没有限制）

    private int maxIdle; // 连接池中的最大空闲连接

    private int minIdle; // 连接池中的最小空闲连接

    private int timeout; // 连接超时时间（毫秒）

    public String getHost() {
        return host;
    }

    public RedisConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public RedisConfig setPort(int port) {
        this.port = port;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RedisConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public RedisConfig setMaxActive(int maxActive) {
        this.maxActive = maxActive;
        return this;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public RedisConfig setMaxWait(int maxWait) {
        this.maxWait = maxWait;
        return this;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public RedisConfig setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
        return this;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public RedisConfig setMinIdle(int minIdle) {
        this.minIdle = minIdle;
        return this;
    }

    public int getTimeout() {
        return timeout;
    }

    public RedisConfig setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }
}
