package com.nexon.admin.banner.res;

import lombok.Data;

import java.util.List;

@Data
public class SelectBannerDetailRes {
    private Integer bannerSeq;
    private String bannerNm;
    private String bannerType;
    private String viewStDt;
    private String viewEndDt;
    private String viewUnlimitYn;
    private String targetUrl;
    private String videoUrl;
    private String thumbnailNm;
    private String topText;
    private String title;
    private String contents;
    private String sortNo;
    private String category;
    private Integer fileGrpSeq;

    private List fList;
}
