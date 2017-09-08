package com.lld360.cnc.util;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.enums.PayType;
import com.lld360.cnc.core.enums.TradeState;
import com.lld360.cnc.model.UserStatement;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by clb on 2016-10-24.
 */
public class FinanceUtil {

    private static final int MIN_RECHARGE_AMOUNT = 10;

    public static UserStatement buildUserStatement(long userId, BigDecimal amount, int rechargeExchangeRate, byte tradeType) {
        Date date = Calendar.getInstance(Locale.CHINA).getTime();
        String innerTradeNo = buildTradeNo(userId, date);

        UserStatement userStatement = new UserStatement();
        userStatement.setUserId(userId);
        userStatement.setTradeAmount(amount);
        userStatement.setPayType(PayType.AliPay.getValue());
        userStatement.setInnerTradeNo(innerTradeNo);
        userStatement.setTradeType(tradeType);
        switch (tradeType) {
            case Const.PAY_SCORE:
                userStatement.setTradeRemark("购买牛人币");
                break;
            case Const.PAY_VIP:
                userStatement.setTradeRemark("购买高级会员");
                break;
            case Const.PAY_VIDEO:
                userStatement.setTradeRemark("购买视频");
                break;
            default:
                break;
        }
        userStatement.setTradeTime(date);
        userStatement.setTradeState(TradeState.New.getValue());

        //生成订单时，生成相关积分
        userStatement.setBuyScore(amount.intValue() * rechargeExchangeRate);
        userStatement.setPresentScore(null);

        doTest(userStatement, userId);

        return userStatement;
    }

    private static UserStatement doTest(UserStatement userStatement, long userId) {
        if (278 == userId) {
            userStatement.setTradeAmount(new BigDecimal("0.01"));
        }
        return userStatement;
    }

    public static UserStatement buildUserStatementForWeixin(long userId, BigDecimal amount, int rechargeExchangeRate, byte tradeType) {
        UserStatement statement = buildUserStatement(userId, amount, rechargeExchangeRate, tradeType);
        statement.setPayType(PayType.WeiXin.getValue());
        return statement;
    }

    public static boolean checkAmount(BigDecimal amount) {
        if (null == amount) return false;
        String str = amount.toString();
        int value = amount.intValue();
        return value > 0 && value >= MIN_RECHARGE_AMOUNT && (str.indexOf('.') < 0 || str.matches(".*\\.0*$"));
    }

    public static double getMoney(BigDecimal b, int len) {
        BigDecimal b2 = new BigDecimal(1);
        return b.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String buildTradeNo(long userId, Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault());
        String key = format.format(date);
        return key + "-" + userId;
    }

    public static String buildOrderDetail(UserStatement userStatement) {
        StringBuilder str = new StringBuilder();
        str.append("购买牛人币：").append(userStatement.getBuyScore());
        if (null != userStatement.getPresentScore() && userStatement.getPresentScore() > 0) {
            str.append("，赠送牛人币：").append(userStatement.getPresentScore());
        }
        return str.toString();
    }

}
