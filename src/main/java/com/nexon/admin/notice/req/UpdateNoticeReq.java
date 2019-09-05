package com.nexon.admin.notice.req;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Data
public class UpdateNoticeReq {
    private String seq;

    private String title;
    private String importantYn;
    private String viewYn;
    private String viewStDt;
    private String viewEndDt;
    private String viewUnlimitYn;
    private String noticeType;
    private Integer imgGrpSeq;
    private Integer fileGrpSeq;
    private String contents;
    private String creId;
    private String updId;

    private Integer attachSeq;
    private Integer imgSeq;

    private MultipartFile file;
    private MultipartFile img;
    private ArrayList<String> editorDelImg;
}
