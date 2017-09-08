package com.lld360.cnc.repository;

import com.lld360.cnc.model.UserWithdraw;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Map;

/**
 * UserWithdraw 数据库操作
 */
@Repository
public interface UserWithdrawDao{
	
    List<UserWithdraw> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    UserWithdraw find(Long id);

    void create(UserWithdraw userwithdraw);

    int update(UserWithdraw userwithdraw);

    void delete(Long id);
    
    UserWithdraw findByInnerTradeNo(String innerTradeNo);

}