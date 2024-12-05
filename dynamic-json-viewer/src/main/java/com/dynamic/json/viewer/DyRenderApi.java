package com.dynamic.json.viewer;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import kotlin.jvm.functions.Function0;

public class DyRenderApi {

    public static Application application;

    public static Application getApplication() {
        return application;
    }

    public static Context getAppContext() {
        return getApplication().getApplicationContext();
    }

    private static String packageName = null;

    public static String getPackageName() {
        if (packageName == null) {
            packageName = getApplication().getPackageName();
        }
        return packageName;
    }

    public static Function0<Activity> topActivity;

    public static Activity getTopActivity() {
        return topActivity.invoke();
    }
}
