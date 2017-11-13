package com.wirelesskings.wkreload.executor;

import android.support.annotation.NonNull;


import com.wirelesskings.wkreload.domain.executor.ThreadExecutor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class JobExecutor implements ThreadExecutor {
    private static final ThreadExecutor ourInstance = new JobExecutor();

    public static ThreadExecutor getInstance() {
        return ourInstance;
    }

    private final ThreadPoolExecutor threadPoolExecutor;

    private JobExecutor() {
        this.threadPoolExecutor = new ThreadPoolExecutor(3,
                5,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(), new JobThreadFactory()
        );
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        this.threadPoolExecutor.execute(runnable);
    }

    private static class JobThreadFactory implements ThreadFactory {
        private int counter = 0;

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            return new Thread(runnable, "android_" + counter++);
        }
    }
}
