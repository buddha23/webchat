package com.lld360.cnc.repository;

import com.lld360.cnc.dto.PostsInfos;
import com.lld360.cnc.model.Posts;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Posts 数据库操作
 */
@Repository
public interface PostsDao {

    void create(Posts posts);

    int update(Posts posts);

    int updateState(@Param("id") long id, @Param("state") byte state);

    void deletePosts(long[] ids);

    /**
     * 添加浏览量
     *
     * @param id 帖子ID
     * @return 是否成功
     */
    int addViews(long id);

    void delete(Long id);

    Posts find(Long id);

    PostsInfos findPostsInfo(long id);

    long count(Map<String, Object> parameters);

    List<Posts> search(Map<String, Object> parameters);

    long countPostsInfos(Map<String, Object> parameters);

    List<PostsInfos> findPostsInfos(Map<String, Object> params);

    long searchViews(Map<String, Object> params);

    void zeroPostsByCategoryId(Integer categoryId);

    void stickPosts(Long postId);

    void changeCategory(@Param("postsId") Long postsId,@Param("categoryId") Integer categoryId);

}