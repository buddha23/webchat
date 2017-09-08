package com.lld360.cnc.admin.controller;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.annotation.OperateRecord;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.model.Course;
import com.lld360.cnc.service.CourseService;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 精品教程
 */
@RestController
@RequiresPermissions("course")
@RequestMapping("admin/course")
public class AdmCourseController extends AdmController {

    @Autowired
    private CourseService courseService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<Course> coursePageGet() {
        Map<String, Object> params = getParamsPageMap(15);
        if (params.containsKey("key"))
            params.put("title", params.get("key"));
        return courseService.search(params);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Course articleGet(@PathVariable long id) {
        return courseService.find(id);
    }

    @OperateRecord("新增教程")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Course articlePost(@RequestBody Course course) {
        checkCourse(course);
        return courseService.add(course);
    }

    @OperateRecord("修改教程")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public Course articlePut(@RequestBody Course course) {
        checkCourse(course);
        courseService.update(course);
        return course;
    }

    private void checkCourse(Course course) {
        Map<String, Object> param = new HashMap<>();
        if (StringUtils.isNotEmpty(course.getTitle())) {
            param.put("title", course.getTitle());
        }
        if (course.getId() != null) {
            param.put("id", course.getId());
        }
        if (StringUtils.isEmpty(course.getTitle()) || courseService.countTitle(param) > 0) {
            throw new ServerException(HttpStatus.BAD_REQUEST, "标题不能为空且不能重复");
        }
        if (course.getType() != Const.CNC_COURSE_TYPE_COURSE && course.getType() != Const.CNC_COURSE_TYPE_NEWS) {
            throw new ServerException(HttpStatus.BAD_REQUEST, "类型错误");
        }
    }

    @OperateRecord("删除课程")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResultOut articleDel(@PathVariable long id) {
        courseService.delete(id);
        return getResultOut(M.I10200.getCode());
    }

    @OperateRecord("批量删除课程")
    @RequestMapping(value = "deleteSome", method = RequestMethod.POST)
    public ResultOut deleteSome(@RequestBody long[] ids) {
        for (long id : ids)
            courseService.delete(id);
        return getResultOut(M.I10200.getCode());
    }

    // 上传图片
    @OperateRecord("上传课程图片")
    @RequestMapping(value = "{id}/image", method = RequestMethod.POST)
    public Course userAvatarPost(@PathVariable long id, MultipartFile file) {
        Course course = courseService.find(id);
        checkFileType(file,Const.COURSE_NOT_GEN_PIC_TYPES);
        if (course != null) {
            return courseService.setImage(course, file);
        }
        throw new ServerException(HttpStatus.NOT_FOUND, M.S90404);
    }
}
