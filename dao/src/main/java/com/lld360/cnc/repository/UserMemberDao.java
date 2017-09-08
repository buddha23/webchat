package com.lld360.cnc.repository;

import com.lld360.cnc.dto.UserDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.lld360.cnc.model.UserMember;

import java.util.List;
import java.util.Map;

@Repository
public interface UserMemberDao {
	
	void create(UserMember userMember);

	List<UserMember> searchByUserId(Long userId);
	
	UserMember get(@Param("userId")Long userId,@Param("type") byte type);

	void update(UserMember member);

	List<UserDto> searchMembers(Map<String, Object> parameters);

	long membersCount(Map<String, Object> parameters);
}
