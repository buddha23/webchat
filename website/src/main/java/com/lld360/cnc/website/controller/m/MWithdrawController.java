package com.lld360.cnc.website.controller.m;

import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.model.UserAccount;
import com.lld360.cnc.service.*;
import com.lld360.cnc.website.SiteController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("m/withdraw")
public class MWithdrawController extends SiteController {

    @Autowired
    private WithdrawalsService withdrawalsService;

    @Autowired
    private ValidSmsService validSmsService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserStatementService userStatementService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "board", method = RequestMethod.GET)
    public String boardGet(Model model) {
        UserDto user = getRequiredCurrentUser();
        model.addAttribute("userScore", userService.findUserScore(user.getId()));
        return "m/withdraw-board";
    }

    // 账户列表
    @RequestMapping(value = "accounts", method = RequestMethod.GET)
    public String accountsGet(Model model) {
        UserDto user = getRequiredCurrentUser();
        model.addAttribute("accounts", userStatementService.getAccountList(user));
        return "m/withdraw-accounts";
    }

    // 添加账户
    @RequestMapping(value = "accountAdd", method = RequestMethod.GET)
    public String accountAddGet(Model model, @RequestParam(required = false) Long accountId) {
        UserDto user = getRequiredCurrentUser();
        model.addAttribute("user", user);
        if (accountId != null) {
            UserAccount userAccount = userAccountService.findAccountById(accountId);
            if (userAccount != null && userAccount.getUserId().equals(user.getId()))
                model.addAttribute("account", userAccount);
            else throw new ServerException(HttpStatus.BAD_REQUEST, "非法操作");
        }
        return "m/withdraw-account-add";
    }

    // 提现申请
    @RequestMapping(value = "apply", method = RequestMethod.GET)
    public String applyGet(Model model) {
        UserDto user = getRequiredCurrentUser();
        if (StringUtils.isEmpty(user.getMobile()))
            throw new ServerException(HttpStatus.BAD_REQUEST, "用户账户未绑定手机号，请先绑定！");
        UserAccount aliAccount = userAccountService.findAliAccount(user);
        if (aliAccount == null) return "redirect:/m/withdraw/accountAdd";
        model.addAttribute("user", user);
        model.addAttribute("aliAccount", aliAccount);
        model.addAttribute("userScore", userService.findUserScore(user.getId()));
        return "m/withdraw-apply";
    }

    // 提现申请记录
    @RequestMapping(value = "history", method = RequestMethod.GET)
    public String withdrawHistory(Model model) {
        UserDto userDto = getRequiredCurrentUser();
        Map<String, Object> params = getParamsMap();
        params.put("userId", userDto.getId());
        model.addAttribute("histories", withdrawalsService.getUserWithdrawPage(params));
        return "m/withdraw-history";
    }


}
