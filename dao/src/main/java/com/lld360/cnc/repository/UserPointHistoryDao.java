package com.lld360.cnc.repository;

import com.lld360.cnc.model.UserPointHistory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserPointHistoryDao {

    List<UserPointHistory> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    UserPointHistory find(Long id);

    void create(UserPointHistory userPointHistory);

    int update(UserPointHistory userPointHistory);

    void delete(Long id);

}
