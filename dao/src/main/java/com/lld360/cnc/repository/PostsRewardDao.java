package com.lld360.cnc.repository;

import com.lld360.cnc.model.PostsReward;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsRewardDao {

    void create(PostsReward postsReward);

    void update(PostsReward postsReward);
}
