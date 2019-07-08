package com.nexon.notice.dto.res;

import com.nexon.notice.dto.model.NoticeDetailPrevNextVO;
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
    private String viewUnlimit;
    private String contents;
    private String creId;
    private String creDt;
    private String updId;

    private Integer fileGrpSeq;
    private String attach;
    private String attachName;
    private List fList;

    private NoticeDetailPrevNextVO prevNotice;
    private NoticeDetailPrevNextVO nextNotice;
}
