package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.model.DocTag;
import com.lld360.cnc.repository.DocTagDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class DocTagService extends BaseService {

    @Autowired
    private DocTagDao docTagDao;

    public Page<DocTag> search(Map<String, Object> parameters) {
        parameters.put("state", Const.DOC_STATE_NORMAL);
        Pageable pageable = getPageable(parameters);
        long count = docTagDao.count(parameters);
        List<DocTag> docTagList = docTagDao.search(parameters);
        return new PageImpl<>(docTagList, pageable, count);
    }

    public long count(Map<String, Object> parameters) {
        return docTagDao.count(parameters);
    }

    public DocTag get(Integer id) {
        return docTagDao.findById(id);
    }

    public void add(DocTag docTag) {
        docTagDao.create(docTag);
    }

    public boolean update(DocTag docTag) {
        return docTagDao.update(docTag) > 0;
    }

    public boolean delete(Integer id) {
        DocTag docTag = new DocTag();
        docTag.setId(id);
        return docTagDao.delete(docTag);
    }
}
