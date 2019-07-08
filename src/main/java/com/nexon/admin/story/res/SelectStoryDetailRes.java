package com.nexon.admin.story.res;

import lombok.Data;

import java.util.List;

@Data
public class SelectStoryDetailRes {
    private Integer no;
    private String storyNm;
    private String title;
    private String importantYn;
    private String viewYn;
    private String viewUnlimitYn;
    private String contents;
    private String viewStDt;
    private String viewEndDt;

    private Integer fileGrpSeq;
    private List fList;

}
