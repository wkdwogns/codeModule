package com.nexon.admin.banner.req;

import com.nexon.common.dto.req.CommonReq;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateBannerReq  extends CommonReq {
    private Integer bannerSeq;
    private String bannerNm;
    private String bannerType;
    private String viewStDt;
    private String viewEndDt;
    private String viewUnlimitYn;
    private String viewYn;
    private String targetUrl;
    @ApiParam(value = "영상링크")
    private String videoUrl;
    private Integer fileGrpSeq;
    private String thumbnailNm;
    private String topText;
    private String title;
    private String contents;
    private String sortNo;
    private String delYn;
    private String category;
    private MultipartFile image;
}
