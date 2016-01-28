package com.horizon.cantor.node.core;

import com.horizon.cantor.config.CantorConfig;
import com.horizon.cantor.node.channel.TaskScheduleChannel;
import com.horizon.cantor.node.recovery.ReloadTaskFromStore;
import com.horizon.cantor.node.store.TaskMessageStore;
import com.horizon.cantor.transfer.core.TaskMessage;
import com.horizon.cantor.transfer.core.TaskMsgStoreQueue;
import com.horizon.cantor.transfer.mq.TransferClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 计划任务节点管理器
 * @author : David.Song/Java Engineer
 * @date : 2016/1/27 15:18
 * @see
 * @since : 1.0.0
 */
public class TaskNodeManager implements NodeManager{

    private Logger logger = LoggerFactory.getLogger(TaskNodeManager.class);

    private CantorConfig cantorConfig;

    private TaskScheduleChannel taskScheduleChannel;

    public TaskNodeManager(){
        cantorConfig = CantorConfig.configHolder();
        taskScheduleChannel = new TaskScheduleChannel();
        //启动开启处理storeQueue队列数据的线程
        new Thread(new TaskNodeHandler(taskScheduleChannel)).start();
    }

    @Override
    public void startNode() {
        //开始处理任务
        NodeStatus.isReceive = true;
        //重新加载redis中的计划任务
        ReloadTaskFromStore.reloadHolder().recovery();
        //启动Transfer模块，监听需要拉取消息的topic
        TransferClientManager.clientManagerHolder().start(cantorConfig.getMonitorTopic());
        logger.info("start node success !");
    }

    @Override
    public void stopNode() {
        //取消订阅,以及发送回调消息
        TransferClientManager.clientManagerHolder().shutdown();
        //停止添加新的任务
        NodeStatus.isReceive = false;
        //此处无需关心MemoryTaskStore内存任务信息，因为没有回调则没有删除redis存储的数据
        taskScheduleChannel.unscheduleAll();
        //把未添加的任务存储到redis
        TaskMsgStoreQueue receive = TaskMsgStoreQueue.queueHolder();
        while(!receive.isEmpty()){
            try {
                TaskMessage taskMsg = (TaskMessage) receive.get();
                TaskMessageStore.storeHolder().store(taskMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.info("stop node finished !");
    }
}
