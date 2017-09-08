package com.lld360.cnc.website.controller.m;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lld360.alipay.AlipayConfig;
import com.lld360.alipay.bean.AlipayTradeBuilder;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.WeiXinPayConfiger;
import com.lld360.cnc.core.utils.ClientUtils;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.model.UserStatement;
import com.lld360.cnc.service.UserStatementService;
import com.lld360.cnc.service.WeixinPayService;
import com.lld360.cnc.util.FinanceUtil;
import com.lld360.cnc.website.SiteController;
import com.lld360.cnc.website.dto.WCPayRequest;

/**
 * Created by clb on 2016-10-19.
 */
@Controller
@RequestMapping("m/finance")
public class MFinanceController extends SiteController {

    private static final Logger logger = LoggerFactory.getLogger(MFinanceController.class);
    @Autowired
    private UserStatementService userStatementService;

    @Autowired
    private AlipayTradeBuilder alipayTradeBuilder;

    @Autowired
    private AlipayConfig alipayConfig;

    @Autowired
    private WeixinPayService weixinPayService;

    @Autowired
    private WeiXinPayConfiger weiXinPayConfiger;

    @ResponseBody
    @RequestMapping(value = "alipay/recharge", method = RequestMethod.POST)
    public void alipayRecharge(@RequestParam BigDecimal amount, HttpServletResponse response, byte tradeType) {
        try {
            if (!FinanceUtil.checkAmount(amount)) {
                throw new ServerException(HttpStatus.BAD_REQUEST, M.E20002);
            }
            UserDto user = getRequiredCurrentUser();
            String form = buildAliPayWapRequest(user.getId(), amount, tradeType);
            response.setContentType("text/html;charset=" + AlipayConfig.INPUT_CHARSET);
            response.getWriter().write(form);
            response.getWriter().flush();
        } catch (IOException e) {
            logger.warn("支付宝-wap-生成订单失败", e);
            throw new ServerException(HttpStatus.BAD_REQUEST, M.S90500).setMessage("支付宝-wap-生成订单失败: " + e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "weixin/recharge", method = RequestMethod.POST)
    public WCPayRequest weixinRecharge(@RequestParam BigDecimal amount, @RequestParam byte tradeType) {
        if (!FinanceUtil.checkAmount(amount)) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.S90500).setMessage("充值金额必须为大于10的整数");
        }
        UserDto user = getRequiredCurrentUser();
        String openId = getSessionStringValue("WXGZ_openId");
        UserStatement userStatement = FinanceUtil.buildUserStatementForWeixin(user.getId(), amount, configer.getRechargeExchangeRate(), tradeType);
        userStatementService.saveTempData(userStatement);
        String prepayId = weixinPayService.unifiedorder(amount, userStatement.getInnerTradeNo(), ClientUtils.getIp(request), openId);
        WCPayRequest payRequest = makeUpWcPayRequest(prepayId, userStatement.getInnerTradeNo());
        logger.info("微信-wap-生成内部订单：" + userStatement.toString());
        return payRequest;
    }

    @ResponseBody
    @RequestMapping(value = "weixin/notify", method = RequestMethod.POST)
    public void wxNotifyPost(HttpServletRequest request) {
        try {
            String body = ClientUtils.getRequestBody(request);
            String returnCode = weixinPayService.getNodeInfo(body, "return_code");
            if ("SUCCESS".equals(returnCode)) {
                String innerNo = weixinPayService.getNodeInfo(body, "out_trade_no");
                String totalFee = weixinPayService.getNodeInfo(body, "total_fee");
                String transactionId = weixinPayService.getNodeInfo(body, "transaction_id");
                userStatementService.save(transactionId, innerNo, new BigDecimal(totalFee).divide(new BigDecimal(100)));
            }
        } catch (IOException | JDOMException e) {
            logger.error("MFinanceController.wxNotifyGet getRequestBody error!");
            throw new ServerException(HttpStatus.BAD_REQUEST, M.S90400);
        }
    }

    @RequestMapping(value = "alipay/createOrder", method = RequestMethod.POST)
    public String alipayCreateOrder(@RequestParam BigDecimal amount, @RequestParam byte tradeType, RedirectAttributes attributes) {
        if (!FinanceUtil.checkAmount(amount)) {
            attributes.addFlashAttribute("errMsg", "充值金额必须为大于10的整数");
            return "redirect:/m/user/scoreRecharge";
        }
        UserDto user = getRequiredCurrentUser();
        UserStatement userStatement = FinanceUtil.buildUserStatement(user.getId(), amount, configer.getRechargeExchangeRate(), tradeType);

        userStatementService.saveTempData(userStatement);
        logger.info("支付宝-wap-生成内部订单：" + userStatement.toString());
        attributes.addAttribute("innerTradeNo", userStatement.getInnerTradeNo());
        return "redirect:/m/finance/alipay/gotoPayOrder";
    }

    @RequestMapping(value = "alipay/gotoPayOrder", method = RequestMethod.GET)
    public String alipayCreateOrder(@RequestParam String innerTradeNo) {
        if (StringUtils.isEmpty(innerTradeNo)) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.S90400);
        }
        return "m/user-score-done";
    }

    @ResponseBody
    @RequestMapping(value = "alipay/payOrder", method = RequestMethod.GET)
    public void alipayPayOrder(HttpServletRequest request, HttpServletResponse response) {
        String innerTradeNo = request.getParameter("innerTradeNo");
        try {
            if (StringUtils.isEmpty(innerTradeNo)) {
                throw new ServerException(HttpStatus.BAD_REQUEST, M.S90400);
            }
            UserStatement userStatement = userStatementService.getTempData(innerTradeNo);
            if (null == userStatement) {
                throw new ServerException(HttpStatus.NOT_FOUND, M.S90404);
            }
            String form = buildAliPayWapRequest(userStatement);
            response.setContentType("text/html;charset=" + AlipayConfig.INPUT_CHARSET);
            response.getWriter().write(form);
            response.getWriter().flush();
        } catch (IOException e) {
            logger.warn("支付宝-wap-生成订单失败", e);
            throw new ServerException(HttpStatus.BAD_REQUEST, M.S90500).setMessage("支付宝-wap-生成订单失败: " + e.getMessage());
        }
    }

    @RequestMapping(value = "alipay/notify", method = RequestMethod.POST)
    public void notifyUrl(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = alipayTradeBuilder.getAlipayNotifyParams(request);
        StringBuilder result = new StringBuilder();

        String out_trade_no = params.get(AlipayTradeBuilder.KEY_OUT_TRADE_NO);
        String trade_no = params.get(AlipayTradeBuilder.KEY_TRADE_NO);
        String trade_status = params.get(AlipayTradeBuilder.KEY_TRADE_STATUS);
        String amount = params.get(AlipayTradeBuilder.KEY_TOTAL_AMOUNT);
        boolean verify = alipayTradeBuilder.checkData(result, params, out_trade_no, trade_no, trade_status, alipayConfig.getAlipayWapPublicKey());
        String message = "fail";
        if (verify && alipayTradeBuilder.isTradeFinish(trade_status)
                && userStatementService.save(trade_no, out_trade_no, new BigDecimal(amount))) {
            message = "success";
        }
        result.append(",\t处理结果=").append(message);
        logger.info("支付宝-wap-异步通知：" + result.toString());

        outputMessage(response, message);

        if ("fail".equals(message)) {
            String param = "out_trade_no:" + out_trade_no + ",trade_no:" + trade_no + ",trade_status:" + trade_status + ",amount:" + amount;
            throw new ServerException(HttpStatus.BAD_REQUEST, M.S90500).setMessage("支付宝-wap-失败: 校验失败" + param);
        }
    }

    private String buildAliPayWapRequest(long userId, BigDecimal amount, byte tradeType) {
        UserStatement userStatement = FinanceUtil.buildUserStatement(userId, amount, configer.getRechargeExchangeRate(), tradeType);
        return buildAliPayWapRequest(userStatement);
    }

    private String buildAliPayWapRequest(UserStatement userStatement) {
        String orderDetail = FinanceUtil.buildOrderDetail(userStatement);
        String form = alipayTradeBuilder.buildTradeByWap(userStatement.getTradeRemark(), orderDetail, userStatement.getInnerTradeNo(), userStatement.getTradeAmount());
        if (StringUtils.isEmpty(form)) return null;
        logger.info("支付宝-wap-生成订单：" + form);
        return form;
    }

    private WCPayRequest makeUpWcPayRequest(String prepayId, String innerNo) {
        WCPayRequest request = new WCPayRequest();
        SortedMap<Object, Object> parameters = new TreeMap<>();
        request.setAppId(weiXinPayConfiger.getAppId());
        parameters.put("appId", weiXinPayConfiger.getAppId());
        String nonceStr = RandomStringUtils.randomAlphanumeric(18);
        request.setNonceStr(nonceStr);
        parameters.put("nonceStr", nonceStr);
        request.setSignType("MD5");
        parameters.put("signType", "MD5");
        request.setWxPackage("prepay_id=" + prepayId);
        parameters.put("package", "prepay_id=" + prepayId);
        long timeStamp = System.currentTimeMillis() / 1000;
        request.setTimeStamp(timeStamp);
        parameters.put("timeStamp", timeStamp);
        String sign = weixinPayService.createSign(parameters);
        request.setPaySign(sign);
        request.setInnerNo(innerNo);
        return request;
    }


}
