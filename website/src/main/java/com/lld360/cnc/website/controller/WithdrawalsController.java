package com.lld360.cnc.website.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.model.UserAccount;
import com.lld360.cnc.model.UserWithdraw;
import com.lld360.cnc.service.UserAccountService;
import com.lld360.cnc.service.UserService;
import com.lld360.cnc.service.ValidSmsService;
import com.lld360.cnc.service.WithdrawalsService;
import com.lld360.cnc.util.FinanceUtil;
import com.lld360.cnc.website.SiteController;
import com.lld360.cnc.website.dto.WithdrawalsRequest;

@Controller
@RequestMapping("withdrawals")
public class WithdrawalsController extends SiteController {

    @Autowired
    private WithdrawalsService withdrawalsService;

    @Autowired
    private ValidSmsService validSmsService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(Model model) {
        Map<String, Object> params = getParamsPageMap();
        params.put("userId", getRequiredCurrentUser().getId());
        model.addAttribute("list", withdrawalsService.getUserWithdrawPage(params));
        return "withdraw/list";
    }

    @RequestMapping(value = "apply", method = RequestMethod.POST)
    public String postApply(WithdrawalsRequest request) {
        String error = volidateRequest(request);
        if (null != error) {
            throw new ServerException(HttpStatus.BAD_REQUEST, error);
        }
        UserWithdraw userWithdraw = parseToUserWithdraw(request);
        withdrawCutPlatCharge(userWithdraw);
        withdrawalsService.putNewUserWithdraw(userWithdraw);
        return "redirect:list";
    }

    private UserWithdraw parseToUserWithdraw(WithdrawalsRequest request) {
        UserWithdraw userWithdraw = new UserWithdraw();
        userWithdraw.setUserId(getRequiredCurrentUser().getId());
        userWithdraw.setAccountId(request.getAccountId());
        userWithdraw.setTradeAmount(new BigDecimal(request.getTradeAmount()));
        userWithdraw.setTradeScore(Integer.parseInt(request.getTradeAmount()) * 10);
        Date now = new Date();
        userWithdraw.setInnerTradeNo(FinanceUtil.buildTradeNo(getRequiredCurrentUser().getId(), now));
        userWithdraw.setCreateTime(now);
        userWithdraw.setUpdateTime(now);
        userWithdraw.setTradeStatus(UserWithdraw.TradeStatus.EXAMINE.getState());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String remark = "于" + sdf.format(new Date()) + "提出转账申请，处于待审核状态";
        userWithdraw.setTradeRemark(remark);
        return userWithdraw;
    }

    private String volidateRequest(WithdrawalsRequest request) {
        StringBuffer buffer = null;

        if (null == request.getAccountId() || StringUtils.isEmpty(request.getMobile()) || StringUtils.isEmpty(request.getPassword()) || StringUtils.isEmpty(request.getTradeAmount())) {
            buffer = buffer == null ? new StringBuffer() : buffer;
            buffer.append("必填参数不能为空，");
        }
        if (Integer.parseInt(request.getTradeAmount()) < 50 || (Integer.parseInt(request.getTradeAmount()) * 10) > userService.findUserScore(getRequiredCurrentUser().getId()).getTotalScore()) {
            buffer = buffer == null ? new StringBuffer() : buffer;
            buffer.append("提现金额不能小于50或者大于可提金额，");
        }
        if (!getRequiredCurrentUser().getPassword().equals(DigestUtils.md5Hex(StringUtils.reverse(request.getPassword()) + getRequiredCurrentUser().getMobile()))) {
            buffer = buffer == null ? new StringBuffer() : buffer;
            buffer.append("密码错误，");
        }
        try {
            validSmsService.validSmsCode(request.getMobile(), Const.SMS_WITHDRAWALS, request.getValidCode());
        } catch (ServerException e) {
            buffer = buffer == null ? new StringBuffer() : buffer;
            buffer.append("验证码错误，");
        }
        boolean flag = false;
        List<UserAccount> list = userAccountService.findAvaliableByUserId(getRequiredCurrentUser().getId());
        for (UserAccount account : list) {
            if (account.getId().equals(request.getAccountId())) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            buffer = buffer == null ? new StringBuffer() : buffer;
            buffer.append("提现账户不存在，");
        }
        return buffer == null ? null : buffer.deleteCharAt(buffer.length() - 1).append('!').toString();
    }

    /*
    * 平台收取费用
    * */
    private void withdrawCutPlatCharge(UserWithdraw withdraw) {
        BigDecimal realAmount = withdraw.getTradeAmount().multiply(Const.WITHDRAW_REAL_AMOUNT);
        withdraw.setTradeAmount(realAmount);
    }

}
