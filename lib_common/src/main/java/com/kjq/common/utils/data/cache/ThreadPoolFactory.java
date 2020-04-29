package com.kjq.common.utils.data.cache;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 线程池
 */
class ThreadPoolFactory {
    private final static int FIX_THREAD_COUNT = 5;

    private final static int FIX_NET_THREAD_COUNT = 10;

    private volatile static ThreadPoolFactory instance;

    private Executor fixExecutor;

    private Executor netFixExecutor;

    private Executor singleExecutor;

    private Executor immediateExecutor;

    private ThreadPoolFactory() {
        fixExecutor = Executors.newFixedThreadPool(FIX_THREAD_COUNT);
        netFixExecutor = Executors.newFixedThreadPool(FIX_NET_THREAD_COUNT);
        singleExecutor = Executors.newSingleThreadExecutor();
        immediateExecutor = Executors.newCachedThreadPool();
    }

    public static ThreadPoolFactory instance() {
        if (instance == null) {
            synchronized (ThreadPoolFactory.class) {
                if (instance == null) {
                    instance = new ThreadPoolFactory();
                }
            }
        }
        return instance;
    }

    /**
     * 用于可以尽快处理完的任务，比如数据库操作等。
     * @param runnable
     */
    public void fixExecutor(Runnable runnable) {
        fixExecutor.execute(runnable);
    }

    /**
     * 用于比较耗时的同步网络请求等任务
     * @param runnable
     */
    public void netFixExecutor(Runnable runnable) {
        netFixExecutor.execute(runnable);
    }

    /**
     * 处理需要单线程单任务。⚠️ 不要做特别耗时操作
     * @param runnable
     */
    public void singleExecutor (Runnable runnable) {
        singleExecutor.execute(runnable);
    }

    public void executeImmediately(Runnable runnable){
        immediateExecutor.execute(runnable);
    }
}
