package com.nexon.admin.banner.req;

import com.nexon.common.dto.req.CommonReq;
import lombok.Data;

@Data
public class DeleteBannerReq  extends CommonReq {
    private Integer bannerSeq;
    private String delYn;
}
