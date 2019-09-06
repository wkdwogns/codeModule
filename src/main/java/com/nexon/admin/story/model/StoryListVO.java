package com.nexon.admin.story.model;

import lombok.Data;

@Data
public class StoryListVO {
    private String no;
    private Integer category;
    private Integer storySeq;
    private String storyNm;
    private String title;
    private String importantYn;
    private String viewUnlimitYn;
    private String viewYn;
    private String viewStDt;
    private String viewEndDt;
    private String creId;
    private String updId;
}
