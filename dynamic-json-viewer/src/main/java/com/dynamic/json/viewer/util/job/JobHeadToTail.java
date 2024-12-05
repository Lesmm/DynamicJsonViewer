package com.dynamic.json.viewer.util.job;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * JobHeadToTail 防弹跳 (开新线程)
 * <p />
 * 开一个新线程，立马执行队列的第一个
 * <p />
 * 执行完过程中若有更多的任务加入，只保留最后一个
 * <p />
 * 执行结束后发现若还有任务在队列中，则执行最后一个任务
 * <p />
 * 目的是确保执行队列中的第一个和最后一个任务
 */
public class JobHeadToTail {

    private static final String TAG = "JobHeadToTail";

    private Thread mThread = null;

    private final ArrayList<Runnable> mQueue = new ArrayList<>();

    public void start(Runnable runnable) {
        mQueue.clear();
        mQueue.add(runnable);
        // if thread already running, return
        if (mThread != null) {
            return;
        }
        // start a new thread
        mThread = new Thread(() -> {
            runnable.run();
            while (!mQueue.isEmpty()) {
                Runnable last = mQueue.remove(mQueue.size() - 1);
                mQueue.clear();
                last.run();
            }
            mThread = null;
        });
        mThread.setName("JobHeadToTail-Thread");
        mThread.start();
    }


    /**
     * Class instances
     */
    private static HashMap<String, JobHeadToTail> instances = new HashMap<>();

    public static JobHeadToTail getInstance(String key) {
        JobHeadToTail instance = instances.get(key);
        if (instance == null) {
            synchronized (JobHeadToTail.class) {
                instance = instances.get(key); // double check, maybe another thread has created it
                if (instance == null) {
                    instance = new JobHeadToTail();
                    instances.put(key, instance);
                }
            }
        }
        return instance;
    }

    public static JobHeadToTail removeInstance(String key) {
        return instances.remove(key);
    }

    public static void debounce(@NonNull String key, @NonNull Runnable runnable) {
        getInstance(key).start(runnable);
    }
}
