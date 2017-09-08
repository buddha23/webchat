package com.lld360.cnc.admin.controller;

import com.lld360.cnc.base.BaseController;
import com.lld360.cnc.model.SearchWords;
import com.lld360.cnc.service.SearchWordsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/searchword")
public class AdmSearchWordsController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(AdmSearchWordsController.class);

    @Autowired
    public SearchWordsService searchWordsService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<SearchWords> wordPageGet() {
        return searchWordsService.getSearchingWords(getParamsPageMap(15));
    }

    @RequestMapping(value = "doBatchClearZero", method = RequestMethod.GET)
    public boolean doBatchClearZero(@RequestParam String idsStr) {
        logger.info("idsStr:" + idsStr);
        if (idsStr.startsWith(",")) idsStr = idsStr.substring(1, idsStr.length());
        logger.info("idsStr2:" + idsStr);
        String[] idsStrStringArray = idsStr.split(",");
        Integer[] idsStrIntegerArray = new Integer[idsStrStringArray.length];
        for (int i = 0; i < idsStrStringArray.length; i++)
            idsStrIntegerArray[i] = Integer.valueOf(idsStrStringArray[i]);
        return searchWordsService.doBatchClearZero(idsStrIntegerArray);
    }

    @RequestMapping(value = "doOneClearZero/{id}", method = RequestMethod.GET)
    public boolean doOneClearZero(@PathVariable Integer id) {
        logger.info("id:" + id);
        return searchWordsService.doOneClearZero(id);
    }

}
