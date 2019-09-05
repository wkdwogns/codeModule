package com.nexon.admin.notice.req;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Data
public class InsertNoticeReq {
    private String title;
    private String importantYn;
    private String viewYn;
    private String viewStDt;
    private String viewEndDt;
    private String viewUnlimitYn;
    private Integer fileGrpSeq;
    private Integer imgGrpSeq;
    private String contents;
    private String noticeType;
    private String creId;
    private String updId;
    private MultipartFile file;
    private MultipartFile img;
    private ArrayList<String> editorDelImg;

}
