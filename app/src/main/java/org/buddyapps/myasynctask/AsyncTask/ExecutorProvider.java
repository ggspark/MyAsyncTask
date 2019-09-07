package org.buddyapps.myasynctask.AsyncTask;

import android.os.Handler;
import android.os.Looper;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Singleton class to provide executors for main thread and background threads
 */
class ExecutorProvider {
    private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private final ThreadPoolExecutor mBackgroundExecutor;
    private final Executor mMainExecutor;
    private static ExecutorProvider mInstance;

    synchronized static ExecutorProvider getInstance() {
        if (mInstance == null) {
            mInstance = new ExecutorProvider();
        }
        return mInstance;
    }

    private ExecutorProvider() {
        mMainExecutor = new MainThreadExecutor();
        mBackgroundExecutor = new ThreadPoolExecutor(
                NUMBER_OF_CORES,
                NUMBER_OF_CORES * 2,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new MyThreadFactory()
        );
    }

    ThreadPoolExecutor getBackgroundExecutor() {
        return mBackgroundExecutor;
    }

    Executor getMainExecutor() {
        return mMainExecutor;
    }

    /**
     * Static inner class to create Main Thread Executor
     */
    private static class MainThreadExecutor implements Executor {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NotNull Runnable runnable) {
            handler.post(runnable);
        }
    }

    /**
     * Static inner class to create Thread factory
     */
    private static class MyThreadFactory implements ThreadFactory {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(@NotNull Runnable r) {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    }

}