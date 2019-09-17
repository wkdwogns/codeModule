package com.nexon.notice.dto.model;

import lombok.Data;

@Data
public class NoticeListVO {
    private int no;
    private String noticeSeq;
    private String title;
    private String filePath;
    private String viewStDt;
}
