package com.lld360.cnc.repository;

import com.lld360.cnc.model.Course;
import com.lld360.cnc.model.Doc;
import com.lld360.cnc.model.DocCategory;
import com.lld360.cnc.model.Posts;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by clb on 2016/11/23.
 */
@Repository
public interface SiteMapDao {

    List<Doc> findDoc();

    List<DocCategory> findDocCategory();

    List<Course> findCourse();

    List<Posts> findPosts();
}
