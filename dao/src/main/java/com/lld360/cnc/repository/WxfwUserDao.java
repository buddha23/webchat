package com.lld360.cnc.repository;

import com.lld360.cnc.model.WxfwUser;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface WxfwUserDao {

    void create(WxfwUser wxfwUser);

    void update(WxfwUser wxfwUser);

    List<WxfwUser> search(Map<String,Object> params);

    long count(Map<String,Object> params);

    WxfwUser getLastUser();

    void updateByOpenid(WxfwUser wxfwUser);

    List<WxfwUser> searchNoUnionUser();

}
