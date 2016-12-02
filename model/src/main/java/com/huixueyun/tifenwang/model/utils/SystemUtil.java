package com.huixueyun.tifenwang.model.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.provider.Settings;

import com.huixueyun.tifenwang.model.model.LoginInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;


/**
 * 安卓系统及应用程序相关类. <br/>
 * 在使用该方法之前, 需要先调用 {@link #init(Context)}初始化一下.
 * 如果应用程序的Application是继承自{@link BaseApplication}, 则不需要再进行初始化了, 可以直接使用
 *
 */
public class SystemUtil {
    private static Context context;

    public static void init(Context ctx) {
        context = ctx;
    }

    /**
     * 获取当前应用程序的版本号(获取AndroidManifest文件中android:versionName属性值)
     *
     * @return 当前应用程序的版本号
     */
    public static String getAppVersionName() {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            // 应用程序版本名称, 例如2.1.1
            String appVersionName = info.versionName;
            return appVersionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return "0.0";
    }

    /**
     * 获取当前应用程序的版本号(获取AndroidManifest文件中android:versionCode属性值)
     *
     * @return 当前应用程序的版本号
     */
    public static int getAppVersionCode() {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            // 应用程序的版本号
            int appVersionCode = info.versionCode;

            return appVersionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return 1;
    }

    /**
     * 获取手机的型号信息, 例如 Xiaomi_2S
     *
     * @return 手机的型号信息
     */
    public static String getModelInfo() {
        return Build.MANUFACTURER + "_" + Build.MODEL;
    }

    /**
     * 获取手机操作系统的版本号, 例如4.0.3
     *
     * @return 手机操作系统的版本号
     */
    public static String getOsVersionName() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机Sdk版本
     *
     * @return 手机Sdk版本
     */
    public static int getSdkVersionName() {
        return Build.VERSION.SDK_INT;
    }

    public static String getDeviceId() {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    /**
     * 模拟系统Home键按下效果方式退出app
     *
     * @param context
     */
    public static void exitApp(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(intent);
    }


    /**
     * 写入应用缓存文件
     *
     * @param context
     * @param filename
     * @param info
     * @throws Exception
     */
    public static void writeInfo(Context context, String filename, HashMap<String, LoginInfo> info) throws Exception {
        File file = getAppCacheDir(context, filename);
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(info);
        oos.flush();
        oos.close();
    }

    /**
     * 读取应用缓存文件
     *
     * @param context
     * @param filename
     * @return
     * @throws Exception
     */
    public static HashMap<String, LoginInfo> readInfo(Context context, String filename) throws Exception {
        File file = getAppCacheDir(context, filename);
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        HashMap<String, LoginInfo> imInfo = (HashMap<String, LoginInfo>) ois.readObject();
        ois.close();

        return imInfo;
    }

    /**
     * 获取应用缓存路径
     *
     * @param context
     * @param fileName
     * @return
     */
    public static File getAppCacheDir(Context context, String fileName) {
        String cachePath = context.getFilesDir().getPath();

        L.i("file", cachePath + File.separator + fileName);
        return new File(cachePath + File.separator + fileName);
    }
}
