package com.horizon.cantor.schedule.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>基于内存存储任务消息</pre>
 * @author : David.Song/Java Engineer
 * @date : 2016/1/27 16:59
 * @see
 * @since : 1.0.0
 */
public class MemoryTaskStore {

    private Logger logger = LoggerFactory.getLogger(MemoryTaskStore.class);
    /**
     * taskId---Task映射存储Map
     * 负责存储所有的计划任务
     */
    private ConcurrentHashMap<String, Task> taskMap;

    /**
     * 1：存储所有计划任务执行的时间点
     * 2: 管理所有的任务执行时间
     */
    private SortedSet<Long> timeTriggers;

    /**
     * 某个时间点存在多少计划任务，时间戳---待执行的计划任务集合 Map
     * 管理某个时间点要执行的Task集合
     */
    private ConcurrentHashMap<Long, List<TaskTrigger>> triggerMap;


    private MemoryTaskStore() {
        taskMap = new ConcurrentHashMap<>(1000);
        triggerMap = new ConcurrentHashMap<>();
        timeTriggers = Collections.synchronizedSortedSet(new TreeSet<>(new TreeNodeComparator()));
    }

    private static class Holder {
        private static MemoryTaskStore holder = new MemoryTaskStore();
    }

    public static MemoryTaskStore taskStore() {
        return Holder.holder;
    }

    public void putTask(Task Task) {
        if (Task != null && Task.getTaskId() != null) {
            taskMap.put(Task.getTaskId(), Task);
        }
    }

    public Task getTask(String TaskId) {
        if (TaskId != null) {
            return taskMap.get(TaskId);
        } else {
            return null;
        }
    }

    public void removeTask(String TaskId) {
        if (TaskId != null) {
            taskMap.remove(TaskId);
        }
    }

    public void clear(){
        if(taskMap != null){
            taskMap.clear();
        }
        if(timeTriggers != null){
            timeTriggers.clear();
        }

        if(triggerMap != null){
            triggerMap.clear();
        }
    }

    /**
     * 添加 时间点-计划任务
     * @param TaskTrigger
     */
    public void putTaskTrigger(TaskTrigger TaskTrigger) {
        if (TaskTrigger != null) {
            List<TaskTrigger> TaskTriggerList = triggerMap.get(TaskTrigger.getTime());
            //如果该时间点已经存在要执行的计划任务，那么就添加到该时间点对应的集合中
            if (TaskTriggerList != null && TaskTriggerList.size() > 0) {
                TaskTriggerList.add(TaskTrigger);
                triggerMap.put(TaskTrigger.getTime(), TaskTriggerList);
            } else {
                // 否则往时间触发器树里面增加一个元素
                timeTriggers.add(TaskTrigger.getTime());
                // triggerMap里面添加一个一对 key-value
                List<TaskTrigger> newTaskTriggerList = new ArrayList<>();
                newTaskTriggerList.add(TaskTrigger);
                triggerMap.put(TaskTrigger.getTime(), newTaskTriggerList);
            }
        }
    }

    public void removeTaskTrigger(TaskTrigger TaskTrigger) {
        if (TaskTrigger != null) {
            timeTriggers.remove(TaskTrigger.getTime());
        }
    }

    /**
     * 遍历时间触发器，如果已经达到执行时间，那么就执行任务回调
     * @param now
     * @return
     */
    public List<TaskTrigger> acquireNextTriggers(long now) {
        List<TaskTrigger> taskTriggerList = null;
        try {
            if (timeTriggers.size() > 0) {
                // 获取离当前时间最近要执行的
                Long time = timeTriggers.first();
                if (time != null) {
                    // 与当前时间比对,如果到达执行时间点
                    if (time <= now) {
                        timeTriggers.remove(time);
                        taskTriggerList = triggerMap.get(time);
                        triggerMap.remove(time);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("acquireNextTriggers error", e);
        }
        return taskTriggerList;
    }

}
