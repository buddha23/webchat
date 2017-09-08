package com.lld360.cnc.website.controller;

import com.lld360.cnc.base.BaseController;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.model.Course;
import com.lld360.cnc.service.CourseService;
import com.lld360.cnc.service.UserClickHabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("course")
public class CourseController extends BaseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserClickHabitService userClickHabitService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String courseGet(Model model) {
        Map<String, Object> params = getParamsPageMap(10);
        params.put("type", Const.CNC_COURSE_TYPE_COURSE);
        model.addAttribute("courses", courseService.search(params));
        return "course";
    }

    @RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
    public String courseGet(@PathVariable long id, Model model) {
        Course course = courseService.find(id);
        if (course == null)
            throw new ServerException(HttpStatus.NOT_FOUND);
        model.addAttribute("course", course);

        Map<String, Object> params = getParamsPageMap(3);
        params.put("orderBy", "visits");
        params.put("type", Const.CNC_COURSE_TYPE_COURSE);
        model.addAttribute("hotCourse", courseService.search(params).getContent());

        params.replace("limit", 6);
        params.put("title", course.getTitle());
        List<Course> courseList = courseService.searchByTitle(params);
        if (courseList.isEmpty()) courseList = courseService.search(getParamsPageMap(6)).getContent();
        model.addAttribute("courseList", courseList);

        userClickHabitService.createHabit(request);
        return "course-detail";
    }

}
