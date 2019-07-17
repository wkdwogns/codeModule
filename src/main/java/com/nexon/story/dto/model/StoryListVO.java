package com.nexon.story.dto.model;

import lombok.Data;

@Data
public class StoryListVO {
    private int no;
    private int storySeq;
    private String storyNm;
    private String category;
    private String title;
    private String importantYn;
    private String viewUnlimitYn;
    private String viewYn;
    private String viewStDt;
    private String viewEndDt;
    private String viewCheck;
    private String filePath;

}
