package com.nexon.banner.controller;

import com.nexon.banner.req.*;
import com.nexon.banner.res.SelectBannerRes;
import com.nexon.banner.service.BannerService;
import com.nexon.common.dto.res.ResponseHandler;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @ApiOperation(value = "상단 배너 목록")
    @GetMapping
    public ResponseHandler<SelectBannerRes> selectBanner() {
        BannerTypeReq req = new BannerTypeReq();
        ResponseHandler<SelectBannerRes> result = bannerService.selectBanner(req);
        return  result;
    }

    @ApiOperation(value = "컨텐츠 배너 목록")
    @GetMapping(value = "/contents")
    public ResponseHandler<SelectBannerRes> selectContentsBanner() {
        BannerTypeReq req = new BannerTypeReq();
        ResponseHandler<SelectBannerRes> result = bannerService.selectContentsBanner(req);
        return  result;
    }

    @ApiOperation(value = "팝업 배너 목록")
    @GetMapping(value = "/popup")
    public ResponseHandler<?> selectPopupBanner() {
        ResponseHandler<SelectBannerRes> result = bannerService.selectPopupBanner();
        return  result;
    }
}
