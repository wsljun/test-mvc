package com.huixueyun.tifenwang.model.utils;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.os.Build;
import android.text.ClipboardManager;
import android.text.TextUtils;

@SuppressWarnings("deprecation")
public class CacheUtil {
    /**
     * 将文字复制到剪切板上
     *
     * @param context
     * @param text
     */
    public static void copy(Context context, String text) {
        if (TextUtils.isEmpty(text))
            return;

        ClipboardManager c = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        c.setText(text);// 设置Clipboard 的内容
    }

    /**
     * @param context
     * @param content 要分享的字符串
     **/
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void setClipBoard(Context context, String content) {
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.HONEYCOMB) {// 这里已经判断版本号了
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", content);
            clipboard.setPrimaryClip(clip);
        } else {
            ClipboardManager clipboard = (ClipboardManager) context
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(content);
        }
    }

    /**
     * 实现粘贴功能
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static String paste(Context context) {
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.HONEYCOMB) {// 这里已经判断版本号了
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            return clipboard.getText().toString().trim();
        } else {
            ClipboardManager clipboard = (ClipboardManager) context
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            return clipboard.getText().toString().trim();
        }
    }

}
