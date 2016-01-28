package com.horizon.cantor.transfer.core;

/**
 * taskMap包括以下5个key:
 * 1: taskId: value
 * 2: operator: value
 * 3: executeTime: value
 * 4: data: value
 * 5: backTopic:value
 * @author : David.Song/Java Engineer
 * @date : 2016/1/27 14:49
 * @see
 * @since : 1.0.0
 */
public class TaskMapKey {

    public static final String TASK_ID_KEY = "taskId";

    public static final String OPERATOR_KEY = "operator";

    public static final String EXECUTE_TIME_KEY = "executeTime";

    public static final String DATA_KEY = "data";

    public static final String BACK_TOPIC_KEY = "backTopic";

    public static final String OP_ADD_KEY = "add";

    public static final String OP_DELETE_KEY = "delete";
}
