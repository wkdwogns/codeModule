package com.nexon.admin.story.dao;


import com.nexon.admin.story.req.*;
import com.nexon.admin.story.res.SelectStoryDetailRes;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
