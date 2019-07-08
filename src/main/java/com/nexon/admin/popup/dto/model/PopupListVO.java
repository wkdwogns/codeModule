package com.nexon.admin.popup.dto.model;

import lombok.Data;

@Data
public class PopupListVO {

    private Integer popupNo;
    private Integer popupSeq;
    private String popupNm;
    private String viewYn;
    private String viewStDt;
    private String viewEndDt;
    private String viewUnlimitYn;
    private String targetUrl;
    private String targetYn;
    private Integer fileGrpSeq;
    private Integer fileSeq;
    private String orgFileNm;
    private String sysFileNm;
    private String filePath;
}
