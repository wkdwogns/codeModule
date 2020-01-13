package com.jjh.admin.story.model;

import lombok.Data;

@Data
public class StoryListVO {
    private String no;
    private Integer category;
    private Integer storySeq;
    private String title;
    private String fileGrpSeq;
    private String viewYn;
    private String viewStDt;
    private String creId;
    private String updId;
}
