package com.lld360.cnc.repository;

import com.lld360.cnc.model.User;
import com.lld360.cnc.model.VodLecturer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VodLecturerDao {

    void create(VodLecturer vodLecturer);

    int update(VodLecturer vodLecturer);

    Boolean isVodLecturer(Long userId);

    VodLecturer getByUserId(Long userId);

    void setLecturerNull(Long lecturerId);

    List<User> getAllVodLecturer();

}
