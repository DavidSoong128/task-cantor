package com.horizon.cantor.schedule.core;

import java.io.Serializable;
import java.util.Comparator;

/**
 * <pre>时间戳比较器，时间越早的排序在前面，
 * 比如说15:20的计划任务要早于15:21的计划任务
 * </pre>
 * @author : David.Song/Java Engineer
 * @date : 2016/1/27 17:24
 * @see
 * @since : 1.0.0
 */
public class TreeNodeComparator implements Comparator<Long>,Serializable{
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public int compare(Long o1, Long o2) {
        if(o1 != null && o2 != null){
            if(o1 > o2){//时间越老的值越小，则需要排在集合前面 o1和o2排序之后为，o2,o1,所以o2先执行
                return 1;
            }
            if(o1 < o2){
                return -1;
            }
        }
        return 0;
    }
}
