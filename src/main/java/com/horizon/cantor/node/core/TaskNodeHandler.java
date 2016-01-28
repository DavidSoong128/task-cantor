package com.horizon.cantor.node.core;

import com.horizon.cantor.node.channel.ScheduleChannel;
import com.horizon.cantor.transfer.core.TaskMapKey;
import com.horizon.cantor.transfer.core.TaskMessage;
import com.horizon.cantor.transfer.core.TaskMsgStoreQueue;
import com.horizon.cantor.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 开始读取临时存储队列的任务消息，交给任务调度模块执行
 * @author : David.Song/Java Engineer
 * @date : 2016/1/27 15:37
 * @see
 * @since : 1.0.0
 */
public class TaskNodeHandler implements Runnable{

    private Logger logger = LoggerFactory.getLogger(TaskNodeHandler.class);
    //临时存储任务消息的内置队列
    private TaskMsgStoreQueue storeQueue = TaskMsgStoreQueue.queueHolder();

    private ScheduleChannel scheduleChannel;

    public TaskNodeHandler(ScheduleChannel scheduleChannel){
        this.scheduleChannel = scheduleChannel;
    }

    @Override
    public void run() {
        while(true){
            try{
                if(!NodeStatus.isReceive){
                    TimeUnit.SECONDS.sleep(1);
                    continue;
                }
                TaskMessage msg = (TaskMessage) storeQueue.get();
                if (TaskMapKey.OP_ADD_KEY.equals(msg.getOperation())) {
                    scheduleChannel.schedule(msg);
                }else if(TaskMapKey.OP_DELETE_KEY.equals(msg.getOperation())){
                    scheduleChannel.unschedule(msg);
                }
                logger.info("receive taskId:"+msg.getTaskId()+"operator:"+msg.getOperation()+",time:" +
                        ""+DateUtil.format(msg.getExecuteTime())+",appId:"+msg.getCommunicateId());
            }catch(Exception ex){
                logger.error("receive task error",ex);
            }
        }
    }
}
