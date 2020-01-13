package com.jjh.admin.story.res;

import com.jjh.admin.story.model.StoryListVO;
import lombok.Data;

import java.util.List;

@Data
public class SelectStoryRes {
    List<StoryListVO> list;
    int totalCnt;
    String thumbnailUrl;
}
