package com.lld360.cnc.repository;

import com.lld360.cnc.model.UserMessage;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * UserMessage 数据库操作
 */
@Repository
public interface UserMessageDao {

    List<UserMessage> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    UserMessage find(Long id);

    long countNoReadByUser(long userId);

    void create(UserMessage usermessage);

    int update(UserMessage usermessage);

    int updateRead(long id);

    int updateReadByUser(long userId);

    void delete(Long id);
}