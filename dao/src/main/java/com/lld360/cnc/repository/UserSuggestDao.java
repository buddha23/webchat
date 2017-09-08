package com.lld360.cnc.repository;

import com.lld360.cnc.dto.UserSuggestDto;
import com.lld360.cnc.model.UserSuggest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserSuggestDao {

    void create(UserSuggest userSuggest);

    void delete(long id);

    void update(UserSuggest userSuggest);

    List<UserSuggestDto> getUserSuggestPage(Map<String,Object> pamams);

    long count(Map<String,Object> pamams);

    UserSuggestDto getUserSuggestById(long id);

}
