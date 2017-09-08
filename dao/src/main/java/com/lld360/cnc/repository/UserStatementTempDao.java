package com.lld360.cnc.repository;

import com.lld360.cnc.model.UserStatement;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserStatementTempDao {

    void create(UserStatement userTrade);

    void delete(Long id);

    UserStatement findByInnerTradeNo(String innerTradeNo);

    List<UserStatement> search(Map<String, Object> parameters);

    List<UserStatement> searchRows(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);
    
    void deleteOverdue();

}
