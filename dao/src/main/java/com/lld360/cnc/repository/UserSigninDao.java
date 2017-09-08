package com.lld360.cnc.repository;

import com.lld360.cnc.model.UserSignin;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserSigninDao {

     List<UserSignin> findByParam(Map<String, Object> param);

     void create(UserSignin userSignin);

}
