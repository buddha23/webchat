package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.model.UserMessage;
import com.lld360.cnc.repository.UserMessageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Author: dhc
 * Date: 2016-12-01 14:10
 */
@Service
public class UserMessageService extends BaseService {

    @Autowired
    private UserMessageDao userMessageDao;

    // 添加消息
    public void add(UserMessage message) {
        userMessageDao.create(message);
    }

    // 获取未读数量
    public long noReadCount(long userId) {
        return userMessageDao.countNoReadByUser(userId);
    }

    // 设置已读
    public int setRead(long id) {
        return userMessageDao.updateRead(id);
    }

    public UserMessage getMessageById(long id) {
        return userMessageDao.find(id);
    }

    // 删除消息
    public void delete(long id) {
        userMessageDao.delete(id);
    }

    // 设置指定用户的消息为已读
    public int setReadByUser(long userId) {
        return userMessageDao.updateReadByUser(userId);
    }

    // 分页获取用户消息
    public Page<UserMessage> search(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        long total = userMessageDao.count(params);
        List<UserMessage> messageList = userMessageDao.search(params);
        return new PageImpl<>(messageList, pageable, total);
    }
}
