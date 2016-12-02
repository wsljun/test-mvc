package com.huixueyun.tifenwang.model.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug.MemoryInfo;
import android.os.Environment;
import android.os.Process;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 用于记录当前内存使用情况以及CPU使用情况
 */
public class MemoLog {
    private static MemoLog instance;

    private ActivityManager am;
    private Timer timer;
    private String cachePath;

    /**
     * 构造函数
     *
     * @param context
     */
    private MemoLog(Context context) {
        am = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        cachePath = getCacheFolder(context);
    }

    /**
     * MemLog单例
     *
     * @param context
     * @return
     */
    public static MemoLog getInstance(Context context) {
        if (instance == null) {
            synchronized (MemoLog.class) {
                if (instance == null)
                    instance = new MemoLog(context);
            }
        }

        return instance;
    }

    /**
     * 开始记录内存、CPU使用情况（CPU使用情况必须root）
     */
    public void start() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA);
        final String filePath = cachePath + "memo_" + sdf.format(new Date()) + ".csv";
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startLog(filePath);
            }
        }, 1000, 1000);
    }

    /**
     * 停止记录内存、CPU使用情况
     */
    public void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }

    /**
     * 开始记录log
     *
     * @param filepath
     */
    private void startLog(String filepath) {
        FileWriter writer = null;
        try {
            File file = new File(filepath);
            StringBuffer sb = new StringBuffer();

            if (!file.exists()) {// 说明是新建的日志，需要写入头信息
                writer = new FileWriter(file, true);
                sb.append("Time,");// 时间

                sb.append("CPU,");// 剩余内存

                sb.append("AvailMem,");// 剩余内存
                sb.append("USS,");//
                sb.append("PSS,");//
                sb.append("RSS,");//

                // Dalvik层
                sb.append("DalvikUSS,");//
                sb.append("DalvikPSS,");//
                sb.append("DalvikRSS,");//

                // native层
                sb.append("NativeUSS,");//
                sb.append("NativePSS,");//
                sb.append("NativeRSS");//
                sb.append("\r\n");
            }

            if (writer == null) {
                writer = new FileWriter(file, true);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.CHINA);
            sb.append(sdf.format(new Date()));// 写入时间
            sb.append(",");

            sb.append(getCpuUsage());// 写入当前CPU使用率(百分比)
            sb.append(",");

            ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
            am.getMemoryInfo(outInfo);
            long availMem = outInfo.availMem;
            sb.append(availMem / 1024);// 写入当前剩余内存(KB)
            sb.append(",");

            int[] pids = new int[]{Process.myPid()};
            MemoryInfo[] memoryInfoArray = am.getProcessMemoryInfo(pids);
            if (memoryInfoArray != null && memoryInfoArray.length != 0) {
                MemoryInfo pidMemoryInfo = memoryInfoArray[0];

                // 整体相关 KB
                int uss = pidMemoryInfo.getTotalPrivateDirty();// USS
                int pss = pidMemoryInfo.getTotalPss();// PSS
                int rss = pidMemoryInfo.getTotalSharedDirty();// RSS
                sb.append(uss);
                sb.append(",");
                sb.append(pss);
                sb.append(",");
                sb.append(rss);
                sb.append(",");

                // dalvik相关 KB
                int dalvikPrivateDirty = pidMemoryInfo.dalvikPrivateDirty;
                int dalvikPss = pidMemoryInfo.dalvikPss;// dalvikPss
                int dalvikSharedDirty = pidMemoryInfo.dalvikSharedDirty;
                sb.append(dalvikPrivateDirty);
                sb.append(",");
                sb.append(dalvikPss);
                sb.append(",");
                sb.append(dalvikSharedDirty);
                sb.append(",");

                // native相关 KB
                int nativePrivateDirty = pidMemoryInfo.nativePrivateDirty;
                int nativePss = pidMemoryInfo.nativePss;// nativePss
                int nativeSharedDirty = pidMemoryInfo.nativeSharedDirty;
                sb.append(nativePrivateDirty);
                sb.append(",");
                sb.append(nativePss);
                sb.append(",");
                sb.append(nativeSharedDirty);
                sb.append(",");

                sb.append("\r\n");
                writer.write(sb.toString());// 写入到文件中
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }

    }

    /**
     * 获取CPU的使用率(百分比)
     *
     * @return CPU的使用率(百分比 取值范围0-100)
     */
    public int getCpuUsage() {
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
            String load = reader.readLine();
            String[] toks = load.split(" ");
            long idle1 = Long.parseLong(toks[4]);
            long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            try {
                Thread.sleep(360);
            } catch (Exception e) {
                e.printStackTrace();
            }

            reader.seek(0);
            load = reader.readLine();
            reader.close();
            toks = load.split(" ");
            long idle2 = Long.parseLong(toks[5]);

            long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            return (int) (100 * (cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1)));

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception e) {

        }
        return 0;

    }

    /**
     * 获取缓存文件夹
     *
     * @param context
     * @return
     */
    private String getCacheFolder(Context context) {
        String path;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 有SD卡
            path = Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + context.getPackageName()
                    + "/memo/";
        } else {
            path = Environment.getDataDirectory().getPath() + "/data/" + context.getPackageName() + "/memo/";
        }

        File file = new File(path);
        if (!file.isDirectory()) {
            file.mkdirs();
        }

        return path;
    }

}
