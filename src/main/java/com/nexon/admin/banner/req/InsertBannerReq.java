package com.nexon.admin.banner.req;

import com.nexon.common.dto.req.CommonReq;
import io.swagger.annotations.ApiParam;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class InsertBannerReq  extends CommonReq{
    @ApiParam(value = "배너명")
    private String bannerNm;
    @ApiParam(value = "배너타입")
    private String bannerType;
    @ApiParam(value = "노출시작일자")
    private String viewStDt;
    @ApiParam(value = "노출종료일자")
    private String viewEndDt;
    @ApiParam(value = "노출무제한여부")
    private String viewUnlimitYn;
    @ApiParam(value = "노출여부")
    private String viewYn;
    @ApiParam(value = "이동링크")
    private String targetUrl;
    @ApiParam(value = "영상링크")
    private String videoUrl;
    @ApiParam(value = "파일업로드 시퀀스", hidden = true)
    @Ignore
    private Integer fileGrpSeq;
    @ApiParam(value = "썸네일명")
    private String thumbnailNm;
    @ApiParam(value = "상단문구")
    private String topText;
    @ApiParam(value = "배너제목")
    private String title;
    @ApiParam(value = "배너내용")
    private String contents;
    @ApiParam(value = "정렬")
    private String sortNo;
    @ApiParam(value = "카테고리")
    private String category;
    @ApiParam(value = "배너 이미지")
    private MultipartFile image;
}
