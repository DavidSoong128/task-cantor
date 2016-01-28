package com.horizon.cantor.schedule.callback;

import com.horizon.cantor.node.callback.TaskCallbackHandler;
import com.horizon.cantor.schedule.core.Task;
import com.horizon.cantor.schedule.core.MemoryTaskStore;
import com.horizon.cantor.schedule.core.TaskTrigger;
import com.horizon.cantor.schedule.threadpool.CallbackThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * <pre>任务到达指定时间执行回调，查找转发达到处理时间的计划任务线程<pre/>
 * @author : David.Song/Java Engineer
 * @date : 2016/1/28 9:57
 * @see
 * @since : 1.0.0
 */
public class TaskCallbackScheduler implements Runnable{

    private Logger logger = LoggerFactory.getLogger(TaskCallbackScheduler.class);

    private CallbackThreadPool callbackThreadPool = CallbackThreadPool.threadPool();

    private MemoryTaskStore taskStore = MemoryTaskStore.taskStore();
    /**
     * 线程睡眠时间
     */
    private static final long SLEEP_TIME = 100L;
    /**
     * 回调业务核心处理类
     */
    private TaskCallbackHandler callbackHandler;

    public TaskCallbackScheduler(TaskCallbackHandler callbackHandler){
        this.callbackHandler = callbackHandler;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 从taskStore中回去下一个要执行的TaskTrigger(task触发器)
                List<TaskTrigger> jobTriggerList = taskStore.acquireNextTriggers(System.currentTimeMillis());
                if (jobTriggerList == null || jobTriggerList.size() <= 0) {
                    Thread.sleep(SLEEP_TIME);
                    continue;
                }
                for (TaskTrigger taskTrigger : jobTriggerList) {
                    // 获取相应的job
                    Task task = taskStore.getTask(taskTrigger.getTaskId());
                    if (task == null) {
                        continue;
                    }
                    if (taskTrigger.getTime() == task.getTime()) {
                        // remove掉
                        taskStore.removeTask(task.getTaskId());
                        // 放入到线程池中执行
                        callbackThreadPool.execute(new TaskCallbackRunnable(callbackHandler, task));
                    }
                }
            } catch (Exception e) {
                logger.error("taskCallback error", e);
            }
        }
    }
}
