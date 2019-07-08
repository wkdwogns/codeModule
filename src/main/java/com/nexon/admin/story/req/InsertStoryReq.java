package com.nexon.admin.story.req;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Data
public class InsertStoryReq {
    private String storyNm;
    private String title;
    private String importantYn;
    private String viewYn;
    private String viewUnlimitYn;
    private String contents;
    private String viewStDt;
    private String viewEndDt;
    private String creId;
    private Integer fileGrpSeq;
    private MultipartFile image;
    private ArrayList<String> editorDelImg;
}
