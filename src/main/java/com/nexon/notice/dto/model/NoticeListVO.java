package com.nexon.notice.dto.model;

import lombok.Data;

@Data
public class NoticeListVO {
    private int no;
    private String noticeSeq;
    private String title;
    private String importantYn;
    private String noticeType;
    private String fileGrpSeq;
    private String viewYn;
    private String viewStDt;
    private String viewEndDt;
    private String viewUnlimit;
    private String creDt;
}
