package com.nexon.admin.popup.dto.model;

import lombok.Data;

@Data
public class PopupDtlVO {

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
}
