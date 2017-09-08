package com.lld360.cnc.core.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IdCardUtils {

    private final static String BIRTH_DATE_FORMAT = "yyyyMMdd"; // 身份证号码中的出生日期的格式
    private final static Date MINIMAL_BIRTH_DATE = new Date(-2209017600000L); // 身份证的最小出生日期,1900年1月1日

    private final static char[] VERIFY_CODE = {'1', '0', 'X', '9', '8', '7',
            '6', '5', '4', '3', '2'}; // 18位身份证中最后一位校验码
    private final static int[] VERIFY_CODE_WEIGHT = {7, 9, 10, 5, 8, 4, 2, 1,
            6, 3, 7, 9, 10, 5, 8, 4, 2};// 18位身份证中，各个数字的生成校验码时的权值

    private final static int NEW_CARD_NUMBER_LENGTH = 18;
    private final static int OLD_CARD_NUMBER_LENGTH = 15;

    public static boolean checkValidate(String cardNumber) {
        if (null != cardNumber && cardNumber.length() == OLD_CARD_NUMBER_LENGTH) {
            return checkValidateBy15(cardNumber);
        } else if (null != cardNumber
                && cardNumber.length() == NEW_CARD_NUMBER_LENGTH) {
            return checkValidateBy18(cardNumber.toUpperCase());
        } else {
            return false;
        }
    }

    private static boolean checkValidateBy15(String cardNumber) {
        try {
            return isNumber(cardNumber.substring(0,OLD_CARD_NUMBER_LENGTH))
                    &&checkTime(getDataString(cardNumber))
                    &&checkAreaCode(cardNumber.substring(0, 6));
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean checkValidateBy18(String cardNumber) {
        try {
            return isNumber(cardNumber.substring(0,NEW_CARD_NUMBER_LENGTH - 1))
                    && checkLastChar(cardNumber)
                    && checkTime(getDataString(cardNumber))
                    && checkAreaCode(cardNumber.substring(0, 6));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 必须全数字
     *
     * @param cardNumber
     * @return
     * @throws IDCardException
     */
    private static boolean isNumber(String cardNumber) throws IDCardException {
        for (int i = 0; i < cardNumber.length(); i++) {
            char ch = cardNumber.charAt(i);
            if (ch < '0' || ch > '9') {
                throw new IDCardException("不是数字");
            }
        }
        return true;
    }

    /**
     * 检测18位身份证最后一位
     *
     * @param cardNumber
     * @return
     * @throws IDCardException
     */
    private static boolean checkLastChar(String cardNumber) throws IDCardException {
        if (calculateVerifyCode(cardNumber) != cardNumber.charAt(NEW_CARD_NUMBER_LENGTH - 1)) {
            throw new IDCardException("最后一位效验失败");
        }
        return true;
    }

    /**
     * 校验码（第十八位数）：<br>
     * 十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0...16 ，先对前17位数字的权求和；<br>
     * Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 <br>
     * Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2<br>
     * 计算模 Y = mod(S, 11)< 通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8
     * 7 6 5 4 3 2<br>
     *
     * @param cardNumber
     * @return
     */
    private static char calculateVerifyCode(String str) {
        int sum = 0;
        for (int i = 0; i < NEW_CARD_NUMBER_LENGTH - 1; i++) {
            char ch = str.charAt(i);
            sum += ((int) (ch - '0')) * VERIFY_CODE_WEIGHT[i];
        }
        return VERIFY_CODE[sum % 11];
    }

    /**
     * 检测时间
     *
     * @param str
     * @return
     * @throws IDCardException
     */
    private static boolean checkTime(String str) throws IDCardException {
        try {
            Date time = new Date(createBirthDateParser().parse(str).getTime());
            /**
             * 出生日期不能晚于当前时间，并且不能早于1900年
             * 出生日期中的年、月、日必须正确,比如月份范围是[1,12],日期范围是[1,31]，还需要校验闰年、大月、小月的情况时，
             * 月份和日期相符合
             */
            if (null == time || time.after(new Date())
                    || time.before(MINIMAL_BIRTH_DATE)
                    || !str.equals(createBirthDateParser().format(time))) {
                throw new IDCardException("时间验证失败");
            }
        } catch (ParseException e) {
            throw new IDCardException("时间验证失败");
        }
        return true;
    }

    /**
     * 检测 地区
     *
     * @param str
     * @return
     */
    private static boolean checkAreaCode(String str) {
        // TODO 完整身份证号码的省市县区检验规则
        return true;
    }

    private static SimpleDateFormat createBirthDateParser() {
        return new SimpleDateFormat(BIRTH_DATE_FORMAT);
    }

    /**
     * 把15位身份证号码转换到18位身份证号码<br>
     * 15位身份证号码与18位身份证号码的区别为：<br>
     * 1、15位身份证号码中，"出生年份"字段是2位，转换时需要补入"19"，表示20世纪<br>
     * 2、15位身份证无最后一位校验码。18位身份证中，校验码根据根据前17位生成 先调用 checkValidate(cardNumber) 判断
     * 是否合法
     * @param cardNumber
     * @return
     */
    public static String getNewCardNumber(String cardNumber) {
        StringBuilder buf = new StringBuilder(NEW_CARD_NUMBER_LENGTH);
        buf.append(cardNumber.substring(0, 6));
        buf.append("19");
        buf.append(cardNumber.substring(6));
        buf.append(calculateVerifyCode(buf.toString()));
        return buf.toString();

    }

    /**
     * 先调用 checkValidate(cardNumber) 判断 是否合法
     *
     * @param cardNumber
     * @return
     */
    public static boolean isWoman(String cardNumber) {
        if (null != cardNumber && cardNumber.length() == NEW_CARD_NUMBER_LENGTH) {
            return 1 != getGenderCode(cardNumber
                    .charAt(NEW_CARD_NUMBER_LENGTH - 2));
        } else if (null != cardNumber
                && cardNumber.length() == NEW_CARD_NUMBER_LENGTH) {
            return 1 != getGenderCode(cardNumber
                    .charAt(OLD_CARD_NUMBER_LENGTH - 1));
        }
        return false;
    }

    /**
     * 18位身份证的第17位，15位身份证的最后一位，奇数为男性，偶数为女性
     *
     * @return
     */
    private static int getGenderCode(char genderCode) {
        return (((int) (genderCode - '0')) & 0x1);
    }

    /**
     * 获取 生日
     *
     * @param cardNumber
     * @return
     */
    public static String getDataString(String cardNumber) {
        if (null != cardNumber && cardNumber.length() == NEW_CARD_NUMBER_LENGTH) {
            return cardNumber.substring(6, 14);
        } else if (null != cardNumber
                && cardNumber.length() == OLD_CARD_NUMBER_LENGTH) {
            return "19" + cardNumber.substring(6, 12);
        }
        return null;
    }

    private static class IDCardException extends Exception {

        public IDCardException(String msg) {
            super(msg);
        }
    }

}
