package com.lld360.cnc.repository;

import com.lld360.cnc.dto.PostsCommentDto;
import com.lld360.cnc.model.PostsComment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * PostsComment 数据库操作
 */
@Repository
public interface PostsCommentDao {

    List<PostsCommentDto> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    PostsCommentDto find(Long id);

    void create(PostsComment postscomment);

    int update(PostsComment postscomment);

    void delete(Long id);

}