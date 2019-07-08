package com.nexon.admin.banner.req;

import com.nexon.admin.banner.model.UpdateBannerSortVO;
import com.nexon.common.dto.req.CommonReq;
import io.swagger.annotations.ApiParam;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;

import java.util.List;

@Data
public class UpdateBannerSortReq extends CommonReq {
    private List<UpdateBannerSortVO> sortArry;

    @ApiParam(value = "배너 카테고리", hidden = true)
    @Ignore
    private String category;
}
