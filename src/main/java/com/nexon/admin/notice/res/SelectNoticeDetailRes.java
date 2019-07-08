package com.nexon.admin.notice.res;

import lombok.Data;

import java.util.List;

@Data
public class SelectNoticeDetailRes {
    private String no;
    private String noticeSeq;
    private String title;
    private String importantYn;
    private String viewYn;
    private String viewStDt;
    private String viewEndDt;
    private String viewUnlimitYn;
    private String noticeType;
    private String contents;
    private String creId;
    private String updId;

    private Integer fileGrpSeq;
    private String attach;
    private String attachName;
    private List fList;
}
