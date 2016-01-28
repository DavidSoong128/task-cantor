package com.horizon.cantor.transfer.core;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 用作临时存储任务消息队列
 * @author : David.Song/Java Engineer
 * @date : 2016/1/27 14:42
 * @see
 * @since : 1.0.0
 */
public class TaskMsgStoreQueue {

    private ArrayBlockingQueue<Object> queue = new ArrayBlockingQueue<Object>(50000);

    private TaskMsgStoreQueue(){
    }
    private static class Holder{
        private static TaskMsgStoreQueue holder = new TaskMsgStoreQueue();
    }
    public static TaskMsgStoreQueue queueHolder(){
        return Holder.holder;
    }

    public Object get() throws InterruptedException{
        return queue.take();
    }

    public void put(Object msg){
        try {
            queue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void clear(){
        queue.clear();
    }
    public boolean isEmpty(){
        return queue.isEmpty();
    }
}
