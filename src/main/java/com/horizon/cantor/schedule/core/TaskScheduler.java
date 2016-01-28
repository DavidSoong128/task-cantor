package com.horizon.cantor.schedule.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * <pre>任务调度核心处理类</pre>
 * @author : David.Song/Java Engineer
 * @date : 2016/1/27 16:55
 * @see
 * @since : 1.0.0
 */
public class TaskScheduler {

    private Logger logger = LoggerFactory.getLogger(TaskScheduler.class);

    private MemoryTaskStore taskStore;

    private TaskScheduler() {
        taskStore = MemoryTaskStore.taskStore();
    }

    private static class Holder {
        private static TaskScheduler holder = new TaskScheduler();
    }

    public static TaskScheduler schedulerHolder(){
        return Holder.holder;
    }
    /**
     * <pre>
     * 添加 / 更新一个task
     * </pre>
     * @param task
     * @return 返回job执行时间，如果不更新/添加，返回null
     */
    public Date scheduleTask(Task task) {
        Task orgTask = taskStore.getTask(task.getTaskId());
        //1.存在且原来的task时间小于当前task的时间，更新；
        //2.taskStore中不存在该task，添加
        if ((orgTask != null && orgTask.getTime() < task.getTime()) || orgTask == null) {
            //覆盖原有的存储的Task
            taskStore.putTask(task);
            //添加当前时间戳触发任务键值对
            TaskTrigger taskTrigger = new TaskTrigger(task.getTaskId(), task.getTime());
            taskStore.putTaskTrigger(taskTrigger);
            return new Date(taskTrigger.getTime());
        }
        return null;
    }
    /**
     * <pre>
     * 移除一个job
     * </pre>
     * @param taskId
     */
    public void removeTask(String taskId) {
        if (taskId != null) {
            taskStore.removeTask(taskId);
        }
    }

    public Task getTaskDetail(String taskId) {
        return taskStore.getTask(taskId);
    }

    public void clear() {
        taskStore.clear();
    }

}
