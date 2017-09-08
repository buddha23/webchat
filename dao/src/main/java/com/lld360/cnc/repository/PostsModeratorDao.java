package com.lld360.cnc.repository;

import com.lld360.cnc.dto.ModeratorDto;
import com.lld360.cnc.model.Moderator;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsModeratorDao {

    void create(Moderator moderator);

    void delete(@Param("category") int categoryId, @Param("user") long userId);

    void update(Moderator moderator);

    List<ModeratorDto> findModeratorsByCategory(int id);

    ModeratorDto findModerator(@Param("category") int categoryId, @Param("user") long userId);

}
