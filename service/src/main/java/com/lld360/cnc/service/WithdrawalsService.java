package com.lld360.cnc.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.model.UserScore;
import com.lld360.cnc.model.UserScoreHistory;
import com.lld360.cnc.repository.UserScoreDao;
import com.lld360.cnc.repository.UserScoreHistoryDao;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransOrderQueryRequest;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.lld360.alipay.AlipayConfig;
import com.lld360.cnc.BaseService;
import com.lld360.cnc.model.UserWithdraw;
import com.lld360.cnc.repository.UserWithdrawDao;
import com.lld360.cnc.util.FinanceUtil;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WithdrawalsService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(WithdrawalsService.class);

    @Autowired
    private AlipayConfig alipayConfig;

    @Autowired
    private UserWithdrawDao userWithdrawDao;

    @Autowired
    private UserScoreDao userScoreDao;

    @Autowired
    private UserScoreHistoryDao userScoreHistoryDao;

    @Transactional
    public void transfer(String payName, String showName, UserWithdraw userWithdraw) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.WAP_GATEWAY, alipayConfig.getAlipayTransferpid(),
                alipayConfig.getAlipayTransferprivateKey(), AlipayConfig.WAP_FROM, AlipayConfig.INPUT_CHARSET, alipayConfig.getAlipayTransferpublicKey(), AlipayConfig.SIGN_TYPE);
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
        request.setBizContent("{" +
                "\"out_biz_no\":\"" + userWithdraw.getInnerTradeNo() + "\"," +
                "\"payee_type\":\"ALIPAY_LOGONID\"," +
                "\"payee_account\":\"" + userWithdraw.getUserAccount().getAccount() + "\"," +
                "\"amount\":\"" + userWithdraw.getTradeAmount() + "\"," +
                "\"payer_show_name\":\"" + showName + "\"," +
                "\"payee_real_name\":\"" + userWithdraw.getUserAccount().getAccountName() + "\"," +
                "\"remark\":\"" + userWithdraw.getTradeRemark() + "\"" +
                "}");
        StringBuffer buffer = new StringBuffer(userWithdraw.getTradeRemark());
        buffer.append("\n");
        buffer.append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + ":");
        AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            userWithdraw.setTradeStatus(UserWithdraw.TradeStatus.HANDLE.getState());
            buffer.append("支付请求发送成功！");
            userWithdraw.setTradeRemark(buffer.toString());
            userWithdrawDao.update(userWithdraw);
            logger.info("订单号：" + userWithdraw.getUserAccount().getAccount() + "提现转账成功！");
        } else if (response.getSubCode().equals("SYSTEM_ERROR")) {
            userWithdraw.setTradeStatus(UserWithdraw.TradeStatus.SERUNAVAILABLE.getState());
            buffer.append(response.getSubMsg());
            userWithdraw.setTradeRemark(buffer.toString());
            userWithdrawDao.update(userWithdraw);
            logger.info("订单号：" + userWithdraw.getUserAccount().getAccount() + "提现转账失败！");
        } else {
            if (response.getSubCode().equals("PAYEE_USER_INFO_ERROR")) System.out.println("测试用户账号错误");
            userWithdraw.setTradeStatus(UserWithdraw.TradeStatus.FAIL.getState());
            buffer.append("转账失败，牛人币没有扣除，失败原因：");
            buffer.append(response.getSubMsg());
            userWithdraw.setTradeRemark(buffer.toString());
            userWithdraw.setFailReason(response.getSubMsg());
            userWithdrawDao.update(userWithdraw);
            returnWithdrawScore(userWithdraw);
            throw new AlipayApiException(response.getSubCode(), response.getSubMsg());
        }
    }

    public AlipayFundTransOrderQueryResponse orderQuery(String outOrderNo) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.WAP_GATEWAY, alipayConfig.getAlipayTransferpid(),
                alipayConfig.getAlipayTransferprivateKey(), AlipayConfig.WAP_FROM, AlipayConfig.INPUT_CHARSET, alipayConfig.getAlipayTransferpublicKey(), AlipayConfig.SIGN_TYPE);
        AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
        request.setBizContent("{" +
                "\"out_biz_no\":\"" + outOrderNo + "\"" +
                "}");
        AlipayFundTransOrderQueryResponse response = alipayClient.execute(request);
        return response;
    }

    public Page<UserWithdraw> getUserWithdrawPage(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        dealParams(params);
        List<UserWithdraw> list = userWithdrawDao.search(params);
        long count = userWithdrawDao.count(params);
        return new PageImpl<>(list, pageable, count);
    }

    public UserWithdraw getUserWithdraw(long id) {
        return userWithdrawDao.find(id);
    }

    public void reject(UserWithdraw userWithdraw) {
        returnWithdrawScore(userWithdraw);
        userWithdrawDao.update(userWithdraw);
    }

    public void withdrawPostTo(UserWithdraw userWithdraw) {
        StringBuffer buffer = new StringBuffer(userWithdraw.getTradeRemark());
        Date date = Calendar.getInstance(Locale.CHINA).getTime();
        String innerTradeNo = FinanceUtil.buildTradeNo(userWithdraw.getUserId(), date);
        userWithdraw.setInnerTradeNo(innerTradeNo);
        buffer.append("\n");
        buffer.append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + ":");
        buffer.append("审核通过,向支付宝发起转账请求");
        userWithdraw.setTradeRemark(buffer.toString());
        try {
            transfer("大牛数控", "大牛数控提现资金", userWithdraw);
        } catch (AlipayApiException e) {
            throw new ServerException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    private void dealParams(Map<String, Object> params) {
        if (params.get("beginTime") != null) {
            params.put("beginTime", params.get("beginTime") + " 00:00:00");
        }
        if (params.get("endTime") != null) {
            params.put("endTime", params.get("endTime") + " 23:59:59");
        }
    }

    @Transactional
    public void putNewUserWithdraw(UserWithdraw userWithdraw) {
        userScoreDao.updateScore(userWithdraw.getUserId(), -userWithdraw.getTradeScore());
        UserScoreHistory history = new UserScoreHistory(userWithdraw.getUserId(), Const.USER_SCORE_HISTORY_TYPE_WITHDRAWALS, -userWithdraw.getTradeScore(), userWithdraw.getUserId());
        history.setDescription("用户转账提现冻结牛人币");
        userScoreHistoryDao.create(history);
        userWithdrawDao.create(userWithdraw);
    }

    @Transactional
    public void returnWithdrawScore(UserWithdraw userWithdraw) {
        userScoreDao.updateScore(userWithdraw.getUserId(), userWithdraw.getTradeScore());
        UserScoreHistory history = new UserScoreHistory(userWithdraw.getUserId(), Const.USER_SCORE_HISTORY_TYPE_WITHDRAWALS, userWithdraw.getTradeScore(), userWithdraw.getUserId());
        history.setDescription("用户提现转账失败,返还牛人币");
        userScoreHistoryDao.create(history);
    }

}
