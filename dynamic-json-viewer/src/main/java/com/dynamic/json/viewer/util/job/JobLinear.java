package com.dynamic.json.viewer.util.job;

import android.os.Handler;
import android.os.Looper;

import com.dynamic.json.viewer.util.DyLogger;

import java.util.ArrayList;

/**
 * JobLinear 按顺序执行完各个任务 (主线程)
 * <p/>
 * 在主线程 把队列里的任务一个接一个地执行完
 */
public class JobLinear {

    /**
     * Job Element
     */
    public static class Job {
        public long delay = 0;         // delay for run
        public Runnable runnable;

        public Job(long delay, Runnable runnable) {
            this.delay = delay;
            this.runnable = runnable;
        }
    }

    /**
     * JobLinear members
     */
    private static final String TAG = "JobLinear";

    private Runnable mQueueChecker = null;

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private final ArrayList<Job> mQueue = new ArrayList<>();

    /**
     * Add a job to the queue
     */
    public JobLinear add(Job job) {
        mQueue.add(job);
        return this;
    }

    /**
     * Start the job queue
     */
    public void start() {
        if (mQueue.isEmpty()) return;
        if (mQueueChecker != null) return;  // start once, just add please

        mQueueChecker = () -> {
            if (mQueue.isEmpty() || mQueueChecker == null) {
                mQueueChecker = null;
                return;
            }

            Job job = mQueue.remove(0);

            // start execute the job
            mHandler.postDelayed(() -> {
                try {
                    job.runnable.run();
                } catch (Exception e) {
                    e.printStackTrace();
                    DyLogger.e(TAG, TAG + " exception: " + e.getMessage());
                    DyLogger.e(TAG, e);
                }
                // repeat check
                mHandler.post(mQueueChecker);
            }, job.delay);
        };
        mHandler.post(mQueueChecker);
    }
}
