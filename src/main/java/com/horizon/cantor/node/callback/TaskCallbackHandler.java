package com.horizon.cantor.node.callback;

import com.horizon.cantor.config.CantorConfig;
import com.horizon.cantor.node.store.TaskMessageDestroy;
import com.horizon.cantor.schedule.core.Task;
import com.horizon.cantor.transfer.core.TaskMessage;
import com.horizon.cantor.transfer.mq.TransferClientManager;
import com.horizon.cantor.util.DateUtil;
import com.horizon.cantor.util.FstSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 达到指定任务执行回调处理
 * @author : David.Song/Java Engineer
 * @date : 2016/1/27 16:39
 * @see
 * @since : 1.0.0
 */
public class TaskCallbackHandler implements CallbackHandler{

    private Logger logger = LoggerFactory.getLogger(TaskCallbackHandler.class);

    private FstSerializer fstSerializer = new FstSerializer();

    private TransferClientManager clientManager = TransferClientManager.clientManagerHolder();

    private CantorConfig cantorConfig = CantorConfig.configHolder();

    @Override
    public void handleCallback(Task task) {
        try {
            if (task == null || task.getData() == null) {
                return;
            }
            TaskMessage taskMsg = fstSerializer.deserialize(task.getData(), TaskMessage.class);
            //回调返回队列的消息，第一个参数代表topic名称，第二个是实际传递的上下文数据
            clientManager.callback(taskMsg.getCommunicateId(),taskMsg.getData());

            //执行回调成功之后的任务消息同时需要将redis数据清除
            TaskMessageDestroy.destroyHolder().destroy(cantorConfig.getMonitorTopic() + task.getTaskId());

            logger.info("taskId=" + taskMsg.getTaskId() + " 计划时间="+ DateUtil.format(taskMsg.getExecuteTime())+" " +
                    "执行时间=" + DateUtil.format(System.currentTimeMillis())+" backTopic="+taskMsg.getCommunicateId());
        } catch (Exception ex) {
            logger.error("handleCallback ex",ex);
        }
    }
}
