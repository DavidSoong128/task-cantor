package com.horizon.cantor.node.channel;

import com.horizon.cantor.transfer.core.TaskMessage;

/**
 * @author : David.Song/Java Engineer
 * @date : 2016/1/27 16:13
 * @see
 * @since : 1.0.0
 */
public interface ScheduleChannel {
    /**
     * 往调度模块新增一个计划任务
     * @param taskMsg
     */
    public void schedule(TaskMessage taskMsg);

    /**
     * 往调度模块取消一个计划任务
     * @param taskMsg
     */
    public void unschedule(TaskMessage taskMsg);

    /**
     * 取消所有的计划任务
     */
    public void unscheduleAll();
}
