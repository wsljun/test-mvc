package com.huixueyun.tifenwang.model.utils;

import android.graphics.Typeface;
import android.os.Environment;
import android.os.StatFs;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AppUtils {
    /**
     * 获取Sd卡路径
     *
     * @return 获取Sd卡路径
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断SD卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取SD卡路径
            return sdDir.toString();
        } else {
            return null;
        }

    }

    /**
     * 获取SDcard剩余空间
     *
     * @return SDcard剩余空间（单位为MIB）
     */
    public static float getSDAvailSize() {
        File sdcardDir = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(sdcardDir.getPath());
        long blockSize = sf.getBlockSize();
        long availCount = sf.getAvailableBlocks();
        return availCount * blockSize / 1024 / 1024;
    }

    /**
     * 获取MimeType
     *
     * @param url
     * @return MimeType
     */
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    /**
     * 获取文件扩展名
     *
     * @param filename
     * @return 文件扩展名
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * 从文件路径中获取文件名
     *
     * @param filename
     * @return 文件名
     */
    public static String getFileName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            if (filename.contains("/")) {
                return filename.substring(filename.lastIndexOf("/") + 1,
                        filename.length());
            }
        }
        return filename;
    }

    /**
     * 设置变量非空（非NULL）
     *
     * @param str
     * @return 设置变量非空
     */
    public static String val(String str) {
        if (null == str || "".equals(str) || "NULL".equalsIgnoreCase(str)) {
            return "";
        } else {
            return str;
        }
    }

    /**
     * 判断变量是否为空（包含：NULL、""、"NULL"）
     *
     * @param str
     * @return 变量是否为空
     */
    public static boolean isNull(String str) {
        if (null == str || "".equals(str) || "NULL".equalsIgnoreCase(str)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 格式化时间字符串
     *
     * @param timestamp
     * @return 返回格式：yyyy-MM-dd HH:mm:ss
     */
    public static String formatTime(Timestamp timestamp) {
        if (timestamp == null)
            return null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINA);
        return format.format(timestamp);
    }

    /**
     * 格式化时间字符串
     *
     * @param d
     * @return 返回格式：yyyy-MM-dd HH:mm:ss
     */
    public static String formatTime(long d) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINA);
        return format.format(new Date(d));
    }

    /**
     * 自定义格式 格式化时间字符串
     *
     * @param d
     * @param fmt 要格式化的时间格式，如（yyyy-MM-dd HH:mm:ss）
     * @return 格式化时间字符串
     */
    public static String formatDateTime(long d, String fmt) {
        SimpleDateFormat format = new SimpleDateFormat(fmt, Locale.CHINA);
        return format.format(new Date(d));
    }

    /**
     * 自定义格式 格式化时间字符串
     *
     * @param timestamp
     * @param fmt       要格式化的时间格式，如（yyyy-MM-dd HH:mm:ss）
     * @return 格式化时间字符串
     */
    public static String formatDateTime(Timestamp timestamp, String fmt) {
        SimpleDateFormat format = new SimpleDateFormat(fmt, Locale.CHINA);
        return format.format(timestamp);
    }

    /**
     * 解析时间字符串
     *
     * @param dateTimeString
     * @return Timestamp
     */
    public static Timestamp parseTime(String dateTimeString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINA);
        if (isNull(dateTimeString))
            return null;
        Timestamp ret = null;
        try {
            ret = new Timestamp(format.parse(dateTimeString).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 解析时间字符串
     *
     * @param dateTimeString
     * @param fmt
     * @return Timestamp
     */
    public static Timestamp parseTime(String dateTimeString, String fmt) {
        SimpleDateFormat format = new SimpleDateFormat(fmt,
                Locale.CHINA);
        if (isNull(dateTimeString))
            return null;
        Timestamp ret = null;
        try {
            ret = new Timestamp(format.parse(dateTimeString).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的日期
     * @param bdate  较大的日期
     * @return 天数
     * @throws ParseException
     */
    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Math.abs(Integer.parseInt(String.valueOf(between_days)));
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的日期
     * @param bdate  较大的日期
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Math.abs(Integer.parseInt(String.valueOf(between_days)));
    }

    /**
     * 从字符串中截取指定长度的字符串
     *
     * @param str
     * @param len
     * @return 从字符串中截取指定长度的字符串
     */
    public static String subString(String str, int len) {
        if (isNull(str))
            return "";
        if (str.length() <= len)
            return str;
        return str.substring(0, len);
    }

    /**
     * 生成MD5字符串
     *
     * @param s
     * @return 生成MD5字符串
     */
    public static String md5StringFor(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(s.getBytes());
            StringBuilder builder = new StringBuilder();
            for (byte b : hash) {
                builder.append(Integer.toString(b & 0xFF, 16));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将计时的秒值转换成hh:mm:ss(hh:多少个小时，mm:多少分钟，ss:多少秒)
     *
     * @param time
     * @return 将计时的秒值转换成hh:mm:ss(hh:多少个小时，mm:多少分钟，ss:多少秒)
     */
    public static String formatTime(int time) {
        int sec = time % 60;
        int min = time / 60 % 60;
        int hou = time / 60 / 60 % 60;
        return (hou >= 10 ? hou : "0" + hou) + ":"
                + (min >= 10 ? min : "0" + min) + ":"
                + (sec >= 10 ? sec : "0" + sec);
    }

    /**
     * 获取不同级别的现在时间的Long值
     *
     * @param level 1:秒级、2:分钟级、3:小时级、4:天级、5:月级、6:年级
     * @return
     */
    public static long getCurrentTime(int level) {
        long currentTime = System.currentTimeMillis();
        String fmt = "";
        switch (level) {
            case 1:
                fmt = "yyyy-MM-dd HH:mm:ss";
                break;
            case 2:
                fmt = "yyyy-MM-dd HH:mm";
                break;
            case 3:
                fmt = "yyyy-MM-dd HH";
                break;
            case 4:
                fmt = "yyyy-MM-dd";
                break;
            case 5:
                fmt = "yyyy-MM";
                break;
            case 6:
                fmt = "yyyy";
                break;
        }
        String newDate = formatDateTime(currentTime, fmt);
        long newTime = parseTime(newDate, fmt).getTime();
        return newTime / 1000;
    }

    /**
     * 获取UUID
     *
     * @param length
     * @return UUID
     */
    public static String generateId(int length) {
        String uuid = UUID.randomUUID().toString();
        return PadLeading(uuid, length, "");
    }

    /**
     * 输出当前时间
     */
    public static void consolCurrentTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINA);// 格式化时间
        Date curDate = new Date(System.currentTimeMillis());
        Log.v("当前时间", df.format(curDate));
    }

    /**
     * 创建文件夹
     *
     * @param folderPath
     */
    public static void mkDirs(String folderPath) {

        String[] strs = folderPath.split("/");
        int len = strs.length;
        String strPath = AppUtils.getSDPath() + "/";
        File path;
        for (int i = 0; i < len; i++) {
            if (strs[i] != null && !"".equalsIgnoreCase(strs[i])) {
                strPath += strs[i] + "/";
                path = new File(strPath);
                if (!path.exists()) {
                    try {
                        path.mkdirs();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    /**
     * @param rString
     * @param rLength
     * @param rPad
     * @return
     */
    public static String PadLeading(String rString, int rLength, String rPad) {
        String lTmpPad = "";
        String lTmpStr = rString;

        if (!isNull(lTmpStr)) {
            if (lTmpStr.length() >= rLength)
                return lTmpStr.substring(0, rLength);
            else {
                for (int gCnt = 1; gCnt <= rLength - lTmpStr.length(); gCnt++) {
                    lTmpPad = lTmpPad + rPad;
                }
            }

        }
        return lTmpPad + lTmpStr;
    }

    /**
     * 获取关键字加粗变色后的字符串
     *
     * @param res   源字符串
     * @param key   关键字
     * @param color 关键字显示颜色
     * @return 关键字加粗变色后的字符串
     */
    public static SpannableStringBuilder getSpanString(String res, String key,
                                                       int color) {
        int start = res.toLowerCase(Locale.US).indexOf(key.toLowerCase(Locale.US));
        int end = start + key.length();
        int length = res.length();

        SpannableStringBuilder style = new SpannableStringBuilder(res);
        if (start < 0 || end < 0 || start > length || end > length) {
            return style;
        }

        style.setSpan(new ForegroundColorSpan(color), start, end,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return style;
    }

    /**
     * 获取关键字加粗变色后的字符串
     *
     * @param res   源字符串
     * @param key   关键字
     * @param color 关键字显示颜色
     * @return 关键字加粗变色后的字符串
     */
    public static SpannableStringBuilder getBoldSpanString(String res,
                                                           String key, int color) {
        int start = res.toLowerCase(Locale.US).indexOf(key.toLowerCase(Locale.US));
        int end = start + key.length();
        int length = res.length();

        if (start < 0 || end < 0 || start > length || end > length) {
            return null;
        }

        SpannableStringBuilder style = new SpannableStringBuilder(res);
        style.setSpan(new ForegroundColorSpan(color), start, end,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(new StyleSpan(Typeface.BOLD), start, end,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return style;
    }

}
