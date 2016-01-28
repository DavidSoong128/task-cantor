package com.horizon.cantor.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池管理
 * @author : David.Song/Java Engineer
 * @date : 2016/1/27 15:28
 * @see
 * @since : 1.0.0
 */
public class ThreadPoolManager {

    private ExecutorService  threadPool;

    private ThreadPoolManager(){
        threadPool = Executors.newCachedThreadPool();
    }

    private static class PoolHolder{
        private static ThreadPoolManager manager = new ThreadPoolManager();
    }

    public ThreadPoolManager poolManager(){
        return PoolHolder.manager;
    }

    public void execute(Runnable task){
        threadPool.execute(task);
    }

    public void submit(Runnable task){
        threadPool.submit(task);
    }

    public void shutdown(){
        threadPool.shutdown();
    }

}
