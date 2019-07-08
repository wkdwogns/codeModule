package com.nexon.admin.banner.controller;

import com.nexon.admin.banner.req.*;
import com.nexon.admin.banner.res.SelectBannerDetailRes;
import com.nexon.admin.banner.res.SelectBannerRes;
import com.nexon.admin.banner.service.ABannerService;
import com.nexon.common.dto.res.ResponseHandler;
import com.nexon.common.session.SessionCheck;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mapi/banner")
public class ABannerController {

    @Autowired
    private ABannerService aBannerService;

    @ApiOperation(value = "배너 목록")
    @GetMapping
    public ResponseHandler<SelectBannerRes> selectBanner(SelectBannerReq req) {
        ResponseHandler<SelectBannerRes> result = aBannerService.selectBanner(req);
        return  result;
    }

    @ApiOperation(value = "배너 노출 목록")
    @GetMapping(value="/view")
    public ResponseHandler<SelectBannerRes> selectViewBanner() {
        SelectBannerReq req = new SelectBannerReq();
        ResponseHandler<SelectBannerRes> result = aBannerService.selectViewBanner(req);
        return  result;
    }

    @ApiOperation(value = "배너 상세")
    @GetMapping("/detail")
    public ResponseHandler<SelectBannerDetailRes> selectBannerDetail(SelectBannerDetailReq req) {
        ResponseHandler<SelectBannerDetailRes> result = aBannerService.selectBannerDetail(req);
        return  result;
    }

    @ApiOperation(value = "배너 쓰기")
    @PostMapping
    @SessionCheck
    public ResponseHandler<?> insertBanner(InsertBannerReq req) {
        ResponseHandler<?> result = aBannerService.insertBanner(req);
        return  result;
    }

    @ApiOperation(value = "배너 수정")
    @PostMapping("/edit")
    @SessionCheck
    public ResponseHandler<?> updateBanner(UpdateBannerReq req) {
        ResponseHandler<?> result = aBannerService.updateBanner(req);
        return  result;
    }

    @ApiOperation(value = "배너 삭제")
    @DeleteMapping
    @SessionCheck
    public ResponseHandler<?> deleteBanner(@RequestBody(required = false) DeleteBannerReq req) {
        ResponseHandler<?> result = aBannerService.deleteBanner(req);
        return  result;
    }

    @ApiOperation(value = "배너 정렬")
    @PutMapping(value = "/sort")
    @SessionCheck
    public ResponseHandler<?> updateViewControl(@RequestBody(required = false) UpdateBannerSortReq req) {
        ResponseHandler<?> result = aBannerService.updateViewControl(req);
        return result;
    }

    @ApiOperation(value = "컨텐츠 배너 목록")
    @GetMapping(value = "/contents")
    public ResponseHandler<SelectBannerRes> selectContentBanner(SelectBannerReq req) {
        ResponseHandler<SelectBannerRes> result = aBannerService.selectContentBanner(req);
        return  result;
    }

    @ApiOperation(value = "컨텐츠 배너 노출 목록")
    @GetMapping(value="/view/contents")
    public ResponseHandler<SelectBannerRes> selectViewContentsBanner() {
        SelectBannerReq req = new SelectBannerReq();
        ResponseHandler<SelectBannerRes> result = aBannerService.selectViewContentsBanner(req);
        return  result;
    }

    @ApiOperation(value = "컨텐츠 배너 쓰기")
    @PostMapping(value = "contents")
    @SessionCheck
    public ResponseHandler<?> insertContentsBanner(InsertBannerReq req) {
        ResponseHandler<?> result = aBannerService.insertContentsBanner(req);
        return  result;
    }

    @ApiOperation(value = "컨텐츠 배너 수정")
    @PostMapping("/edit/contents")
    @SessionCheck
    public ResponseHandler<?> updateContentsBanner(UpdateBannerReq req) {
        ResponseHandler<?> result = aBannerService.updateContentsBanner(req);
        return  result;
    }

    @ApiOperation(value = "컨텐츠 배너 정렬")
    @PutMapping(value = "/sort/contents")
    @SessionCheck
    public ResponseHandler<?> updateViewContentsControl(@RequestBody(required = false) UpdateBannerSortReq req) {
        ResponseHandler<?> result = aBannerService.updateViewContentsControl(req);
        return result;
    }
}
