package com.horizon.cantor.transfer.core;

import java.io.Serializable;

/**
 * 计划任务消息载体
 * @author : David.Song/Java Engineer
 * @date : 2016/1/27 14:06
 * @see
 * @since : 1.0.0
 */
public class TaskMessage implements Serializable{

    private static final long	serialVersionUID	= -251395609120956687L;
    //计划任务唯一标示
    private final String		taskId;
    //计划任务操作，是新增，删除，还是更新
    private final String		operation;
    //计划任务执行时间，也就是回调时间
    private final long			executeTime;
    //传递的计划任务上下文信息
    private final byte[]		data;
    //应用与计划任务中心之间沟通的唯一标示
    private final String		communicateId;

    public TaskMessage(String taskId, String operation, long executeTime,byte[] data,
                       String communicateId) {
        this.taskId = taskId;
        this.operation = operation;
        this.executeTime = executeTime;
        this.data = data;
        this.communicateId = communicateId;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getOperation() {
        return operation;
    }

    public long getExecuteTime() {
        return executeTime;
    }

    public byte[] getData() {
        return data;
    }

    public String getCommunicateId() {
        return communicateId;
    }
}
