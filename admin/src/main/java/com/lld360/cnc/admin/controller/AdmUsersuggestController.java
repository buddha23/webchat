package com.lld360.cnc.admin.controller;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.annotation.OperateRecord;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.dto.UserSuggestDto;
import com.lld360.cnc.model.UserSuggest;
import com.lld360.cnc.service.UserSuggestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

//用户反馈
@RestController
@RequestMapping("admin/suggest")
public class AdmUsersuggestController extends AdmController {

    @Autowired
    UserSuggestsService userSuggestsService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<UserSuggestDto> getSuggestPage() {
        Map<String, Object> params = getParamsPageMap();
        return userSuggestsService.getSuggestDtoPage(params);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserSuggestDto suggestDetail(@PathVariable long id) {
        return userSuggestsService.getSuggestDetail(id);
    }

    @OperateRecord("修改用户反馈")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void update(@RequestBody UserSuggestDto userSuggestDto, @PathVariable long id) {
        UserSuggest userSuggest = new UserSuggest();
        userSuggest.setId(id);
        userSuggest.setReply(userSuggestDto.getReply());
        userSuggest.setReplyTime(new Timestamp(Calendar.getInstance(Locale.CHINA).getTimeInMillis()));
        userSuggestsService.update(userSuggest);
    }

    @OperateRecord("删除用户反馈")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResultOut articleDel(@PathVariable long id) {
        userSuggestsService.delete(id);
        return getResultOut(M.I10200.getCode());
    }

    @OperateRecord("批量删除用户反馈")
    @RequestMapping(value = "deleteSome", method = RequestMethod.POST)
    public ResultOut deleteSome(@RequestBody long[] ids) {
        if (ids.length == 0) throw new ServerException(HttpStatus.BAD_REQUEST, "未进行选择");
        for (long id : ids)
            userSuggestsService.delete(id);
        return getResultOut(M.I10200.getCode());
    }

}
