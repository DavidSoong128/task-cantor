package com.horizon.cantor.schedule.core;

/**
 * <pre>某个时间点，对应的所有的计划任务集合元素</pre>
 * @author : David.Song/Java Engineer
 * @date : 2016/1/27 17:19
 * @see
 * @since : 1.0.0
 */
public class TaskTrigger {
    /**
     * taskId
     */
    private String taskId;
    /**
     * 时间
     */
    private long time;

    public TaskTrigger(String taskId, long time) {
        super();
        this.taskId = taskId;
        this.time = time;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Task [TaskId="+ taskId + ",time" + time + "]";
    }

}
