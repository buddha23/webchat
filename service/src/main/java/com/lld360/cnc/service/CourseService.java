package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.model.Course;
import com.lld360.cnc.repository.CourseDao;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class CourseService extends BaseService {

    @Autowired
    CourseDao courseDao;

    @Autowired
    SynonymService synonymService;

    public Page<Course> search(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        long count = courseDao.count(params);
        List<Course> articles = count > 0 ? courseDao.search(params) : new ArrayList<>();
        return new PageImpl<>(articles, pageable, count);
    }

    public long countTitle(Map<String, Object> params) {
        return courseDao.countTitle(params);
    }


    public Course find(Long id) {
        Course course = courseDao.find(id);
        if (course != null) {
            course.setVisits(course.getVisits() + 1);
            courseDao.update(course);
            return course;
        }
        return null;
    }

    // 分词查询相似
    public List<Course> searchByTitle(Map<String, Object> params) {
        String titleStr = "title";
        if (!params.containsKey("status"))
            params.put("status", Const.CNC_COURSE_STATUS_UP);
        if (params.containsKey(titleStr)) {
            String title = (String) params.get(titleStr);
            if (!title.matches("\\w+"))
                title = synonymService.getSynonymsRegexp(title);
            params.replace(titleStr, title);
        }
        return courseDao.searchByTitle(params);
    }

    public Course add(Course course) {
        course.setVisits(0L);
        course.setPublishTime(new Date());
        course.setStatus(Const.CNC_COURSE_STATUS_UP);
        courseDao.create(course);
        return course;
    }

    public Course update(Course course) {
        courseDao.update(course);
        return course;
    }

    @Transactional
    public void delete(Long id) {
        if (courseDao.find(id) != null)
            courseDao.delete(id);
        try {
            FileUtils.deleteDirectory(new File(configer.getFileBasePath() + "course/" + id));
        } catch (IOException e) {
            logger.warn("删除Course文件夹失败", e);
        }
    }

    // 上传图片
    public Course setImage(Course course, MultipartFile file) {
        String relativePath = "course/" + course.getId() + "/";
        String relativeFile = relativePath + UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        String absoluteFile = configer.getFileBasePath() + relativeFile;
        if (course.getImgUrl() != null) {
            File oldAvatarFile = new File(configer.getFileBasePath() + course.getImgUrl());
            if (oldAvatarFile.exists())
                FileUtils.deleteQuietly(oldAvatarFile);
        }
        try {
            File f = new File(absoluteFile);
            if (f.getParentFile().exists() || f.getParentFile().mkdirs())
                file.transferTo(f);
            courseDao.updateImage(course.getId(), relativeFile);
            course.setImgUrl(relativeFile);
            return course;
        } catch (IOException e) {
            logger.warn("保存教程图片失败");
            throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, M.E90003).setData(absoluteFile);
        }
    }
}
