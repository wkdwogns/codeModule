package com.nexon.admin.banner.model;

import com.nexon.common.dto.req.CommonReq;
import lombok.Data;

@Data
public class UpdateBannerSortVO extends CommonReq {
    private Integer bannerSeq;
    private String sortNo;
    private String viewYn;
}