package com.horizon.cantor.node.store;

import com.horizon.cantor.config.CantorConfig;
import com.horizon.cantor.redis.RedisOperator;
import com.horizon.cantor.transfer.core.TaskMessage;
import com.horizon.cantor.util.DateUtil;
import com.horizon.cantor.util.FstSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <pre>
 *    计划任务持久化
 * </pre>
 * @author : David.Song/Java Engineer
 * @date : 2016/1/28 12:04
 * @see
 * @since : 1.0.0
 */
public class TaskMessageStore {

    private Logger logger = LoggerFactory.getLogger(TaskMessageStore.class);

    private FstSerializer fstSerializer = new FstSerializer();

    private ExecutorService storePool = Executors.newFixedThreadPool(10);

    private RedisOperator redisOperator = new RedisOperator();

    private CantorConfig cantorConfig = CantorConfig.configHolder();

    private static class Holder{
        private static TaskMessageStore holder = new TaskMessageStore();
    }

    public static TaskMessageStore storeHolder(){
        return Holder.holder;
    }

    public void store(final TaskMessage taskMsg){
        storePool.submit(new Runnable(){
            public void run(){
                try {
                    byte[] msg = fstSerializer.serialize(taskMsg);
                    //存儲的key=appId + taskId唯一标示
                    String storeKey = cantorConfig.getMonitorTopic() + taskMsg.getTaskId();
                    redisOperator.setValue(storeKey.getBytes(),msg);
                    logger.info("store redis taskId = "+taskMsg.getTaskId()+"  operator="+taskMsg.getOperation()+
                                " start="+ DateUtil.format(taskMsg.getExecuteTime()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
