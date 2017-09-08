package com.lld360.cnc.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lld360.cnc.model.UserScoreHistory;

@Repository
public interface UserScoreHistoryDao {

    List<UserScoreHistory> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    UserScoreHistory find(Long id);

    void create(UserScoreHistory userscorehistory);

    int update(UserScoreHistory userscorehistory);

    void delete(Long id);

    Integer getAdminGive();

}