package com.silver.ddrtools.common.manager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ThreadPoolManager
 * @Description TODO
 * @Author Tsui
 * @Date 2022/12/28 10:45
 * @Version 1.0
 **/

public class ThreadPoolManager {
    private static ThreadPoolManager instance;
    private static final int QUEUE_SIZE = 100;
    private static final int CORE_POOL_SIZE = 8;
    private static final int MAX_POOL_SIZE = 8;
    private static final long KEEP_ALIVE_TIME = 0L;
    private static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
    private static final BlockingQueue<Runnable> QUEUE = new LinkedBlockingQueue<>(QUEUE_SIZE);
    private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r);
        }
    };
    private static final RejectedExecutionHandler REJECTED_HANDLER = new ThreadPoolExecutor.CallerRunsPolicy();
    private Executor executor;

    private ThreadPoolManager() {
        executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TIME_UNIT, QUEUE, THREAD_FACTORY, REJECTED_HANDLER);
    }

    public static ThreadPoolManager getInstance() {
        if (instance == null) {
            instance = new ThreadPoolManager();
        }
        return instance;
    }

    public void execute(Runnable task) {
        executor.execute(task);
    }
}
