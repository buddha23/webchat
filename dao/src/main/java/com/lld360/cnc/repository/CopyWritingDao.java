package com.lld360.cnc.repository;

import com.lld360.cnc.model.CopyWriting;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CopyWritingDao {

    void create(CopyWriting copyWriting);

    void update(CopyWriting copyWriting);

    List<CopyWriting> search(Map<String, Object> params);

    long count(Map<String, Object> parameters);

    CopyWriting findById(long id);

    CopyWriting findByKeyword(String keyword);

    void delete(long id);

    int deleteSome(long[] ids);
}
