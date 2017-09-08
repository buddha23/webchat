package com.lld360.cnc.admin.controller;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.annotation.OperateRecord;
import com.lld360.cnc.model.*;
import com.lld360.cnc.service.UserMessageService;
import com.lld360.cnc.service.UserService;
import com.lld360.cnc.service.WithdrawalsService;
import com.lld360.cnc.service.WxTempMsgService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequiresPermissions("withdraw")
@RequestMapping("admin/withdraw")
public class AdminUserWithdrawController extends AdmController {

    @Autowired
    private WithdrawalsService withdrawalsService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private WxTempMsgService wxTempMsgService;

    @RequestMapping("getWithdrawList")
    public Page<UserWithdraw> getWithdrawList() {
        Map<String, Object> params = getParamsPageMap();
        return withdrawalsService.getUserWithdrawPage(params);
    }

    @OperateRecord("转账申请审核不通过")
    @RequestMapping("/{id}/reject")
    public ResponseEntity<String> reject(@PathVariable long id, String remark) {
        UserWithdraw userWithdraw = withdrawalsService.getUserWithdraw(id);
        if(userWithdraw.getTradeStatus() != UserWithdraw.TradeStatus.EXAMINE.getState())
            throw new ServerException(HttpStatus.NOT_ACCEPTABLE,"该申请不在审核中状态,不能执行操作");
        userWithdraw.setFailReason(remark);
        userWithdraw.setDealer(getCurrentAdmin().getId());
        userWithdraw.setAccountId(userWithdraw.getUserAccount().getId());
        userWithdraw.setTradeStatus(UserWithdraw.TradeStatus.AUDITFAIL.getState());
        userWithdraw.setUpdateTime(new Date());
        withdrawalsService.reject(userWithdraw);
        userMessageService.add(new UserMessage(userWithdraw.getUserId(), UserMessage.TYPE_WITHDRAW_REVIEW_DENY, id, "提现申请", "转账申请审核未通过"));
        ThirdAccount thirdAccount = userService.getThirdAccountByUserid(userWithdraw.getUserId(), Const.THIRD_ACCOUNT_TYPE_WEIXIN);
        if (thirdAccount != null) wxTempMsgService.withdrawWxMsg(thirdAccount,userWithdraw);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @OperateRecord("转账申请审核通过")
    @RequestMapping("doSuccess/{id}")
    public ResponseEntity<Model> withdrawPostTo(@PathVariable long id) {
        UserWithdraw userWithdraw = withdrawalsService.getUserWithdraw(id);
        if(userWithdraw.getTradeStatus() != UserWithdraw.TradeStatus.EXAMINE.getState())
            throw new ServerException(HttpStatus.NOT_ACCEPTABLE,"该申请不在审核中状态,不能执行操作");
        userWithdraw.setAccountId(userWithdraw.getUserAccount().getId());
        userWithdraw.setDealer(getCurrentAdmin().getId());
        userWithdraw.setUpdateTime(new Date());
        withdrawalsService.withdrawPostTo(userWithdraw);
        // 消息.模板消息
        userMessageService.add(new UserMessage(userWithdraw.getUserId(), UserMessage.TYPE_WITHDRAW_REVIEW_OK, id, "提现申请", "转账申请审核通过"));
        ThirdAccount thirdAccount = userService.getThirdAccountByUserid(userWithdraw.getUserId(), Const.THIRD_ACCOUNT_TYPE_WEIXIN);
        if (thirdAccount != null) wxTempMsgService.withdrawWxMsg(thirdAccount,userWithdraw);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
