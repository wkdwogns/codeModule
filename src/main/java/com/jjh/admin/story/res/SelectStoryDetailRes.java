package com.jjh.admin.story.res;

import lombok.Data;

import java.util.List;

@Data
public class SelectStoryDetailRes {
    private Integer no;
    private Integer category;
    private String storyNm;
    private String title;
    private String importantYn;

    private String viewYn;
    private String viewUnlimitYn;
    private String contents;
    private String viewStDt;
    private String orderNo;

    private Integer fileGrpSeq;
    private Integer imgGrpSeq;

    private List fList;
    private List iList;

}
