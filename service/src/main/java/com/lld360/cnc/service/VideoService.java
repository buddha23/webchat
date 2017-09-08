package com.lld360.cnc.service;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.mts.model.v20140618.AddMediaRequest;
import com.aliyuncs.mts.model.v20140618.AddMediaResponse;
import com.aliyuncs.mts.model.v20140618.QueryMediaListRequest;
import com.aliyuncs.mts.model.v20140618.QueryMediaListResponse;
import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.utils.AliYunOSSUtil;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.dto.VodVolumesDto;
import com.lld360.cnc.dto.VolumeDto;
import com.lld360.cnc.model.*;
import com.lld360.cnc.repository.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VideoService extends BaseService {

    @Autowired
    private FileUtilService fileUtilService;

    @Autowired
    private VodVolumesDao vodVolumesDao;

    @Autowired
    private VodChaptersDao vodChaptersDao;

    @Autowired
    private VodSectionDao vodSectionDao;

    @Autowired
    private VodBuysDao vodBuysDao;

    @Autowired
    private UserScoreDao userScoreDao;

    @Autowired
    private UserScoreHistoryDao userScoreHistoryDao;

    @Autowired
    private UserPointDao userPointDao;

    @Autowired
    private UserPointHistoryDao userPointHistoryDao;

    @Autowired
    private AliYunOSSUtil aliYunOSSUtil;

    @Autowired
    private UserInviteService userInviteService;

    @Autowired
    private UserMemberService userMemberService;

    @Autowired
    private VodTagDao vodTagDao;

    @Autowired
    private UserService userService;

    @Autowired
    private WxTempMsgService wxTempMsgService;

    public Page<VodVolumesDto> getVodVolumesPage(Map<String, Object> params) {
        if (!params.containsKey("state"))
            params.put("state", Const.VIDEO_STATE_NORMAL);
        Pageable pageable = getPageable(params);
        List<VodVolumesDto> list = vodVolumesDao.search(params);
        long count = vodVolumesDao.count(params);
        return new PageImpl<>(list, pageable, count);
    }

    public VodVolumesDto getVodVolumeById(Integer id) {
        VodVolumesDto vodVolumes = vodVolumesDao.find(id);
        if (vodVolumes == null)
            throw new ServerException(HttpStatus.NOT_FOUND);
        return vodVolumes;
    }

    public VodVolumes getVolumeBySectionId(Long id) {
        VodVolumes vodVolumes = vodVolumesDao.getVolumeBySectionId(id);
        if (vodVolumes == null)
            throw new ServerException(HttpStatus.NOT_FOUND);
        return vodVolumes;
    }

    /**
     * 根据视频集ID获取包含视频章节内容的视频集
     *
     * @param id 视频集ID
     * @return 包含章节内容的全量视频集
     */
    public VodVolumes getVodVolume(int id) {
        VodVolumes volume = vodVolumesDao.findWithSections(id);
        if (volume == null) {
            throw new ServerException(HttpStatus.NOT_FOUND);
        }
        return volume;
    }

    /**
     * 根据条件获取包含视频章节内容的视频集
     *
     * @param params 查询条件
     * @return 包含章节内容的全量视频集列表
     */
    public List<VodVolumes> getVolumes(Map<String, Object> params) {
        return vodVolumesDao.searchWithSections(params);
    }

    // 获取视频集章节列表
    public List<VodChapters> getVodChapters(Map<String, Object> params) {
        return vodChaptersDao.search(params);
    }

    // 获取视频集第一段视频(存在的视频)
    public VodSection getFirstSectionByVolumesId(long volumesId) {
        return vodSectionDao.getFirstSectionByVolumesId(volumesId);
    }

    // 获取小节播放
    public VodSection getVodSectionById(long id) {
        VodSection vodSection = vodSectionDao.find(id);
        if (vodSection == null)
            throw new ServerException(HttpStatus.NOT_FOUND);
        return vodSection;
    }

    // 购买视频
    @Transactional
    public void buyVolume(UserDto userDto, VodVolumesDto vodVolumes) {
        Integer costScore = vodVolumes.getCostScore();
        if (userMemberService.isMember(userDto.getId())) {
            BigDecimal cs = BigDecimal.valueOf(costScore * Const.USER_MEMBER_DISCOUNT).setScale(0, BigDecimal.ROUND_HALF_UP);
            costScore = cs.intValue();
        }
        if (costScore > 0 && !isBuyVolume(userDto.getId(), vodVolumes.getId())) {
            UserScore userScore = userScoreDao.find(userDto.getId());
            if (costScore > userScore.getTotalScore())
                throw new ServerException(HttpStatus.FORBIDDEN, "牛人币不足");
            // 购买记录
            VodBuys vodBuys = new VodBuys(vodVolumes.getId(), userDto.getId(), costScore, "牛人币购买视频", Const.VOD_BUY_TYPE_SCORE);
            vodBuysDao.create(vodBuys);
            // 扣分
            userScoreDao.updateScore(userDto.getId(), -costScore);
            String dd = "购买视频集《" + vodVolumes.getName() + "》扣除牛人币";
            userScoreHistoryDao.create(new UserScoreHistory(userDto.getId(), Const.USER_SCORE_HISTORY_TYPE_DOC_DOWNLOAD, -costScore, vodBuys.getId(), dd));
            // 加积分
            userPointDao.updatePoint(userDto.getId(), costScore * Const.USER_ADDPOINT_SCALE_SCORE_OPERATE);
            userPointHistoryDao.create(new UserPointHistory(userDto.getId(), Const.USER_POINT_HISTORY_TYPE_VOD_BUY, costScore * Const.USER_ADDPOINT_SCALE_SCORE_OPERATE, vodVolumes.getId().longValue(), dd));
            //  给邀请者和区域合伙人 讲师分利
            userInviteService.doBonusByInvite(userDto, costScore);
            doLecturerBonus(vodVolumes, costScore);
            // 短信通知

        }
        // push消息
        ThirdAccount thirdAccount = userService.getThirdAccountByUserid(userDto.getId(), Const.THIRD_ACCOUNT_TYPE_WEIXIN);
        if (thirdAccount != null) wxTempMsgService.sendVideoBuyWxMsg(thirdAccount, vodVolumes);
    }

    public void doLecturerBonus(VodVolumes vodVolumes, Integer costScore) {
        if (vodVolumes.getLecturerId() != null && vodVolumes.getLecturerId() != 0) {
            Integer bonus = costScore * vodVolumes.getLecturerProfit() / 100;
            if (bonus <= 0) return;
            userScoreDao.updateScore(vodVolumes.getLecturerId(), bonus);
            String dd = "用户购买视频集《" + vodVolumes.getName() + "》获得分利";
            userScoreHistoryDao.create(new UserScoreHistory(vodVolumes.getLecturerId(), Const.USER_SCORE_HISTORY_TYPE_DOC_DOWNLOAD, bonus, vodVolumes.getId().longValue(), dd));
        }
    }

    // 验证是否购买
    public boolean isBuyVolume(long userId, Integer volumeId) {
        Map<String, Object> params = new HashMap<>();
        params.put("volumeId", volumeId);
        params.put("userId", userId);
        return vodBuysDao.count(params) > 0;
    }

    // 保存视频集（新增和修改）
    @Transactional
    public VodVolumes save(VodVolumes volume) {
        if (volume.getChapters() == null || volume.getChapters().isEmpty()) {
            throw new ServerException(HttpStatus.BAD_REQUEST, "视频集未包含任何章节");
        }
        Date now = Calendar.getInstance(Locale.CHINA).getTime();

        volume.setUpdateTime(now);
        volume.setState(Const.VIDEO_STATE_NORMAL);

        if (volume.getId() == null) {
            volume.setDuration(0.0);
            volume.setCreateTime(now);
            vodVolumesDao.create(volume);
        } else {
            VodVolumes v = vodVolumesDao.find(volume.getId());
            if (v.getCover() != null && !v.getCover().equals(volume.getCover())) {
                fileUtilService.deleteByRelativePath(v.getCover());
            }
            vodVolumesDao.update(volume);
        }

        for (VodChapters chapter : volume.getChapters()) {
            chapter.setVolumeId(volume.getId());
            saveChapter(chapter);
        }

        // 保存完毕后清理掉废弃的章
        long[] cids = volume.getChapters().stream().filter(c -> c.getId() != null).mapToLong(VodChapters::getId).toArray();
        vodChaptersDao.deleteByNotBelongVolume(volume.getId(), cids);

        // 保存完毕之后清理掉废弃的小节
        for (VodChapters chapter : volume.getChapters()) {
            long[] sids = null;
            if (chapter.getVodSections() != null) {
                sids = chapter.getVodSections().stream().filter(s -> s.getId() != null).mapToLong(VodSection::getId).toArray();
            }
            vodSectionDao.deleteByNotBelongChapter(chapter.getId(), sids);
        }
        vodVolumesDao.updateStateBySections(volume.getId());

        // 执行添加媒体
        // 因成本问题，暂时取消媒体功能，以后需要再加上
        // taskExecutor.execute(() -> addMediaVolume(volume.getId()));

        return volume;
    }

    // 保存视频章
    @Transactional
    public VodChapters saveChapter(VodChapters chapter) {
        if (chapter.getVolumeId() == null) {
            throw new ServerException(HttpStatus.BAD_REQUEST, "视频集ID不能为空");
        }
        if (chapter.getId() == null || chapter.getId() == 0) {
            vodChaptersDao.create(chapter);
        } else {
            vodChaptersDao.update(chapter);
        }
        if (chapter.getVodSections() != null && !chapter.getVodSections().isEmpty()) {
            for (VodSection section : chapter.getVodSections()) {
                section.setChapterId(chapter.getId());
                saveSection(section);
            }
        }
        return chapter;
    }

    // 保存视频节
    public VodSection saveSection(VodSection section) {
        if (section.getChapterId() == null) {
            throw new ServerException(HttpStatus.BAD_REQUEST, "视频章ID不能为空");
        }
        if (section.getFree() == null) section.setFree(false);
        if (section.getDuration() == null) {
            section.setDuration(0.0);
        }

        if (section.getId() == null) {
            section.setExt(FilenameUtils.getExtension(section.getUrl()));
            section.setVodName(FilenameUtils.getName(section.getUrl()));
            section.setState(VodSection.ST_NORMAL);
            vodSectionDao.create(section);
        } else {
            vodSectionDao.update(section);
        }
        return section;
    }

    /**
     * 更改视频集状态
     *
     * @param id    视频集ID
     * @param state 目标状态
     * @return 影响行数
     */
    public int changeState(int id, byte state) {
        return vodVolumesDao.updateState(id, state);
    }

    /**
     * 根据视频集的所有视频小节状态来判断视频集的状态和更新视频集时长
     *
     * @return 更新数量
     */
    public int updateAllDealingVolumes() {
        return vodVolumesDao.updateAllStateBySections();
    }

    // 添加媒体（需异步调用）
    public void addMediaVolume(int volumeId) {
        List<VodSection> sectionList = vodSectionDao.findByVolume(volumeId, VodSection.ST_NOTRANS);
        if (sectionList.isEmpty()) {
            return;
        }
        VodVolumes volume = vodVolumesDao.find(volumeId);
        if (volume == null) {
            return;
        }
        AddMediaRequest request = new AddMediaRequest();
        if (volume.getWorkflow() != null && volume.getWorkflow().length() == 32) {
            request.setMediaWorkflowId(volume.getWorkflow());
        }
        AddMediaResponse response;
        for (VodSection section : sectionList) {
            request.setTitle(section.getName());
            request.setFileURL(aliYunOSSUtil.getObjectUrl(section.getUrl()));
            try {
                response = aliYunOSSUtil.getAcsClient().getAcsResponse(request);
                AddMediaResponse.Media media = response.getMedia();
                if (media != null) {
                    section.setMediaId(media.getMediaId());
                    section.setState(VodSection.ST_TRANSINT);
                    vodSectionDao.update(section);
                }
            } catch (ClientException e) {
                logger.warn("添加媒体请求失败: SectionId=" + section.getId(), e);
            }
        }
    }

    /**
     * ★ 仅限异步或定时任务调用
     * 获取某个视频集中所有未转码视频的转码状态
     *
     * @param volume 视频集
     */
    public void queryMediaByVolume(VodVolumes volume) {
        if (volume == null || StringUtils.isEmpty(volume.getWorkflow()) || volume.getChapters() == null || volume.getChapters().isEmpty())
            return;
        List<VodSection> sections = new ArrayList<>();
        for (VodChapters chapter : volume.getChapters()) {
            if (chapter.getVodSections() == null || chapter.getVodSections().isEmpty()) continue;
            sections.addAll(chapter.getVodSections().stream().filter(s -> s.getMediaId() != null && s.getState().equals(VodSection.ST_TRANSINT)).collect(Collectors.toList()));
        }
        if (sections.isEmpty()) return;

        List<Map<String, VodSection>> tempList = new ArrayList<>();
        Map<String, VodSection> map = new HashMap<>();
        for (int i = 0; i < sections.size(); i++) {
            if (i % 10 == 0 && !map.isEmpty()) {
                tempList.add(map);
                map = new HashMap<>();
            }
            map.put(sections.get(i).getMediaId(), sections.get(i));
        }
        if (map.size() > 0) {
            tempList.add(map);
        }

        QueryMediaListRequest request = new QueryMediaListRequest();
        QueryMediaListResponse response;
        for (Map<String, VodSection> ms : tempList) {
            String mediaIds = String.join(",", ms.keySet().toArray(new String[ms.size()]));
            request.setMediaIds(mediaIds);
            try {
                response = aliYunOSSUtil.getAcsClient().getAcsResponse(request);
                if (!response.getMediaList().isEmpty()) {
                    List<QueryMediaListResponse.Media> publishedList = response.getMediaList().stream().filter(m -> m.getPublishState().equals("Published")).collect(Collectors.toList());
                    for (QueryMediaListResponse.Media media : publishedList) {
                        if (!ms.containsKey(media.getMediaId())) continue;

                        VodSection section = ms.get(media.getMediaId());
                        section.setBitrate(Double.parseDouble(media.getBitrate()));
                        section.setDuration(Double.parseDouble(media.getDuration()));
                        section.setWidth(Integer.parseInt(media.getWidth()));
                        section.setHeight(Integer.parseInt(media.getHeight()));
                        section.setSize(Integer.parseInt(media.getSize()));
                        section.setState(VodSection.ST_NORMAL);

                        String mediaKey = aliYunOSSUtil.mediaOutKeyFormat.replace("{RunId}", media.getRunIdList().get(0)).replace("{FileName}", FilenameUtils.getName(section.getUrl()));
                        section.setTransUrl(mediaKey);
                        vodSectionDao.update(section);
                    }
                }
            } catch (ClientException e) {
                logger.warn("查询媒体请求失败: MediaIds=" + mediaIds, e);
            }
        }
    }

    /**
     * 后端管理的视频集列表
     *
     * @param params 搜索条件
     * @return 查询分页结果
     */
    public Page<VolumeDto> getVolumeDtoPage(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        int total = vodVolumesDao.countVolumeDto(params);
        List<VolumeDto> content = total > 0 ? vodVolumesDao.searchVolumeDto(params) : new ArrayList<>();
        return new PageImpl<>(content, pageable, total);
    }

    // 获取全部标签
    public List<VodTag> getAllTag() {
        return vodTagDao.getAllTag();
    }

    // 获取视频集标签
    public List<VodTag> getVodTag(Integer volumeId) {
        return vodTagDao.getVodTag(volumeId);
    }

    // 添加视频集标签
    public void addVodTag(Integer volumeId, Integer[] tags) {
        vodTagDao.delVodTag(volumeId);
        if (tags.length > 0)
            for (Integer tagId : tags) {
                vodTagDao.addVodTag(volumeId, tagId);
            }
    }

    // 获取购买历史
    public Page<VodBuys> getVodBuys(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        long total = vodBuysDao.count(params);
        List<VodBuys> list = total > 0 ? vodBuysDao.search(params) : new ArrayList<>();
        return new PageImpl<>(list, pageable, total);
    }

    // 增加浏览量
    public void addViews(Integer volumeId) {
        vodVolumesDao.addViews(volumeId);
    }

}
