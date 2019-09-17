package com.nexon.notice.dto.res;

import com.nexon.notice.dto.model.NoticeDetailPrevNextVO;
import lombok.Data;

import java.util.List;

@Data
public class SelectNoticeDetailRes {
    private String noticeSeq;
    private String title;
    private String viewStDt;
    private String contents;
    private String fileSeq;
    private String orgFileNm;
    private NoticeDetailPrevNextVO prevNotice;
    private NoticeDetailPrevNextVO nextNotice;
}
