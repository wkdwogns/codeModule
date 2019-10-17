package com.nexon.admin.story.req;

import com.nexon.common.dto.req.CommonReq;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import java.util.ArrayList;

@Data
public class InsertStoryReq extends CommonReq {
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
    private MultipartFile[] file;
    private MultipartFile img;

    private ArrayList<String> editorDelImg;
}
