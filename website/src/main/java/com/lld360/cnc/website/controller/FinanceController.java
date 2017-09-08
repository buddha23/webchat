package com.lld360.cnc.website.controller;

import com.lld360.alipay.AlipayConfig;
import com.lld360.alipay.bean.AlipayTradeBuilder;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.utils.BankCardUtil;
import com.lld360.cnc.core.utils.ClientUtils;
import com.lld360.cnc.core.utils.HttpUtil;
import com.lld360.cnc.core.utils.JsonUtils;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.model.User;
import com.lld360.cnc.model.UserAccount;
import com.lld360.cnc.model.UserStatement;
import com.lld360.cnc.service.UserStatementService;
import com.lld360.cnc.service.ValidSmsService;
import com.lld360.cnc.service.WeixinPayService;
import com.lld360.cnc.util.FinanceUtil;
import com.lld360.cnc.website.SiteController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("finance")
public class FinanceController extends SiteController {

    @Autowired
    private UserStatementService userStatementService;

    @Autowired
    private AlipayTradeBuilder alipayTradeBuilder;

    @Autowired
    private AlipayConfig alipayConfig;

    @Autowired
    private ValidSmsService validSmsService;
    @Autowired
    private WeixinPayService weixinPayService;

    @ResponseBody
    @RequestMapping(value = "alipay/recharge", method = RequestMethod.POST)
    public ResultOut alipayRecharge(@RequestParam BigDecimal amount, byte tradeType) {
        if (!FinanceUtil.checkAmount(amount)) {
            return new ResultOut(M.E20002, "充值金额必须为大于10的整数");
        } else {
            UserDto user = getRequiredCurrentUser();
            String alipayRequestBody = buildAliPayRequest(user.getId(), amount,tradeType);
            if (StringUtils.isEmpty(alipayRequestBody)) {
                return new ResultOut(M.S90500, "生成订单错误，请稍后再试！");
            } else {
                return new ResultOut().setData(alipayRequestBody);
            }
        }
    }
    

    @RequestMapping(value = "alipay/notify", method = RequestMethod.POST)
    public void notifyUrl(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = alipayTradeBuilder.getAlipayNotifyParams(request);
        StringBuilder result = new StringBuilder();

        String out_trade_no = params.get(AlipayTradeBuilder.KEY_OUT_TRADE_NO);
        String trade_no = params.get(AlipayTradeBuilder.KEY_TRADE_NO);
        String trade_status = params.get(AlipayTradeBuilder.KEY_TRADE_STATUS);
        String amount = params.get(AlipayTradeBuilder.KEY_TOTAL_FEE);

        boolean verify = alipayTradeBuilder.checkData(result, params, out_trade_no, trade_no, trade_status, alipayConfig.getAlipayPublicKey());

        String message = "fail";
        if (verify && alipayTradeBuilder.isTradeFinish(trade_status)
                && userStatementService.save(trade_no, out_trade_no, new BigDecimal(amount))) {
            message = "success";
        }
        result.append(",\t处理结果=").append(message);
        logger.info("支付宝-异步通知：" + result.toString());

        outputMessage(response, message);

        if ("fail".equals(message)) {
            String param = "out_trade_no:" + out_trade_no + ",trade_no:" + trade_no + ",trade_status:" + trade_status + ",amount:" + amount;
            throw new ServerException(HttpStatus.BAD_REQUEST, M.S90500).setMessage("支付宝-PC-失败: 校验失败" + param);
        }
    }

    @RequestMapping("accountList")
    public String accountList(Model model) {
        User user = getRequiredCurrentUser();
        List<UserAccount> accountlist = userStatementService.getAccountList(user);
        model.addAttribute("list", accountlist);
        return "withdraw/withdraw_accountList";
    }

    @RequestMapping(value = "accountIsExist", method = RequestMethod.GET)
    public ResponseEntity<Model> accountIsExist(Model model) {
        User user = getRequiredCurrentUser();
        if (user.getMobile() != null) {
            model.addAttribute("isBind", true);
            model.addAttribute("mobile", user.getMobile());
        } else {
            model.addAttribute("isBind", false);
        }
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @RequestMapping(value = "addAccount", method = RequestMethod.POST)
    public ResponseEntity AddAccount(UserAccount userAccount, String code) {
        User user = getRequiredCurrentUser();
        userAccount.setUserId(user.getId());
        Model model = new ExtendedModelMap();
        try {
            validSmsService.validSmsCode(user.getMobile(), Const.SMS_WITHDRAWALS, code);
        } catch (ServerException e) {
            model.addAttribute("msg", getMessage(String.valueOf(e.getCode())));
            return new ResponseEntity<>(model, HttpStatus.BAD_REQUEST);
        }
        if (userAccount.getId() != null && !userAccount.getId().equals("")) {
            userStatementService.updateUserAccount(userAccount);
            model.addAttribute("msg", "修改成功");
        } else {
            userAccount.setStatus(Const.NORMAL);
            if (userStatementService.isExist(userAccount)) {
                model.addAttribute("msg", "用户您好，支付宝提现仅支持一个账户");
                return new ResponseEntity<>(model, HttpStatus.BAD_REQUEST);
            }
            userStatementService.addAccount(userAccount);
            model.addAttribute("msg", "添加成功");
        }
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @RequestMapping("deleteAccount")
    public ResponseEntity<Model> deleteAccount(Model model, long id) {
        userStatementService.deleteAccount(id);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @RequestMapping("editAccount/{id}")
    public ResponseEntity<Model> editAccount(Model model, @PathVariable long id) {
        UserAccount userAccount = userStatementService.getAccount(id);
        model.addAttribute("mobile", getRequiredCurrentUser().getMobile());
        model.addAttribute("userAccount", userAccount);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @RequestMapping("checkBankNo")
    public ResponseEntity<Model> checkBankNo(Model model, String bankNo) {
        if (bankNo == null) {
            model.addAttribute("msg", "请输入卡号");
            return new ResponseEntity<>(model, HttpStatus.BAD_REQUEST);
        }
        if (bankNo.length() < 16 || bankNo.length() > 19) {
            model.addAttribute("msg", "卡号应为16-19位数字");
            return new ResponseEntity<>(model, HttpStatus.BAD_REQUEST);
        }
        if (!BankCardUtil.checkBankCard(bankNo)) {
            model.addAttribute("msg", "卡号校验失败");
            return new ResponseEntity<>(model, HttpStatus.BAD_REQUEST);
        }
        if (!bankNo.startsWith("62")) {
            model.addAttribute("msg", "国外的卡，或者旧银行卡，暂时没有收录");
            return new ResponseEntity<>(model, HttpStatus.BAD_REQUEST);
        }
        if (!BankCardUtil.getNameOfBank(bankNo.substring(0, 6), 0).contains("没有记录的卡号")) {
            model.addAttribute("bankName", BankCardUtil.getNameOfBank(bankNo.substring(0, 6), 0));
        } else {
            String result = HttpUtil.doGet(bankNo);
            Map map = JsonUtils.toMap(result);
            model.addAttribute("bankName", map.get("bank"));
        }
        return new ResponseEntity<>(model, HttpStatus.OK);
    }
    
    @RequestMapping("weixin/payByQr")
    public String payByQrCode(@RequestParam BigDecimal amount,@RequestParam byte tradeType,Model model){
    	if (amount==null || amount.intValue()<=0){
    		throw new ServerException(HttpStatus.BAD_REQUEST, "充值金额不能为空");
    	}
    	if (Const.PAY_SCORE != tradeType && Const.PAY_VIP != tradeType){
    		throw new ServerException(HttpStatus.BAD_REQUEST);
    	}
    	UserDto user = getRequiredCurrentUser();
    	UserStatement userStatement = FinanceUtil.buildUserStatementForWeixin(user.getId(), amount, configer.getRechargeExchangeRate(),tradeType);
    	String codeUrl = weixinPayService.unifiedorderByQr(userStatement.getTradeAmount(), ClientUtils.getIp(request), userStatement.getInnerTradeNo());
    	userStatementService.saveTempData(userStatement);
    	model.addAttribute("codeUrl", codeUrl);
    	model.addAttribute("tradeType",tradeType);
    	model.addAttribute("amount",userStatement.getTradeAmount());
    	model.addAttribute("trodeNo", userStatement.getInnerTradeNo());
    	return "wUser/user-buy-byWeixinQr";
    }
    
    @ResponseBody
    @RequestMapping(value="getPayStatus/{innerTradeNo}",method=RequestMethod.GET)
    public String getPayStatus(@PathVariable("innerTradeNo") String innerTradeNo){
    	Map<String,Object> condition = new HashMap<>();
    	condition.put("innerTradeNo", innerTradeNo);
    	List<UserStatement> list = userStatementService.getUserRecharge(condition);
    	if (list == null || list.isEmpty()){
    		return "unSuccess";
    	}
    	return "success";
    }

    private String buildAliPayRequest(long userId, BigDecimal amount ,byte tradeType) {
        UserStatement userStatement = FinanceUtil.buildUserStatement(userId, amount, configer.getRechargeExchangeRate(),tradeType);
        String orderDetail = FinanceUtil.buildOrderDetail(userStatement);
        Map<String, Object> paraTemp = alipayTradeBuilder.buildTradeByWeb(userStatement.getTradeRemark(), orderDetail, userStatement.getInnerTradeNo(), userStatement.getTradeAmount());
        if (null == paraTemp || paraTemp.size() == 0) return null;

        StringBuffer sbHtml = new StringBuffer();
        sbHtml.append(AlipayConfig.WEB_GATEWAY);
        paraTemp.forEach((key, vlaue) -> sbHtml.append(key).append("=").append(vlaue).append("&"));
        userStatement.setTradeType(tradeType);
        userStatementService.saveTempData(userStatement);
        logger.info("支付宝-生成订单：" + userStatement.toString());
        return sbHtml.toString();
    }


}
