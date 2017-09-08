package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.dto.InviteCardDto;
import com.lld360.cnc.model.*;
import com.lld360.cnc.repository.*;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class InviteCardService extends BaseService {

    @Autowired
    private InviteCardDao inviteCardDao;

    @Autowired
    private VodBuysDao vodBuysDao;

    @Autowired
    private DocDao docDao;

    @Autowired
    private DocDownloadDao docDownloadDao;

    @Autowired
    private SoftDownloadDao softDownloadDao;

    @Autowired
    private UserMemberDao userMemberDao;

    public Page<InviteCardDto> getInviteCardPage(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        long count = inviteCardDao.count(params);
        List<InviteCardDto> list = inviteCardDao.inviteCardList(params);
        return new PageImpl<>(list, pageable, count);
    }

    public void deleteCard(Long inviteCardId) {
        inviteCardDao.delete(inviteCardId);
    }

    public void createCard(InviteCard inviteCard) {
        if (inviteCard.getType() == null || (!inviteCard.getType().equals(Const.INVITE_CARD_TYPE_MEMBER) && inviteCard.getObjectId() == null))
            throw new ServerException(HttpStatus.BAD_REQUEST);
        inviteCard.setState(Const.INVITE_CARD_STATE_NEW);
        inviteCard.setInviteCode(genInviteCode(inviteCard.getType(), inviteCard.getObjectId()));
        inviteCardDao.create(inviteCard);
    }

    // 生成邀请码
    private String genInviteCode(Byte type, Long id) {
        Long now = Calendar.getInstance(Locale.CHINA).getTimeInMillis();
        String seed = "abcdefghijkmnpqrstuvwxyz23456789";
        StringBuilder str = new StringBuilder();
        Stack<Character> s = new Stack<>();
        int len = seed.length();
        do {
            s.push(seed.charAt((int) (now % len)));
            now /= len;
        } while (now != 0);
        while (!s.isEmpty()) {
            str.append(s.pop());
        }
        switch (type) {
            case Const.INVITE_CARD_TYPE_VOD:
                str.append("vod");
                break;
            case Const.INVITE_CARD_TYPE_DOC:
                str.append("doc");
                break;
            case Const.INVITE_CARD_TYPE_SOFT:
                str.append("soft");
                break;
            case Const.INVITE_CARD_TYPE_MEMBER:
                str.append("mem");
                break;
            default:
                throw new ServerException(HttpStatus.BAD_REQUEST);
        }
        str.append(id);
        return str.toString();
    }

    public InviteCardDto getInviteCardByCode(String code) {
        return inviteCardDao.getByCode(code);
    }

    @Transactional
    public void activiteCard(InviteCard inviteCard, User user) {
        if (inviteCard.getState() != Const.INVITE_CARD_STATE_NEW)
            throw new ServerException(HttpStatus.BAD_REQUEST, "无效的邀请卡");
        String remark = "使用邀请卡";
        switch (inviteCard.getType()) {
            case Const.INVITE_CARD_TYPE_VOD:
                if (!vodBuysDao.hasBuy(user.getId(), inviteCard.getObjectId())) {
                    VodBuys vodBuys = new VodBuys(inviteCard.getObjectId().intValue(), user.getId(), 0, remark, Const.VOD_BUY_TYPE_INVITECARD);
                    vodBuysDao.create(vodBuys);
                }
                break;
            case Const.INVITE_CARD_TYPE_DOC:
                if (!docDownloadDao.hasDownload(user.getId(), inviteCard.getObjectId())) {
                    DocDownload docDownload = new DocDownload(user.getId(), inviteCard.getObjectId(), 0);
                    docDownload.setComment(remark);
                    docDownloadDao.create(docDownload);
                }
                break;
            case Const.INVITE_CARD_TYPE_SOFT:
                if (!softDownloadDao.hasDownload(user.getId(), inviteCard.getObjectId())) {
                    SoftDownload softDownload = new SoftDownload(user.getId(), inviteCard.getObjectId(), 0);
                    softDownload.setComment(remark);
                    softDownloadDao.create(softDownload);
                }
                break;
            case Const.INVITE_CARD_TYPE_MEMBER:
                UserMember member = userMemberDao.get(user.getId(), (byte) 1);
                Date now = Calendar.getInstance(Locale.CHINA).getTime();
                if (null == member) {
                    member = new UserMember();
                    member.setStartTime(now);
                    member.setUpdateTime(now);
                    member.setUserId(user.getId());
                    member.setState((byte) 1);
                    member.setType((byte) 1);
                    member.setEndTime(DateUtils.addYears(now, 1));
                    userMemberDao.create(member);
                } else {
                    member.setUpdateTime(now);
                    member.setEndTime(member.getEndTime().before(now) ? DateUtils.addYears(now, 1) : DateUtils.addYears(member.getEndTime(), 1));
                    userMemberDao.update(member);
                }
                break;
            default:
                throw new ServerException(HttpStatus.BAD_REQUEST);
        }
        inviteCard.setState(Const.INVITE_CARD_STATE_USED);
        inviteCardDao.update(inviteCard);
    }

}
