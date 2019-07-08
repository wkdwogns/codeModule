package com.nexon.banner.model;

import lombok.Data;

@Data
public class BannerListVO {
    private String bannerSeq;
    private String bannerNm;
    private String bannerType;
    private String viewStDt;
    private String viewEndDt;
    private String viewUnlimitYn;
    private String targetUrl;
    private String videoUrl;
    private String filePath;
    private String thumbnailNm;
    private String  topText;
    private String title;
    private String contents;
    private String sortNo;
    private String category;
}
