package com.dynamic.json.viewer.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class DyUtils {

    private static volatile Handler mMainHandler;

    private static Handler getMainHandler() {
        if (mMainHandler == null) {
            mMainHandler = new Handler(Looper.getMainLooper());
        }
        return mMainHandler;
    }

    public static void post(Runnable runnable) {
        if (runnable == null) return;
        post(runnable.hashCode(), runnable, 0);
    }

    public static void post(Object tag, Runnable runnable) {
        post(tag, runnable, 0);
    }

    public static void post(Runnable runnable, long delayMillis) {
        if (runnable == null) return;
        post(runnable.hashCode(), runnable, delayMillis);
    }

    public static void postSafely(Runnable runnable) {
        postSafely(runnable, 0);
    }

    public static void postSafely(Runnable runnable, long delayMillis) {
        post(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, delayMillis);
    }

    public static void post(Object tag, Runnable runnable, long delayMillis) {
        if (tag == null || runnable == null) return;
        if (delayMillis <= 0) {
            delayMillis = 0;
        }
        if (tag instanceof Number || tag instanceof CharSequence) {
            tag = tag.toString().intern();
        }
        Message message = Message.obtain(getMainHandler(), runnable);
        message.obj = tag;
        getMainHandler().sendMessageDelayed(message, delayMillis);
    }

    public static void cancel(Runnable runnable) {
        if (runnable == null) return;
        cancel(runnable.hashCode(), runnable);
    }

    public static void cancel(Object tag, Runnable runnable) {
        if (tag == null || runnable == null) return;
        if (tag instanceof Number || tag instanceof CharSequence) {
            tag = tag.toString().intern();
        }
        getMainHandler().removeCallbacks(runnable, tag);
    }

    public static void cancelAll(Object tag) {
        if (tag == null) return;
        if (tag instanceof Number || tag instanceof CharSequence) {
            tag = tag.toString().intern();
        }
        getMainHandler().removeCallbacksAndMessages(tag);
    }

    public static void postAtFront(Runnable runnable) {
        if (runnable == null) return;
        getMainHandler().postAtFrontOfQueue(runnable);
    }

    public static void ensureOnUiThread(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            post(runnable);
        }
    }

}
