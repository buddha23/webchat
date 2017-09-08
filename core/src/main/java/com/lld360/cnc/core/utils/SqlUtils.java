package com.lld360.cnc.core.utils;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.summary.TextRankKeyword;

import java.util.List;

/**
 * Author: dhc
 * Date: 2016-08-18 15:34
 */
public class SqlUtils {
    /**
     * 将句子转为简体并提取关键字为SQL正则查询格式
     *
     * @param content 原句子
     * @return 关键字SQL正则查询格式
     */
    public static String getRegexpKeyword(String content) {
        List<String> keywordList = TextRankKeyword.getKeywordList(HanLP.convertToSimplifiedChinese(content), 2);
        if (keywordList.isEmpty()) {
            return content.replaceAll("\\\\", "\\\\\\").replaceAll("\\.", "\\\\\\.").replaceAll("\\|", "\\\\\\|");
        }
        return String.join("|", keywordList);
    }
}
