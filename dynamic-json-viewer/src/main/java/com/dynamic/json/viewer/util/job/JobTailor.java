package com.dynamic.json.viewer.util.job;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.dynamic.json.viewer.util.DyLogger;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * JobTailor 防弹跳 (主线程)
 * <p/>
 * 在主线程执行队列最后一个任务
 * <p/>
 * bounceInterval: 在防弹跳间隔时间期间，若这段时间内一下子来了多个任务，只保留和只执行最后一个
 */
public class JobTailor {

    private static final String TAG = "JobTailor";

    private int bounceInterval = 200; // millis seconds

    private final ArrayList<Runnable> mQueue = new ArrayList<>();

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public JobTailor() {
        this(200);
    }

    public JobTailor(int bounceInterval) {
        this.bounceInterval = bounceInterval;
    }

    public void execute(Runnable runnable) {
        mQueue.clear();
        mQueue.add(runnable);

        // cancel previous and post
        mHandler.removeCallbacks(queueExecutor);
        mHandler.postDelayed(queueExecutor, bounceInterval);
    }

    private final Runnable queueExecutor = () -> {
        try {
            if (mQueue.isEmpty()) {
                return;
            }
            Runnable job = mQueue.remove(mQueue.size() - 1);
            job.run();
        } catch (Exception e) {
            e.printStackTrace();
            DyLogger.e(TAG, TAG + " exception: " + e.getMessage());
            DyLogger.e(TAG, e);
        }
    };

    /**
     * Class instances
     */
    private static HashMap<String, JobTailor> instances = new HashMap<>();

    public static JobTailor getInstance(String key) {
        return getInstance(key, 200);
    }

    public static JobTailor getInstance(String key, int bounceInterval) {
        JobTailor instance = instances.get(key);
        if (instance == null) {
            synchronized (JobTailor.class) {
                instance = instances.get(key); // double check, maybe another thread has created it
                if (instance == null) {
                    instance = new JobTailor(bounceInterval);
                    instances.put(key, instance);
                }
            }
        }
        return instance;
    }

    public static JobTailor removeInstance(String key) {
        return instances.remove(key);
    }

    public static void debounce(@NonNull String key, @NonNull Runnable runnable) {
        getInstance(key).execute(runnable);
    }
}
