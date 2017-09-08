package com.lld360.cnc.repository;

import com.lld360.cnc.dto.PostsCategoryDto;
import com.lld360.cnc.model.Posts;
import com.lld360.cnc.model.PostsCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsCategoryDao {

    void create(PostsCategory postsCategory);

    void update(PostsCategory postsCategory);

    List<PostsCategory> getAllCategories();

    PostsCategory getById(Integer id);

    PostsCategoryDto getDtoById(Integer id);

    void delete(Integer id);

    List<PostsCategoryDto> getAllCategories4Wap();

    List<PostsCategory> getCategoriesByModerator(Long userId);
}
