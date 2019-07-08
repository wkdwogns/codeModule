package com.nexon.banner.req;

import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
public class BannerTypeReq {

    @ApiParam(value = "배너 카테고리")
    private String category;
}
