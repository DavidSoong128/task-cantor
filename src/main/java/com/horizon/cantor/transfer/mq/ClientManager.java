package com.horizon.cantor.transfer.mq;

/**
 * mq客户端操作接口抽象
 * @author : David.Song/Java Engineer
 * @date : 2016/1/27 14:19
 * @see
 * @since : 1.0.0
 */
public interface ClientManager {

    public void start(String topic);

    public void callback(String topic,byte[] taskMsg);

    public void shutdown();
}
