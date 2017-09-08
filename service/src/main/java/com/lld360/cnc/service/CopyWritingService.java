package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.model.CopyWriting;
import com.lld360.cnc.repository.CopyWritingDao;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CopyWritingService extends BaseService {

    @Autowired
    CopyWritingDao copyWritingDao;

    public CopyWriting createCopyWring(CopyWriting copyWriting) {
        copyWriting.setPublishTime(Calendar.getInstance(Locale.CHINA).getTime());
        if (copyWritingDao.findByKeyword(copyWriting.getKeyword()) != null)
            throw new ServerException(HttpStatus.FORBIDDEN, "key值已存在");
        copyWritingDao.create(copyWriting);
        return copyWriting;
    }

    public CopyWriting update(CopyWriting copyWriting) {
        copyWriting.setPublishTime(Calendar.getInstance(Locale.CHINA).getTime());
        copyWritingDao.update(copyWriting);
        return copyWriting;
    }

    public Page<CopyWriting> search(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        long count = copyWritingDao.count(params);
        List<CopyWriting> list = count > 0 ? copyWritingDao.search(params) : new ArrayList<>();
        return new PageImpl<>(list, pageable, count);
    }

    public CopyWriting findById(Long id) {
        return copyWritingDao.findById(id);
    }

    public void delete(long id) {
        copyWritingDao.delete(id);
    }

    public void deleteCopyWritings(long[] ids) {
        copyWritingDao.deleteSome(ids);
    }

    public CopyWriting getCopyWriteByKey(String key) {
        return copyWritingDao.findByKeyword(key);
    }

}
