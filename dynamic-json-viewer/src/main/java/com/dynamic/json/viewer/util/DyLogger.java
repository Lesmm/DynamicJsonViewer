package com.dynamic.json.viewer.util;

import androidx.annotation.NonNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/** @noinspection ALL*/
public class DyLogger {

    private static final String TAG = DyLogger.class.getSimpleName();

    private static final String kFORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    private static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat(kFORMAT, Locale.CHINA);
    }

    /**
     * simple log
     */

    /// default simple logger
    public static Logger logger = log -> {
        android.util.Log.i(TAG, "[" + getDateFormat().format(new Date()) + "]: " + log);
    };

    public static interface Logger {
        void log(String message);
    }

    public static void log(String message) {
        if (logger == null) return;
        logger.log(message);
    }

    /**
     * verbose log
     */

    /// default verbose logger
    public static LoggerVerbose verboseLogger = (level, module, file, line, function, message) -> {
        String log = "[" + getDateFormat().format(new Date()) + "]: " + file + ":" + line + " " + function + " " + message;
        switch (level) {
            case DEBUG:
                android.util.Log.d(TAG, log);
                break;
            case INFO:
                android.util.Log.i(TAG, log);
                break;
            case WARN:
                android.util.Log.w(TAG, log);
                break;
            case ERROR:
            case FATAL:
                android.util.Log.e(TAG, log);
                break;
        }
    };


    public static enum Level {
        NONE(0), DEBUG(1), INFO(2), WARN(3), ERROR(4), FATAL(5);

        final int value;

        Level(int value) {
            this.value = value;
        }
    }

    public interface LoggerVerbose {
        void log(Level level, String module, String file, int line, String function, String message);
    }

    public static void d(String message) {
        d(TAG, message);
    }

    public static void d(String tag, String message) {
        verbose(tag, Level.DEBUG, message);
    }

    public static void i(String message) {
        i(TAG, message);
    }

    public static void i(String tag, String message) {
        verbose(tag, Level.INFO, message);
    }

    public static void w(String message) {
        w(TAG, message);
    }

    public static void w(String tag, String message) {
        verbose(tag, Level.WARN, message);
    }

    public static void e(String message) {
        e(TAG, message);
    }

    public static void e(String tag, String message) {
        verbose(tag, Level.ERROR, message);
    }

    public static void e(Throwable e) {
        e(TAG, e);
    }

    public static void e(String tag, Throwable e) {
        verbose(tag, Level.ERROR, getThrowableMessage(e));
    }

    public static void f(String message) {
        f(TAG, message);
    }

    public static void f(String tag, String message) {
        verbose(tag, Level.FATAL, message);
    }

    public static void f(Throwable e) {
        f(TAG, e);
    }

    public static void f(String tag, Throwable e) {
        verbose(tag, Level.FATAL, getThrowableMessage(e));
    }

    @NonNull
    public static String getThrowableMessage(@NonNull Throwable e) {
        return "\n" + e.getMessage() + "\n" + getStackTrace(e) + "\n";
    }

    @NonNull
    public static String getStackTrace(@NonNull Throwable e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors, true));
        return errors.toString();
    }

    private static void verbose(Level level, String message) {
        verbose(TAG, level, message);
    }

    private static void verbose(String tag, Level level, String message) {
        if (verboseLogger == null) {
            log(level.toString() + ":" + message);
            return;
        }
        if (tag == null) {
            tag = TAG;
        }
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length < 2) {
            log(level.toString() + ":" + message + "\n" + Arrays.toString(stackTrace));
            return;
        }
        StackTraceElement element = stackTrace[1];
        // String className = element.getClassName();
        String methodName = element.getMethodName();
        String fileName = element.getFileName();
        int lineNumber = element.getLineNumber();
        verboseLogger.log(level, tag, fileName, lineNumber, methodName, message);
    }

    /**
     * module log
     */
    public interface LoggerModule {
        void log(String module, String message);
    }

    public static LoggerModule moduleLogger = null;

    // 针对 DEBUG/PRODUCTION 环境, 可配置 loggerModule null/not-null 来控制是否来组装 module log
    public static void console(Runnable runnable) {
        if (moduleLogger == null) return;
        if (runnable != null) {
            try {
                runnable.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 自定义 module 让外部处理，不关心 level/file/line/function 这些信息
    public static void console(String module, String message) {
        if (moduleLogger == null) return;
        moduleLogger.log(module, message);
    }
}
