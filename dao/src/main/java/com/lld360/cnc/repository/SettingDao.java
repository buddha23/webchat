package com.lld360.cnc.repository;

import com.lld360.cnc.model.Setting;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettingDao {

    void insert(Setting setting);

    Setting find(String code);

    List<Setting>  search();

    int update(Setting setting);

    void delete(String name);
}