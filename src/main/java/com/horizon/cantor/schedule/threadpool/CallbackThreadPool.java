package com.horizon.cantor.schedule.threadpool;

import com.horizon.cantor.exception.TaskException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *    任务回调线程池
 * </pre>
 * @author : David.Song/Java Engineer
 * @date : 2016/1/28 10:16
 * @see
 * @since : 1.0.0
 */
public class CallbackThreadPool {

    private static final Logger logger = LoggerFactory.getLogger(CallbackThreadPool.class);
    /**
     * 线程池默认线程数
     */
    private static final int THREAD_POOL_DEFAULT_SIZE = 20;

    private ExecutorService executor;

    private CallbackThreadPool() {
        // executor = Executors.newFixedThreadPool(THREAD_POOL_DEFAULT_SIZE);
        //获取机器的cpu线程数
        int cpuNum = Runtime.getRuntime().availableProcessors();
        //如果机器cpu数小于默认线程数,将默认线程数设置成cpu数量
        if (cpuNum < THREAD_POOL_DEFAULT_SIZE) {
            cpuNum = THREAD_POOL_DEFAULT_SIZE;
        }
        //初始化线程池
        executor = new ThreadPoolExecutor(cpuNum,
                cpuNum, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), new CallbackRejectPolicy());
    }

    private static class Holder {
        private static CallbackThreadPool holder = new CallbackThreadPool();
    }

    public static CallbackThreadPool threadPool() {
        return Holder.holder;
    }

    public void execute(Runnable command) {
        try {
            executor.execute(command);
        } catch (Exception ex) {
            throw new TaskException("callback execute error", ex);
        }
    }
}
