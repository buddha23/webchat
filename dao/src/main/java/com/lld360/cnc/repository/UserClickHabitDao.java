package com.lld360.cnc.repository;

import com.lld360.cnc.model.UserClickHabit;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface UserClickHabitDao {

    void create(UserClickHabit userClickHabit);

    Long getDocActive(Map<String, Object> param);

    Long getPostsActive(Map<String, Object> param);

    Long getSoftActive(Map<String, Object> param);

    Long getVideoActive(Map<String, Object> param);

    Long getCourseActive(Map<String, Object> param);
}
