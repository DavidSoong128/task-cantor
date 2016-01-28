package com.horizon.cantor.schedule.callback;

import com.horizon.cantor.node.callback.TaskCallbackHandler;
import com.horizon.cantor.schedule.core.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 *    异步线程，每个回调交给一个线程处理
 * </pre>
 * @author : David.Song/Java Engineer
 * @date : 2016/1/28 10:40
 * @see
 * @since : 1.0.0
 */
public class TaskCallbackRunnable implements Runnable{

    private Logger logger = LoggerFactory.getLogger(TaskCallbackScheduler.class);
    /**
     * 任务回调处理核心类
     */
    private TaskCallbackHandler handler;

    private Task task;

    public TaskCallbackRunnable(TaskCallbackHandler handler,Task task){
        this.handler = handler;
        this.task = task;
    }

    @Override
    public void run() {
        try{
            handler.handleCallback(task);
        }catch(Exception ex){
            logger.error("handleCallback error",ex);
        }
    }
}
