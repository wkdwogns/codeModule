package com.jjh.admin.story.req;

import com.jjh.common.dto.req.CommonReq;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Data
public class UpdateStoryReq extends CommonReq{
    private String seq;
    private Integer category;
    private String storyNm;
    private String title;
    private String importantYn;
    private String viewYn;
    private String viewUnlimitYn;
    private String contents;
    private String viewStDt;
    private Integer orderNo;
    private String creId;

    private Integer fileGrpSeq;
    private Integer imgGrpSeq;
    private Integer attachSeq;
    private Integer imgSeq;

    private MultipartFile[] file;
    private ArrayList<String> delete;
    private MultipartFile img;
    private ArrayList<String> editorDelImg;
}
