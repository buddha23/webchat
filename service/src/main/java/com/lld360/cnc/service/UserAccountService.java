package com.lld360.cnc.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lld360.cnc.model.UserAccount;
import com.lld360.cnc.repository.UserAccountDao;

@Service
public class UserAccountService {
    @Autowired
    private UserAccountDao userAccountDao;

    public List<UserAccount> findAvaliableByUserId(long userId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", userId);
        parameters.put("status", 1);
        List<UserAccount> result = userAccountDao.search(parameters);
        return result == null ? Collections.emptyList() : result;
    }

    public UserAccount findAccountById(Long id) {
        return userAccountDao.find(id);
    }

    public UserAccount findAliAccount(User user) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", user.getId());
        param.put("accountType", Const.ALIPAY);
        List<UserAccount> accounts = userAccountDao.search(param);
        return accounts.isEmpty() ? null : accounts.get(0);
    }
}
