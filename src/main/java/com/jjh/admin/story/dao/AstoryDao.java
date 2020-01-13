package com.jjh.admin.story.dao;


import com.jjh.admin.story.model.TopStoryVO;
import com.jjh.admin.story.req.*;
import com.jjh.admin.story.res.SelectStoryDetailRes;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface AstoryDao {

    void insertStory(InsertStoryReq req);

    List selectStory(SelectStoryReq req);

    int selectStoryCnt(SelectStoryReq req);

    void updateStory(UpdateStoryReq req);

    void deleteStory(DeleteStoryReq req);

    SelectStoryDetailRes selectStoryDetail(SelectStoryDetailReq req);

    int selectViewLimitCnt();

    void deleteFile(DeleteStoryImgReq req);

    List<TopStoryVO> getTopStory(SelectTopStoryReq req);

    void setTopStory(Map map);

    void deleteTopStory();

    void updateFileGrpSeq(Map map);

    void deleteFileGrpSeq(Map map);
}
