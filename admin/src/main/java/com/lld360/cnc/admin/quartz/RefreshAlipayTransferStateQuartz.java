package com.lld360.cnc.admin.quartz;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.lld360.cnc.model.UserWithdraw;
import com.lld360.cnc.repository.UserWithdrawDao;
import com.lld360.cnc.service.UserService;
import com.lld360.cnc.service.WithdrawalsService;

/*
* 转账提现任务
* 接口说明: https://doc.open.alipay.com/docs/api.htm?docType=4&apiId=1322
*/
public class RefreshAlipayTransferStateQuartz {

    private static final Logger logger = LoggerFactory.getLogger(RefreshAlipayTransferStateQuartz.class);

    @Autowired
    private WithdrawalsService withdrawalsService;

    @Autowired
    private UserWithdrawDao userWithdrawDao;

    @Autowired
    private UserService userService;

    public void refresh() throws AlipayApiException {
        Map<String, Object> params = new HashMap<>();
        params.put("tradeStatus", UserWithdraw.TradeStatus.HANDLE.getState());
        List<UserWithdraw> list = userWithdrawDao.search(params);
        if (null != list && !list.isEmpty()) {
            for (UserWithdraw userWithdraw : list) {
                AlipayFundTransOrderQueryResponse res = withdrawalsService.orderQuery(userWithdraw.getInnerTradeNo());
                if (res.getCode().equals("10000")) {
                    if ("SUCCESS".equals(res.getStatus())) {
                        userWithdraw.setTradeNo(res.getOrderId());
                        userWithdraw.setTradeStatus(UserWithdraw.TradeStatus.SUCCESS.getState());
                        StringBuffer buffer = new StringBuffer(userWithdraw.getTradeRemark());
                        int money = userWithdraw.getTradeAmount().intValue();
                        int score = money * 10;
                        buffer.append("\n");
                        buffer.append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + ":");
                        buffer.append("转账成功,实际转账" + money + "元，");
                        buffer.append("扣除牛人币" + score + "。");
                        userWithdraw.setTradeRemark(buffer.toString());
                        userWithdraw.setSuccessTime(new Date());
                        userWithdrawDao.update(userWithdraw);
                        //userService.withdrawalsReduceScore(userWithdraw.getUserId(), score);
                    } else if ("FAIL".equals(res.getStatus())) {
                        userWithdraw.setTradeStatus(UserWithdraw.TradeStatus.FAIL.getState());
                        StringBuffer buffer = new StringBuffer(userWithdraw.getTradeRemark());
                        buffer.append("\n");
                        buffer.append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + ":");
                        buffer.append("转账失败，失败原因：");
                        buffer.append(res.getFailReason());
                        userWithdraw.setTradeRemark(buffer.toString());
                        userWithdraw.setFailReason(res.getFailReason());
                        userWithdrawDao.update(userWithdraw);
                        withdrawalsService.returnWithdrawScore(userWithdraw);
                        logger.info("转账处理订单号" + userWithdraw.getInnerTradeNo() + ":" + res.getStatus() + "," + res.getFailReason());
                    }
                }

            }
        }

        params.put("tradeStatus", UserWithdraw.TradeStatus.SERUNAVAILABLE.getState());
        List<UserWithdraw> unRequestList = userWithdrawDao.search(params);
        if (null != unRequestList && !unRequestList.isEmpty()) {
            for (UserWithdraw userWithdraw : unRequestList) {
                AlipayFundTransOrderQueryResponse res = withdrawalsService.orderQuery(userWithdraw.getInnerTradeNo());
                if (res.getCode().equals("10000")) {
                    if ("SUCCESS".equals(res.getStatus())) {
                        userWithdraw.setTradeNo(res.getOrderId());
                        userWithdraw.setTradeStatus(UserWithdraw.TradeStatus.SUCCESS.getState());
                        BigDecimal money = new BigDecimal(res.getOrderFee());
                        int score = money.intValue() * 10;
                        StringBuffer buffer = new StringBuffer(userWithdraw.getTradeRemark());
                        buffer.append("\n");
                        buffer.append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + ":");
                        buffer.append("转账成功,实际转账" + money + "元，");
                        buffer.append("扣除牛人币" + score + "。");
                        userWithdraw.setTradeRemark(buffer.toString());
                        userWithdrawDao.update(userWithdraw);
                        //userService.withdrawalsReduceScore(userWithdraw.getUserId(), score);
                    } else if ("FAIL".equals(res.getStatus())) {
                        userWithdraw.setTradeStatus(UserWithdraw.TradeStatus.FAIL.getState());
                        StringBuffer buffer = new StringBuffer(userWithdraw.getTradeRemark());
                        buffer.append("\n");
                        buffer.append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + ":");
                        buffer.append("转账失败，牛人币没有扣除，失败原因：");
                        buffer.append(res.getFailReason());
                        userWithdraw.setTradeRemark(buffer.toString());
                        userWithdraw.setFailReason(res.getFailReason());
                        userWithdrawDao.update(userWithdraw);
                        withdrawalsService.returnWithdrawScore(userWithdraw);
                        logger.info("转账处理订单号" + userWithdraw.getInnerTradeNo() + ":" + res.getStatus() + "," + res.getFailReason());
                    } else if ("INIT".equals(res.getStatus()) || "REFUND".equals(res.getStatus())) {
                        userWithdraw.setTradeStatus(UserWithdraw.TradeStatus.HANDLE.getState());
                        userWithdrawDao.update(userWithdraw);
                    }
                }
                /*//重新向支付宝发送转账请求
                else{
					withdrawalsService.transfer("大牛数控","大牛数控提现资金",userWithdraw);
					
				}*/
            }
        }


    }
}
