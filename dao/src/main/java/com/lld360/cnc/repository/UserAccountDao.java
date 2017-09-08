package com.lld360.cnc.repository;

import com.lld360.cnc.model.UserAccount;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Map;

/**
 * UserAccount 数据库操作
 */
@Repository
public interface UserAccountDao {

    List<UserAccount> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    UserAccount find(Long id);

    void create(UserAccount useraccount);

    int update(UserAccount useraccount);

    void delete(Long id);

    boolean isExist(Map<String, Object> parameters);

}