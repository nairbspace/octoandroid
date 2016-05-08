package com.nairbspace.octoandroid.data.executor;

import android.support.annotation.NonNull;

import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class JobExecutor implements ThreadExecutor {

    private static final int INITIAL_POOL_SIZE = 3;
    private static final int MAX_POOL_SIZE = 5;

    // Sets the amount of time an idle thread waits before terminating
    private static final int KEEP_ALIVE_TIME = 10;

    // Sets the Time Unit to seconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    @SuppressWarnings("FieldCanBeLocal")
    private final BlockingQueue<Runnable> mWorkQueue;

    @SuppressWarnings("FieldCanBeLocal")
    private final ThreadFactory mThreadFactory;

    private final ThreadPoolExecutor mThreadPoolExecutor;

    @Inject
    public JobExecutor() {
        this.mWorkQueue = new LinkedBlockingDeque<>();
        this.mThreadFactory = new JobThreadFactory();
        this.mThreadPoolExecutor = new ThreadPoolExecutor(INITIAL_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, mWorkQueue, mThreadFactory);
    }

    @Override
    public void execute(@NonNull Runnable command) {
        mThreadPoolExecutor.execute(command);

    }

    private static class JobThreadFactory implements ThreadFactory {
        private static final String THREAD_NAME = "android_";
        private int mCounter = 0;

        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, THREAD_NAME + mCounter++);
        }
    }
}
