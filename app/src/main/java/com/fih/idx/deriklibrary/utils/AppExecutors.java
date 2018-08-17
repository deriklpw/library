package com.fih.idx.deriklibrary.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Global executor pools for the whole application.
 * <p>
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 */
public class AppExecutors {
    private static final int THREAD_COUNT = 3;
    private Executor mDiskIO;
    private Executor mNetworkIO;
    private Executor mMainThread;

    AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread){
        mDiskIO = diskIO;
        mNetworkIO = networkIO;
        mMainThread = mainThread;
    }

    public AppExecutors(){
        this(Executors.newSingleThreadExecutor(),
                Executors.newFixedThreadPool(THREAD_COUNT),
                new MainThreadExecutor());
    }


    /**
     *
     * @return executor for DiskIO operate
     */
    public Executor getDiskIO() {
        return mDiskIO;
    }

    /**
     *
     * @return executor for NetworkIO operate
     */
    public Executor getNetworkIO() {
        return mNetworkIO;
    }

    /**
     *
     * @return executor for MainThread operate
     */
    public Executor getMainThread() {
        return mMainThread;
    }

    private static class MainThreadExecutor implements Executor {

        private Handler handler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(Runnable runnable) {
            if (runnable == null) {
                throw new IllegalArgumentException("Illegal argument.");
            }
            handler.post(runnable);
        }
    }

}
