package com.nexon.story.dto.res;

import com.nexon.story.dto.model.StoryDetailPrevNextVO;
import lombok.Data;

import java.util.List;

@Data
public class SelectStoryDetailRes {
    private String StorySeq;
    private String title;
    private String contents;
    private String viewStDt;
    private String fileSeq;
    private String orgFileNm;

    private StoryDetailPrevNextVO prevStory;
    private StoryDetailPrevNextVO nextStory;
}
