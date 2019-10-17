package com.nexon.admin.notice.req;

import com.nexon.common.dto.req.CommonReq;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Data
public class InsertNoticeReq extends CommonReq{
    private String title;
    private String importantYn;
    private String viewYn;
    private String viewStDt;
    private String viewEndDt;
    private String viewUnlimitYn;
    private String contents;
    private String noticeType;
    private String creId;
    private String updId;

    private Integer fileGrpSeq;
    private Integer imgGrpSeq;
    private MultipartFile[] file;
    private MultipartFile img;

    private ArrayList<String> editorDelImg;

}
