package com.lld360.cnc.repository;

import com.lld360.cnc.model.Course;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Course 数据库操作
 */
@Repository
public interface CourseDao {

    List<Course> search(Map<String, Object> parameters);

    List<Course> searchByTitle(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    long countTitle(Map<String, Object> parameters);

    Course find(Long id);

    void create(Course course);

    int update(Course course);

    void delete(Long id);

    int updateImage(@Param("id") long id, @Param("relativeFile") String relativeFile);

    long searchViews(Map<String, Object> parameters);
}