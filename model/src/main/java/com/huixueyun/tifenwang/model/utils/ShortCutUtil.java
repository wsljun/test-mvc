package com.huixueyun.tifenwang.model.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import java.io.UnsupportedEncodingException;

/**
 * 快捷方式相关.
 * <p/>
 * <pre>
 * 需要申请以下权限
 * &lt;uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
 * &lt;uses-permission android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT" />
 * </pre>
 */
public class ShortCutUtil {
    /**
     * 是否已经创建快捷方式。(改方法暂时还有问题，许多手机无法正常获得是否已经创建快捷方式了)
     *
     * @param context
     * @param shortCutName 快捷方式名称
     * @return 是否已经创建快捷方式
     */
    public static boolean hasShortcut(Context context, String shortCutName) {
        boolean isInstallShortcut = false;
        ContentResolver cr = context.getContentResolver();
        String AUTHORITY = "com.android.launcher2.settings";
        Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");

        String title = shortCutName;
        try {
            title = new String(shortCutName.getBytes(), "utf-8");
        } catch (UnsupportedEncodingException e) {

        }

        Cursor c = cr.query(CONTENT_URI, new String[]{"title", "iconResource"}, "title=?", new String[]{title},
                null);
        if (c != null && c.getCount() > 0) {
            isInstallShortcut = true;
        }
        CloseUtil.close(c);

        return isInstallShortcut;
    }

    /**
     * 创建快捷方式
     *
     * @param act          需要创建的快捷方式的入口界面
     * @param shortcutName 需要创建的快捷方式的名称
     * @param iconResId    快捷方式的icon资源id
     */
    public static void addShortcutToDesktop(Activity act, String shortcutName, int iconResId) {
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重建
        shortcut.putExtra("duplicate", false);
        // 设置名字
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        // 设置图标
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(act, iconResId));
        // 设置意图和快捷方式关联程序
        Intent intent = new Intent(act, act.getClass());
        // 桌面图标和应用绑定，卸载应用后系统会同时自动删除图标
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");

        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

        // 发送广播
        act.sendBroadcast(shortcut);
    }

    /**
     * 删除快捷方式
     *
     * @param act          快捷方式绑定的界面
     * @param shortcutName 快捷方式的名称
     */
    public static void delShortcut(Activity act, String shortcutName) {
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");

        // 快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        String appClass = act.getPackageName() + "." + act.getLocalClassName();
        ComponentName comp = new ComponentName(act.getPackageName(), appClass);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));

        act.sendBroadcast(shortcut);
    }
}
