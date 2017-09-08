package com.lld360.cnc.core.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * 加密工具类
 */
public class CryptUtils {
    private static SecureRandom sRandom = new SecureRandom();
    public static final Random random = new Random();

    /**
     * 生成随机盐值
     *
     * @param size 盐值长度
     * @return 盐值字符串
     */
    public static String generateSalt(int size) {
        size = (size + 1) / 2;
        byte[] bytes = new byte[size];
        sRandom.nextBytes(bytes);
        return Hex.encodeHexString(bytes);
    }

    public static String SHA1(String decript, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] result;
            if (StringUtils.isNotEmpty(salt)) {
                digest.update(Hex.decodeHex(salt.toCharArray()));
                result = digest.digest(decript.getBytes());
            } else {
                digest.update(decript.getBytes());
                result = digest.digest();
            }
            return Hex.encodeHexString(result);
        } catch (NoSuchAlgorithmException | DecoderException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String MD5(String input) {
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(input.getBytes());
            // 获得密文
            byte[] md = mdInst.digest();

            return Hex.encodeHexString(md);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    // 生成指定长度的数字，长度必须在2-9位
    public static int genNumber(int size) {
        if (size < 2) {
            size = 2;
        }
        if (size > 9) {
            size = 9;
        }
        double base = Math.pow(10, size - 1);
        double upper = Math.pow(10, size);
        int bound = (int) (upper - base - 1);
        return (int) (base + random.nextInt(bound));
    }

    /**
     * 重置密码，修改密码专用
     * James
     *
     * @param args
     */
    public static void main(String args[]) {
        String expectedPSWD = "111111";//提现密码只能六位数字
        String currentDBSalt = "123456";
        String newPWD = DigestUtils.md5Hex(StringUtils.reverse(expectedPSWD) + currentDBSalt);
        System.out.println(newPWD);
    }
}
