package com.huixueyun.tifenwang.model.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 自动保存崩溃日志, 并且在崩溃的时候, 会自动重启应用程序. <br/>
 * 崩溃日志会自动保存在 <b> /sdcard/Android/data/应用程序包名/crash </b>目录下. 如果没有外置存储卡, 则会保存在 <b>
 * /data/data/应用程序包名/crash </b>目录下
 */
public class CrashHandler implements UncaughtExceptionHandler {
    /**
     * 为了防止程序无限重启, 如果在{@value #MAX_CRASH_TIME_DIFF}秒内连续崩溃 {@value} 次, 则直接退出,
     * 不会再重启了
     */
    private static final int MAX_CRASHH_RETRY_COUNT = 3;
    /**
     * 为了防止程序无限重启, 如果在 {@value} 秒内连续崩溃 {@value #MAX_CRASHH_RETRY_COUNT} 次,
     * 则直接退出, 不会再重启了
     */
    private static final int MAX_CRASH_TIME_DIFF = 20;
    // 崩溃日志的扩展名
    private static final String CRASH_REPORTER_EXTENSION = ".log";
    // 系统默认的UncaughtException处理类
    private static UncaughtExceptionHandler mDefaultHandler;
    // CrashHandler实例
    private static CrashHandler instance;
    // 程序的Application Context对象
    private static Context context;
    private int crashCount;
    private long firstCrashTime;

    /**
     * 构造函数
     */
    private CrashHandler() {

    }

    /**
     * 在Activity或Application中初始化均可以
     *
     * @param ctx
     */
    public static void init(Context ctx) {
        if (instance == null) {
            instance = new CrashHandler();
        }

        context = ctx.getApplicationContext();
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(instance);
    }

    /**
     * 获取缓存路径
     *
     * @param c
     * @return
     */
    public static String getCachePath(Context c) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {// 有sdcard可用

            String externalCachePath = Environment
                    .getExternalStorageDirectory().getPath()
                    + "/Android/data/"
                    + c.getPackageName() + "/crash/";
            File cacheFolder = new File(externalCachePath);
            if (cacheFolder.exists() || cacheFolder.mkdirs()) {
                // 文件存在或者创建缓存文件夹成功，使用sdcard
                return externalCachePath;
            }
        }

        // 使用手机内部存储卡
        return c.getCacheDir().getAbsolutePath() + "/crash/";
    }

    /**
     * 清理崩溃日志文件
     *
     * @return 如果清理成功, 则返回true; 否则返回false
     */
    public static boolean clearCrashFile() {
        String crashFilePath = getCachePath(context);

        if (crashFilePath == null)
            return false;

        File file = new File(crashFilePath);
        if (file.exists()) {
            return file.delete();
        } else {
            return true;
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (!handleException(throwable) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, throwable);
        } else {
            long current = System.currentTimeMillis();
            if (current - firstCrashTime > MAX_CRASH_TIME_DIFF * 1000) {
                firstCrashTime = current;
            }

            crashCount++;
            if (crashCount >= MAX_CRASHH_RETRY_COUNT) {
                if (current - firstCrashTime < MAX_CRASH_TIME_DIFF * 1000) {
                    // 在20秒内崩溃了3次
                    firstCrashTime = 0;
                    crashCount = 0;

                    // 直接退出程序
                    System.exit(0);
                } else {// 超过3次, 但是时间比较久
                    crashCount = 0;
                    firstCrashTime = current;
                }

            } else {
                // Sleep一会后结束重启程序
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.exit(16);
            }
        }
    }

    /**
     * 异常处理
     *
     * @param ex
     * @return
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return true;
        }
        ex.printStackTrace();
        return saveCrashInfoToFile(ex) != null;
    }

    /**
     * 将info保存到文件
     *
     * @param ex
     * @return
     */
    private String saveCrashInfoToFile(Throwable ex) {
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);

        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        // 系统信息
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                printWriter.append("\r\nVersionName=" + pi.versionName);
                printWriter.append("\r\nVersionCode=" + pi.versionCode);
            }
        } catch (NameNotFoundException e) {
            L.e(e, "Error while collect package info");
        }
        // 使用反射来收集设备信息.在Build类中包含各种设备信息,
        // 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
        // 具体信息请参考后面的截图
        Field[] fields = Build.class.getDeclaredFields();
        if (fields != null) {
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    printWriter.append("\r\n" + field.getName() + ": "
                            + field.get(null));
                } catch (Exception e) {
                    L.e(e, "Error while collect crash info");
                }
            }
        }

        String result = info.toString();
        printWriter.close();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss",
                    Locale.US);
            String fileName = getCachePath(context) + "/crash_"
                    + sdf.format(new Date()) + CRASH_REPORTER_EXTENSION;
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(result);
            writer.flush();
            writer.close();
            return fileName;
        } catch (IOException e) {
            L.e(e, "an error occured while writing report file...");
        }
        return null;
    }
}
