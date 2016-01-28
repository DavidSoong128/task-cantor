package com.horizon.cantor.schedule.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author : David.Song/Java Engineer
 * @date : 2016/1/28 10:19
 * @see
 * @since : 1.0.0
 */
public class CallbackRejectPolicy implements RejectedExecutionHandler{
    private Logger logger = LoggerFactory.getLogger(CallbackRejectPolicy.class);
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        try {
            if (!executor.isShutdown())
                executor.getQueue().put(r);
        } catch (InterruptedException ex) {
            logger.error("", ex);
        }
    }
}
