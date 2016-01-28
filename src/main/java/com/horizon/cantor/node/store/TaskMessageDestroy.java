package com.horizon.cantor.node.store;

import com.horizon.cantor.redis.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <pre>计划任务移除持久化</pre>
 * @author : David.Song/Java Engineer
 * @date : 2016/1/28 14:17
 * @see
 * @since : 1.0.0
 */
public class TaskMessageDestroy {

    private Logger logger = LoggerFactory.getLogger(TaskMessageDestroy.class);

    private ExecutorService destroyPool = Executors.newFixedThreadPool(10);

    private RedisOperator redisOperator = new RedisOperator();

    private static class Holder{
        private static TaskMessageDestroy holder = new TaskMessageDestroy();
    }

    public static TaskMessageDestroy destroyHolder(){
        return Holder.holder;
    }

    public void destroy(final String destroyKey){
        destroyPool.submit(new Runnable(){
            public void run(){
                redisOperator.delValue(destroyKey.getBytes());
                logger.info("destroyKey :{}",destroyKey);
            }
        });
    }
}
