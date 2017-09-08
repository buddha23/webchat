package com.lld360.cnc.repository;

import com.lld360.cnc.model.UserPoint;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserPointDao {

    List<UserPoint> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    UserPoint find(long userId);

    void create(UserPoint userPoint);

    void update(UserPoint userPoint);

    void updatePoint(@Param("userId") Long userId, @Param("point") Integer point);


}
