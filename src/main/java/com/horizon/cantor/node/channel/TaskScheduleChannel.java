package com.horizon.cantor.node.channel;

import com.horizon.cantor.config.CantorConfig;
import com.horizon.cantor.node.callback.TaskCallbackHandler;
import com.horizon.cantor.node.store.TaskMessageDestroy;
import com.horizon.cantor.node.store.TaskMessageStore;
import com.horizon.cantor.schedule.core.Task;
import com.horizon.cantor.schedule.callback.TaskCallbackScheduler;
import com.horizon.cantor.schedule.core.TaskScheduler;
import com.horizon.cantor.transfer.core.TaskMessage;
import com.horizon.cantor.util.FstSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Task和调度模块之间的链接通道, 与调度模块schedule交互只能通过这里操作
 * @author : David.Song/Java Engineer
 * @date : 2016/1/27 16:13
 * @see
 * @since : 1.0.0
 */
public class TaskScheduleChannel implements ScheduleChannel {

    private Logger logger = LoggerFactory.getLogger(TaskScheduleChannel.class);

    private TaskScheduler taskScheduler = TaskScheduler.schedulerHolder();

    private FstSerializer fstSerializer = new FstSerializer();

    private CantorConfig cantorConfig = CantorConfig.configHolder();

    public TaskScheduleChannel(){
        //在第一次计划任务调度开始，就需要将回调处理线程启动
        startCallback();
    }

    private void startCallback() {
        new Thread(new TaskCallbackScheduler(new TaskCallbackHandler())).start();
    }

    @Override
    public void schedule(TaskMessage taskMsg) {
        try{
            byte[] data = fstSerializer.serialize(taskMsg);
            Task task = new Task(taskMsg.getTaskId(),data,taskMsg.getExecuteTime());
            taskScheduler.scheduleTask(task);
            //同时存储到redis中，作为failover数据源
            TaskMessageStore.storeHolder().store(taskMsg);
            logger.info("schedule taskId:{}",taskMsg.getTaskId());
        }catch(IOException ex){
            logger.error("schedule io error",ex);
        }catch(Exception ex){
            logger.error("schedule error",ex);
        }
    }

    @Override
    public void unschedule(TaskMessage taskMsg) {
        try{
            taskScheduler.removeTask(taskMsg.getTaskId());
            //同时将redis中存储的任务消息移除
            TaskMessageDestroy.destroyHolder().destroy(cantorConfig.getMonitorTopic() + taskMsg.getTaskId());
            logger.info("unschedule taskId:{}",taskMsg.getTaskId());
        }catch(Exception ex){
            logger.error("unschedule error",ex);
        }
    }

    @Override
    public void unscheduleAll() {
        taskScheduler.clear();
    }
}
