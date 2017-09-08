package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.dto.UserLoginHistoryDto;
import com.lld360.cnc.model.UserLoginHistory;
import com.lld360.cnc.repository.UserLoginHistoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserLoginHistoryService extends BaseService {

    @Autowired
    UserLoginHistoryDao userLoginHistoryDao;

    public void create(UserLoginHistory userLoginHistory) {
        userLoginHistoryDao.create(userLoginHistory);
    }

    public Page<UserLoginHistoryDto> searchUserLoginReport(Map<String, Object> params) {
        checkTime(params);
        Pageable pageable = getPageable(params);
        List<UserLoginHistoryDto> list = userLoginHistoryDao.searchUserLoginHistory(params);
        long count = userLoginHistoryDao.searchCount4LoginHistory();
        return new PageImpl<>(list, pageable, count);
    }

}
