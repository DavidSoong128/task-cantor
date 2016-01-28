package com.horizon.cantor.node.recovery;

import com.horizon.cantor.exception.CantorException;
import com.horizon.cantor.redis.RedisOperator;
import com.horizon.cantor.transfer.core.TaskMessage;
import com.horizon.cantor.transfer.core.TaskMsgStoreQueue;
import com.horizon.cantor.util.FstSerializer;
import com.horizon.cantor.util.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Set;

/**
 * <pre>
 *     当启动节点时，从redis中重新加载计划任务
 * </pre>
 * @author : David.Song/Java Engineer
 * @date : 2016/1/28 15:37
 * @see
 * @since : 1.0.0
 */
public class ReloadTaskFromStore {

    private Logger logger = LoggerFactory.getLogger(ReloadTaskFromStore.class);

    private RedisOperator redisOperator = new RedisOperator();

    private FstSerializer fstSerializer = new FstSerializer();

    private static class Reload{
        private static ReloadTaskFromStore reloadTaskFromStore = new ReloadTaskFromStore();
    }

    public static ReloadTaskFromStore reloadHolder(){
        return Reload.reloadTaskFromStore;
    }

    public void recovery(){
        try{
            //获取monitorTopic开头的key下面所有的值数据
            Set<byte[]> keys = redisOperator.getKeys();
            if (keys == null || keys.size() == 0) {
                logger.info("no task in store need to reload");
                return;
            }
            Iterator<byte[]> iterator = keys.iterator() ;
            TaskMessage taskMsg = null;
            while(iterator .hasNext()){
                try{
                    byte[] msgData = redisOperator.getValue(iterator.next());
                    taskMsg = fstSerializer.deserialize(msgData,TaskMessage.class);
                    TaskMsgStoreQueue.queueHolder().put(taskMsg);
                    logger.info("reload task success {}",JSONUtils.toJSON(taskMsg));
                }catch(Exception ex){
                    logger.error("reload task:{} error", JSONUtils.toJSON(taskMsg));
                }
            }
            logger.info("recovery task from store success！");
        }catch(Exception ex){
            logger.error("reload task from store error",ex);
            throw new CantorException("",ex);
        }
    }

}
