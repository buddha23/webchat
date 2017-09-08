package com.lld360.cnc.admin.quartz;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.lld360.alipay.AlipayConfig;
import com.lld360.cnc.core.enums.PayType;
import com.lld360.cnc.model.UserStatement;
import com.lld360.cnc.service.UserStatementService;
import com.lld360.cnc.service.WeixinPayService;

public class UserStatementRefreshQuartz {

    @Autowired
    private UserStatementService userStatementService;

    @Autowired
    private AlipayConfig configer;

    @Autowired
    private WeixinPayService weixinPayService;

    public void refresh() throws AlipayApiException, JDOMException, IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("payType", PayType.AliPay.getValue());
        params.put("limit", 50);
        params.put("offset", 0);
        List<UserStatement> alipayList = userStatementService.getTempUserStatement(params);
        if (null != alipayList && !alipayList.isEmpty()) {
            for (UserStatement temp : alipayList) {
                AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.WAP_GATEWAY, configer.getAlipayWapAppId(), configer.getAlipayWapPrivateKey(), AlipayConfig.WAP_FROM, AlipayConfig.INPUT_CHARSET, configer.getAlipayWapPublicKey());
                AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
                request.setBizContent("{" +
                        "\"out_trade_no\":\"" + temp.getInnerTradeNo() + "\"" +
                        "}");
                AlipayTradeQueryResponse response = alipayClient.execute(request);
                if (response.isSuccess() && response.getTradeStatus().equals("TRADE_SUCCESS")) {
                    userStatementService.save(response.getTradeNo(), temp.getInnerTradeNo(), new BigDecimal(response.getTotalAmount()));
                }

            }
        }
        params.put("payType", PayType.WeiXin.getValue());
        List<UserStatement> weixinList = userStatementService.getTempUserStatement(params);
        if (null != weixinList && !weixinList.isEmpty()) {
            for (UserStatement temp : weixinList) {
                String result = weixinPayService.orderQuery(temp.getInnerTradeNo());
                String returnCode = weixinPayService.getNodeInfo(result, "return_code");
                if ("SUCCESS".equals(returnCode) && null != weixinPayService.getNodeInfo(result, "transaction_id")) {
                    String totalFee = weixinPayService.getNodeInfo(result, "total_fee");
                    String transactionId = weixinPayService.getNodeInfo(result, "transaction_id");
                    userStatementService.save(transactionId, temp.getInnerTradeNo(), new BigDecimal(totalFee).divide(new BigDecimal(100)));
                }
            }
        }
    }
}
