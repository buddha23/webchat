package com.lld360.cnc.util;

import org.apache.commons.lang3.RandomUtils;

import java.util.Calendar;
import java.util.Date;

public class Utils {

    //生成随机验证码
    public static String generateRandomCaptcha() {
        return String.valueOf(RandomUtils.nextInt(100000, 1000000));
    }

    public static Date generateExpiredTime(int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, min);
        return calendar.getTime();
    }

}
