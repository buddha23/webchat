package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.model.SearchWords;
import com.lld360.cnc.repository.SearchWordsDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchWordsService extends BaseService {
    @Autowired
    public SearchWordsDao searchWordsDao;

    public Page<SearchWords> getSearchingWords(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        long count = searchWordsDao.count(params);
        List<SearchWords> searches = count > 0 ? searchWordsDao.search(params) : new ArrayList<>();
        return new PageImpl<>(searches, pageable, count);
    }

    // 获取热门搜索
    public List<String> getHotWords(int count) {
        return searchWordsDao.findHotWords(count);
    }

    public void create(String word) {
        SearchWords search = new SearchWords();
        search.setName(word);
        search.setCounts(1);
        searchWordsDao.create(search);
    }

    public void updateOrCreate(String word) {
        if (StringUtils.isEmpty(word)) {
            return;
        }
        //暂定最多统计10位
        if (word.length() > 10) {
            word = word.substring(0, 10);
        }
        searchWordsDao.createOrAddCounts(word);
    }


    public boolean doBatchClearZero(Integer[] idsStrIntegerArray) {
        return searchWordsDao.doBatchClearZero(idsStrIntegerArray) > 0;
    }

    public boolean doOneClearZero(Integer id) {
        return searchWordsDao.doOneClearZero(id) > 0;
    }

}
