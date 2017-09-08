package com.lld360.cnc.website.quartz;

import com.lld360.cnc.service.SearchWordsService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * Author: dhc
 * Date: 2016-09-05 15:11
 */
public class ContextQuartz {

    @Autowired
    private ServletContext context;

    @Autowired
    private SearchWordsService searchWordsService;

    // 初始化完成需要执行的方法
    public void doInitTask() {
        updateHotSearchWords();
    }

    // 更新热搜词
    public void updateHotSearchWords() {
        List<String> hotWords = searchWordsService.getHotWords(5);
        context.setAttribute("hotSearchWords", hotWords);
    }
}
