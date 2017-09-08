package com.lld360.cnc.repository;

import com.lld360.cnc.model.ValidSms;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface ValidSmsDao {

    ValidSms find(Map<String, Object> params);

    void create(ValidSms validSms);

    void update(ValidSms validSms);

    void delete(Map<String, Object> params);

}