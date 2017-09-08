package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.dto.UserSuggestDto;
import com.lld360.cnc.model.UserSuggest;
import com.lld360.cnc.repository.UserSuggestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class UserSuggestsService extends BaseService {

    @Autowired
    UserSuggestDao userSuggestDao;

    public Page<UserSuggestDto> getSuggestDtoPage(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        List<UserSuggestDto> list = userSuggestDao.getUserSuggestPage(params);
        long count = userSuggestDao.count(params);
        return new PageImpl<>(list, pageable, count);
    }

    public UserSuggestDto getSuggestDetail(long id){
        return userSuggestDao.getUserSuggestById(id);
    }

    public void update(UserSuggest userSuggest){
        userSuggestDao.update(userSuggest);
    }

    public void create(UserSuggest userSuggest){
        userSuggest.setCreateTime(Calendar.getInstance(Locale.CHINA).getTime());
        userSuggestDao.create(userSuggest);
    }

    public void delete(long id){userSuggestDao.delete(id);}

}
