package com.nexon.banner.model;

import lombok.Data;

@Data
public class PopupBannerListVO {
    private String popupSeq;
    private String popupNm;
    private String viewYn;
    private String viewStDt;
    private String viewEndDt;
    private String viewUnlimitYn;
    private String targetUrl;
    private String targetYn;
    private String fileGrpSeq;
}