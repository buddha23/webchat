package com.lld360.cnc.admin.controller;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.annotation.OperateRecord;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.dto.InviteCardDto;
import com.lld360.cnc.model.InviteCard;
import com.lld360.cnc.service.InviteCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("admin/inviteCard")
public class AdmInviteCardController extends AdmController {

    @Autowired
    private InviteCardService inviteCardService;

    // 获取视频集邀请卡列表
    @RequestMapping(value = "volume", method = RequestMethod.GET)
    public Page<InviteCardDto> getVodInviteCards() {
        Map<String, Object> params = getParamsPageMap(15);
        return inviteCardService.getInviteCardPage(params);
    }

    // 删除
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteInviteCard(@PathVariable Long id) {
        inviteCardService.deleteCard(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @OperateRecord("生成邀请卡")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public ResponseEntity createInviteCard(@RequestParam Byte type, @RequestParam(required = false) Long objectId, @RequestParam Integer cardNum) {
        if (type != Const.INVITE_CARD_TYPE_MEMBER && objectId == null)
            throw new ServerException(HttpStatus.BAD_REQUEST);
        if (objectId == null) objectId = 0L;
        if (cardNum > 0) {
            for (int i = 0; i < cardNum; i++) {
                InviteCard inviteCard = new InviteCard(type, objectId);
                inviteCardService.createCard(inviteCard);
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @OperateRecord("批量删除邀请卡")
    @RequestMapping(value = "deleteSome", method = RequestMethod.POST)
    public ResultOut deleteSome(@RequestBody long[] ids) {
        for (long id : ids) inviteCardService.deleteCard(id);
        return getResultOut(M.I10200.getCode());
    }

}
