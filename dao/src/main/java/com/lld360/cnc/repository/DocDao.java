package com.lld360.cnc.repository;

import com.lld360.cnc.dto.DocDto;
import com.lld360.cnc.model.Doc;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface DocDao {

    void create(Doc doc);

    int update(Doc doc);

    void delete(Long id);

    int updateDocState(@Param("docId") Long docId, @Param("state") byte state);

    long updateDocsState(@Param("ids") Long[] ids, @Param("toState") byte toState, @Param("fromState") Byte fromState);

    int updateDocCategory(@Param("docId") long docId, @Param("categoryId") int categoryId);

    List<Doc> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    List<Doc> docByKeyWord(String keyword);

    long countByKeyWord(String keyword);

    long countDownloads(Map<String, Object> parameters);

    /**
     * 查询指定月份普通用户上传的文档数量
     *
     * @param userId 用户ID
     * @param month  月份时间（以时间的年月为准）
     * @return 用户该月上传文档数量
     */
    long countUploadByUserAndMonth(@Param("userId") long userId, @Param("month") Date month);

    Doc find(Long id);

    long countWeb(Map<String, Object> parameters);

    List<Doc> searchWeb(Map<String, Object> parameters);

    Doc findWeb(Long id);

    List<DocDto> getCollects(Map<String, Object> param);

    List<DocDto> getDownloads(Map<String, Object> param);

    List<Doc> searchForIndex(Map<String, Object> parameters);

    List<Doc> findByMd5(@Param("md5") String md5);

    List<Doc> findByType(String type);

    List<Doc> findByTypes(@Param("types") String... types);

    List<Doc> findDisposingDocs();

    /**
     * 获取一个文档所获得的所有积分
     *
     * @param docId 文档ID
     * @return 文档积分总数
     */
    int sumScoreOfDoc(long docId);

//    Map<String, Object> searchViewsByCategoryId(Integer categoryId);

    long searchViewsByCategoryId(Integer categoryId);

    long searchUploadNum(Map<String, Object> params);


}