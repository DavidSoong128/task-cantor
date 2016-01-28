package com.horizon.cantor.node.core;

/**
 * @author : David.Song/Java Engineer
 * @date : 2016/1/27 15:45
 * @see
 * @since : 1.0.0
 */
public class NodeStatus {
    /**
     * 作为节点从store queue拉取消息时，线程是否继续执行的标示
     */
    public volatile static boolean isReceive = true;
}
