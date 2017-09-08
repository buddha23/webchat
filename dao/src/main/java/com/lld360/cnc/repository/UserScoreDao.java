package com.lld360.cnc.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.lld360.cnc.model.UserScore;

@Repository
public interface UserScoreDao{
	
    List<UserScore> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    UserScore find(long userId);

    void create(UserScore userscore);

    int update(UserScore userscore);

    void delete(long userId);

    void updateScore(@Param("userId") Long userId, @Param("score") Integer score);
}