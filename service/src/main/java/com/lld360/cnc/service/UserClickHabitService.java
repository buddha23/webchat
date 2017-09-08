package com.lld360.cnc.service;

import com.lld360.cnc.core.utils.ClientUtils;
import com.lld360.cnc.model.User;
import com.lld360.cnc.model.UserClickHabit;
import com.lld360.cnc.repository.UserClickHabitDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserClickHabitService {

    @Autowired
    private UserClickHabitDao userClickHabitDao;

    public void create(UserClickHabit userClickHabit) {
        userClickHabitDao.create(userClickHabit);
    }

    public void createHabit(HttpServletRequest request) {
        Object user = request.getSession().getAttribute("user");
        if (user != null && !ClientUtils.isAjax(request)) {
            UserClickHabit userClickHabit = new UserClickHabit(((User) user).getId(), request.getRequestURI(), request.getMethod(), ClientUtils.getIp(request));
            create(userClickHabit);
        }
    }
}
