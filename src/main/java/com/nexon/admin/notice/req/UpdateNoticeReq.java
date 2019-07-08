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
    private Integer fileGrpSeq;
    private String contents;
    private String creId;
    private String updId;

    private Integer attach;
    private Integer attachSeq;
    private MultipartFile file;

    private ArrayList<String> editorDelImg;
}
