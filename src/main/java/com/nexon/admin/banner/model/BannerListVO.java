package com.nexon.admin.banner.model;

import lombok.Data;

@Data
public class BannerListVO {
    private String no;
    private Integer bannerSeq;
    private String bannerNm;
    private String bannerType;
    private String typeNm;
    private String viewStDt;
    private String viewEndDt;
    private String viewUnlimitYn;
    private String sortNo;
    private String delYn;
    private String creDt;
    private String category;
    private String filePath;
}
