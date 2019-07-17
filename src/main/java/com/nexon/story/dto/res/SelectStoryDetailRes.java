package com.nexon.story.dto.res;

import com.nexon.story.dto.model.StoryDetailPrevNextVO;
import lombok.Data;

import java.util.List;

@Data
public class SelectStoryDetailRes {
    private Integer no;
    private String storyNm;
    private String category;
    private String title;
    private String importantYn;
    private String viewYn;
    private String viewUnlimitYn;
    private String contents;
    private String viewStDt;
    private String viewEndDt;
    private String creDt;

    private Integer fileGrpSeq;
    private List fList;

    private StoryDetailPrevNextVO prevStory;
    private StoryDetailPrevNextVO nextStory;


}
