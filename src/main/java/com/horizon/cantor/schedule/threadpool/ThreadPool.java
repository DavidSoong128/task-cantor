package com.horizon.cantor.schedule.threadpool;


public interface ThreadPool {

	
    boolean runInThread(Runnable runnable);

    
    int blockForAvailableThreads();

    
    void initialize() throws Exception;

    
    void shutdown(boolean waitForJobsToComplete);

   
    int getPoolSize();

    
    void setInstanceId(String schedInstId);

   
    void setInstanceName(String schedName);
}
