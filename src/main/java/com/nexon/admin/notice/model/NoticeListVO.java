package com.nexon.admin.notice.model;

import lombok.Data;

@Data
public class NoticeListVO {
    private String no;
    private String noticeSeq;
    private String title;
    private String importantYn;
    private String fileGrpSeq;
    private String viewYn;
    private String viewStDt;
    private String viewEndDt;
    private String viewUnlimitYn;
    private String noticeType;
    private String contents;
    private String creId;
    private String updId;
}
