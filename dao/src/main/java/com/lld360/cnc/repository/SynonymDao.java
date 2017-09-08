package com.lld360.cnc.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: dhc
 * Date: 2016-08-29 10:18
 */
@Repository
public interface SynonymDao {
    // 根据正则关键字查询同义词
    List<String> findSynonyms(String regexpWords);
}
