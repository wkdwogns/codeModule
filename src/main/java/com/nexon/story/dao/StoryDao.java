package com.nexon.story.dao;


import com.nexon.story.dto.model.StoryDetailPrevNextVO;
import com.nexon.story.dto.model.StoryListVO;
import com.nexon.story.dto.req.SelectStoryDetailReq;
import com.nexon.story.dto.req.StoryListReq;
import com.nexon.story.dto.res.SelectStoryDetailRes;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface StoryDao {

    List<StoryListVO> selectStoryList(StoryListReq req);
    int selectStoryListCnt(StoryListReq req);

    List<StoryListVO> selectStoryImportantList();

    SelectStoryDetailRes selectStoryDetail(SelectStoryDetailReq req);

    StoryDetailPrevNextVO selectPrevStory(SelectStoryDetailReq req);
    StoryDetailPrevNextVO selectNextStory(SelectStoryDetailReq req);

    List topStory();
}
