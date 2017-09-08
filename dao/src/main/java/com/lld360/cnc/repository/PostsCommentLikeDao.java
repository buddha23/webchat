package com.lld360.cnc.repository;

import com.lld360.cnc.model.PostsCommentLike;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsCommentLikeDao {

    void create(PostsCommentLike postsCommentLike);

    void delete(PostsCommentLike postsCommentLike);

    boolean isExist(PostsCommentLike postsCommentLike);

}
