package com.horizon.cantor.transfer.handler;

import com.alibaba.fastjson.JSON;
import com.horizon.cantor.transfer.core.TaskMapKey;
import com.horizon.cantor.transfer.core.TaskMessage;
import com.horizon.cantor.transfer.core.TaskMsgStoreQueue;
import com.horizon.cantor.util.FastJsonSerializable;
import com.horizon.mqclient.api.ConsumerResult;
import com.horizon.mqclient.api.Message;
import com.horizon.mqclient.api.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author : David.Song/Java Engineer
 * @date : 2016/1/27 14:15
 * @see
 * @since : 1.0.0
 */
public class TransferMsgHandler implements MessageHandler{

    private Logger logger = LoggerFactory.getLogger(TransferMsgHandler.class);

    @Override
    public void handleMessage(ConsumerResult result) {
        try{
            Message message = result.getValue();
            byte[] taskMsgByte = message.getMessageByte();
            String taskStr = new String(taskMsgByte, Charset.forName("UTF-8"));
            Map taskMap = JSON.parseObject(taskStr, Map.class);
            String taskId = String.valueOf(taskMap.get(TaskMapKey.TASK_ID_KEY));
            String operator = String.valueOf(taskMap.get(TaskMapKey.OPERATOR_KEY));
            TaskMessage taskMsg;
            if(TaskMapKey.OP_ADD_KEY.equals(operator)){
                long time = Long.valueOf(String.valueOf(taskMap.get(TaskMapKey.EXECUTE_TIME_KEY)));
                byte[] data = FastJsonSerializable.encode(taskMap.get(TaskMapKey.DATA_KEY));
                String backTopic = String.valueOf(taskMap.get(TaskMapKey.BACK_TOPIC_KEY));
                taskMsg = new TaskMessage(taskId, operator, time, data, backTopic);
            }else{
                taskMsg = new TaskMessage(taskId, operator, System.currentTimeMillis(), "".getBytes(), "");
            }
            TaskMsgStoreQueue.queueHolder().put(taskMsg);
        }catch(Exception ex){
            logger.error("handle task msg error ",ex);
        }
    }
}
