package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lld360.cnc.repository.UserMemberDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserMemberService extends BaseService{

    @Autowired
    private UserMemberDao userMemberDao;

    @Autowired
    private UserDao userDao;

    public Boolean isMember(Long userId) {
        return userMemberDao.searchByUserId(userId).size() > 0;
    }

    public Page<UserDto> getMembersByPage(Map<String, Object> params){
        Pageable pageable = getPageable(params);
        long count = userMemberDao.membersCount(params);
        List<UserDto> users = count > 0 ? userMemberDao.searchMembers(params) : new ArrayList<>();
        return new PageImpl<>(users, pageable, count);
    }

}
