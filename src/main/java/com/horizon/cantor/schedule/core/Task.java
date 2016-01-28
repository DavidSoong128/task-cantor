package com.horizon.cantor.schedule.core;

/**
 * <pre>任务信息，包括taskId,数据以及时间戳</pre>
 * @author : David.Song/Java Engineer
 * @date : 2016/1/27 16:50
 * @see
 * @since : 1.0.0
 */
public class Task {
    /**
     * Task id
     */
    private String taskId;

    /**
     * 数据
     */
    private byte[] data;

    /**
     * 时间戳
     */
    private long time;

    public Task(){

    }

    public Task(String TaskId, byte[] data) {
        super();
        this.taskId = TaskId;
        this.data = data;
    }

    public Task(String TaskId, byte[] data, long time) {
        super();
        this.taskId = TaskId;
        this.data = data;
        this.time = time;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String TaskId) {
        this.taskId = TaskId;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Task [taskId="+ taskId + ",data=" + data + ",time" + time + "]";
    }
}
