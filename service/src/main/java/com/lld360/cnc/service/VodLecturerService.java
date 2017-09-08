package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.model.Admin;
import com.lld360.cnc.model.User;
import com.lld360.cnc.model.VodLecturer;
import com.lld360.cnc.repository.VodLecturerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VodLecturerService extends BaseService {

    @Autowired
    private VodLecturerDao vodLecturerDao;

    public void createLecturer(Long userId, Admin admin) {
        VodLecturer vodLecturer = vodLecturerDao.getByUserId(userId);
        if (vodLecturer == null || vodLecturer.getUserId() == null) {
            vodLecturer = new VodLecturer(userId, admin.getName(), Const.VODLECTURER_STATE_NORMAL);
            vodLecturerDao.create(vodLecturer);
        } else if (!vodLecturer.getState().equals(Const.VODLECTURER_STATE_NORMAL)) {
            vodLecturer.setState(Const.VODLECTURER_STATE_NORMAL);
            vodLecturerDao.update(vodLecturer);
        }
    }

    @Transactional
    public void deleteLecturer(Long userId) {
        VodLecturer vodLecturer = vodLecturerDao.getByUserId(userId);
        if (vodLecturer != null) {
            vodLecturer.setState(Const.VODLECTURER_STATE_DELETE);
            vodLecturerDao.update(vodLecturer);
            vodLecturerDao.setLecturerNull(userId);
        }
    }

    public Boolean checkIsLecturer(Long userId) {
        return vodLecturerDao.isVodLecturer(userId);
    }

    public List<User> getAllVodLecturer(){
        return vodLecturerDao.getAllVodLecturer();
    }

}
