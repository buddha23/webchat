package com.lld360.cnc.service;

import com.lld360.cnc.core.utils.SqlUtils;
import com.lld360.cnc.repository.SynonymDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: dhc
 * Date: 2016-08-29 10:30
 */
@Service
public class SynonymService {
    @Autowired
    private SynonymDao synonymDao;

    /**
     * 根据用户输入提取关键字并查找所有同义词
     *
     * @param words 用户输入词语
     * @return 可以用于MySQL正在查询的词语字符串
     */
    public String getSynonymsRegexp(String words) {
        words = words.replaceAll("[~!@#$%^&*()_+-={}\\[\\]\\\\:\";'<>,.?/\\s]", "");
        if(StringUtils.isEmpty(words)) {
            return ".";
        }
        String regexpWords = SqlUtils.getRegexpKeyword(words);
        List<String> datas = synonymDao.findSynonyms(regexpWords);
        if (datas.isEmpty()) {
            return regexpWords;
        }
        String allWords = String.join(",", datas).replaceAll("\\\\", "\\\\\\").replaceAll("\\.", "\\\\\\.").replaceAll("\\|", "\\\\\\|");
        List<String> wordList = Arrays.stream(allWords.split(",")).distinct().collect(Collectors.toList());
        return String.join("|", wordList);
    }
}
