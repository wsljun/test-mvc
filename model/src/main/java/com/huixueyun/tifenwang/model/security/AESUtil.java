package com.huixueyun.tifenwang.model.security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class AESUtil {
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM_NAME = "AES";

    /**
     * @param paramString1
     * @param paramString2
     * @param paramArrayOfByte
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(String paramString1, String paramString2, byte[] paramArrayOfByte) throws Exception {
        SecretKeySpec localSecretKeySpec = new SecretKeySpec(paramString2.getBytes(), ALGORITHM_NAME);
        Cipher localCipher = Cipher.getInstance(TRANSFORMATION);
        IvParameterSpec localIvParameterSpec = new IvParameterSpec(paramString1.getBytes());
        localCipher.init(1, localSecretKeySpec, localIvParameterSpec);
        byte[] arrayOfByte = localCipher.doFinal(paramArrayOfByte);
        return arrayOfByte;
    }

    /**
     * @param paramString1
     * @param paramString2
     * @param paramArrayOfByte
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(String paramString1, String paramString2, byte[] paramArrayOfByte) throws Exception {
        SecretKeySpec localSecretKeySpec = new SecretKeySpec(paramString2.getBytes(), ALGORITHM_NAME);
        Cipher localCipher = Cipher.getInstance(TRANSFORMATION);
        IvParameterSpec localIvParameterSpec = new IvParameterSpec(paramString1.getBytes());
        localCipher.init(2, localSecretKeySpec, localIvParameterSpec);
        byte[] arrayOfByte = localCipher.doFinal(paramArrayOfByte);
        return arrayOfByte;
    }
}