package com.nexon.story.dto.res;

import com.nexon.story.dto.model.StoryListVO;
import lombok.Data;

import java.util.List;

@Data
public class StoryListRes {

    private List<StoryListVO> list;
    int totalCnt;
    int noticeCnt;

}
