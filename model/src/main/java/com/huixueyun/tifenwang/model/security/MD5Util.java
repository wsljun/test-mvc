package com.huixueyun.tifenwang.model.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class MD5Util {
    /**
     * @param txt
     * @return
     */
    public static String md5(String txt) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(txt.getBytes("GBK")); // 问题主要出在这里，Java的字符串是unicode编码，不受源码文件的编码影响；而PHP的编码是和源码文件的编码一致，受源码编码影响。
            StringBuffer buf = new StringBuffer();
            for (byte b : md.digest()) {
                buf.append(String.format("%02x", b & 0xff));
            }
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param paramArrayOfByte
     * @param toUpperCase
     * @return
     */
    public static String toMd5(byte[] paramArrayOfByte, boolean toUpperCase) {
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.reset();
            localMessageDigest.update(paramArrayOfByte);
            return toHexString(localMessageDigest.digest(), "", toUpperCase);
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
            throw new RuntimeException(localNoSuchAlgorithmException);
        }
    }

    /**
     * @param paramArrayOfByte
     * @param paramString
     * @param toUpperCase
     * @return
     */
    public static String toHexString(byte[] paramArrayOfByte, String paramString, boolean toUpperCase) {
        StringBuilder localStringBuilder = new StringBuilder();
        for (int k : paramArrayOfByte) {
            String str = Integer.toHexString(0xFF & k);
            if (toUpperCase)
                str = str.toUpperCase(Locale.US);
            if (str.length() == 1)
                localStringBuilder.append("0");
            localStringBuilder.append(str).append(paramString);
        }
        return localStringBuilder.toString();
    }
}
