package com.lld360.cnc.repository;

import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.model.ReportData;
import com.lld360.cnc.model.User;
import com.lld360.cnc.model.UserInvite;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserDao {

    List<UserDto> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    User find(Long id);

    UserDto findByMobile(String mobile);

    boolean isModerator(long id);

    void create(UserDto user);

    int updateById(User user);

    int updateByMobile(User user);

    int desertUser(User user);

    int updateState(@Param("userId") long userId, @Param("state") byte state);

    int updateAvatar(@Param("id") long id, @Param("relativeFile") String relativeFile);

    UserDto findUserDto(long id);

    List<ReportData> searchRegistReport(Map<String, Object> params);

    List<Integer> searchRegistNum();

    // userInviteRelation 用户邀请关系
    UserInvite findInviteByCode(String inviteCode);

    UserInvite findInviteByUserId(Long userId);

    void createUserInvite(UserInvite userInvite);

    void updateUserInvite(UserInvite userInvite);

    List<UserDto> parterList(Map<String, Object> parameters);

    long parterCount(Map<String, Object> parameters);

    long getInviteNum(Long userId);

    long getAreaNum(Long userId);

    UserDto findByWxfwOpenId(String openId);

}