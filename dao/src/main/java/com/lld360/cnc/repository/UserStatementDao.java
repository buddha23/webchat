package com.lld360.cnc.repository;

import com.lld360.cnc.model.UserStatement;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface UserStatementDao {

    void create(UserStatement userTrade);

    int update(UserStatement userTrade);

    void delete(Long id);

    UserStatement find(Long id);

    UserStatement findByInnerTradeNo(String tradeNo);

    List<UserStatement> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    List<UserStatement> searchDetail(Map<String, Object> parameters);

    long count4Detail(Map<String, Object> parameters);

    long searchTotleMoney(Map<String, Object> parameters);

    BigDecimal dailyPayCount(Date date);

    BigDecimal monthPayCount(Date date);

}
