package com.nexon.admin.notice.req;

import com.nexon.common.dto.req.CommonReq;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Data
public class UpdateNoticeReq extends CommonReq {
    private String seq;

    private String title;
    private String importantYn;
    private String viewYn;
    private String viewStDt;
    private String viewEndDt;
    private String viewUnlimitYn;
    private String noticeType;

    private Integer fileGrpSeq;
    private Integer imgGrpSeq;

    private String contents;
    private String creId;
    private String updId;

    private Integer attachSeq;
    private Integer imgSeq;

    private MultipartFile[] file;
    private ArrayList<String> delete;

    private MultipartFile img;
    private ArrayList<String> editorDelImg;
}
