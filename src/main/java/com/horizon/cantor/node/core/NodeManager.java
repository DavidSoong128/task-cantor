package com.horizon.cantor.node.core;

/**
 * @author : David.Song/Java Engineer
 * @date : 2016/1/27 15:19
 * @see
 * @since : 1.0.0
 */
public interface NodeManager{
    /**
     * 启动计划任务处理节点，开始处理数据
     */
    public void startNode();

    /**
     * 停止客户端接收任务，并且停止node处理，但是同样会将剩余数据处理完成
     */
    public void stopNode();
}
