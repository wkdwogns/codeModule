package com.nexon.admin.story.req;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Data
public class UpdateStoryReq {
    private String seq;
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
    private Integer thumbnailSeq;

    private MultipartFile image;
    private ArrayList<String> editorDelImg;
}
