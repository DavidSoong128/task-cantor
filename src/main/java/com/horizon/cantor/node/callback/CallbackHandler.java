package com.horizon.cantor.node.callback;

import com.horizon.cantor.schedule.core.Task;

/**
 * @author : David.Song/Java Engineer
 * @date : 2016/1/27 16:42
 * @see
 * @since : 1.0.0
 */
public interface CallbackHandler {
    /**
     * 处理计划任务到达执行时间回调
     */
    public void handleCallback(Task task);
}
