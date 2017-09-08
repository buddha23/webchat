package com.lld360.cnc.repository;

import com.lld360.cnc.dto.ModeratorDto;
import com.lld360.cnc.model.Moderator;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DocModeratorDao {

    List<Moderator> findAllModerators();

    List<ModeratorDto> findModeratorsByCategory(int id);

    //版主操作
    List<ModeratorDto> findDocCategoriesByModerator(long userId);

    ModeratorDto findModerator(@Param("category") int categoryId, @Param("user") long userId);

    boolean isCategoryModerator(@Param("category") int categoryId, @Param("user") long userId);

    boolean isDocModerator(@Param("docId") long docId, @Param("userId") Long userId);

    boolean isNowModerator(@Param("state") int state, @Param("userId") Long userId);

    void createModerator(Moderator moderator);

    void deleteModerator(@Param("category") int categoryId, @Param("user") long userId);

    ModeratorDto findModeratorByUserid(Long userId);

    List<ModeratorDto> findModerators(Map<String, Object> param);

    long count4Moderators(Map<String, Object> param);

    void updateModetators(ModeratorDto moderatorDto);
}
