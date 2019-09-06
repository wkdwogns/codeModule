package com.nexon.admin.notice.controller;

import com.nexon.admin.notice.req.*;
import com.nexon.admin.notice.service.AnoticeService;
import com.nexon.common.dto.res.ResponseHandler;
import com.nexon.common.session.SessionCheck;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/mapi/notice")
public class AnoticeController {

    @Autowired
    private AnoticeService aNoticeService;

    @ApiOperation(value = "공지사항 목록")
    @GetMapping
    public ResponseHandler<?> selectNotice(SelectNoticeReq req) {
        ResponseHandler<?> result = aNoticeService.selectNotice(req);
        return  result;
    }

    @ApiOperation(value = "공지사항 상세")
    @GetMapping("/detail")
    public ResponseHandler<?> selectNoticeDetail(SelectNoticeDetailReq req) {
        ResponseHandler<?> result = aNoticeService.selectNoticeDetail(req);
        return  result;
    }

    @ApiOperation(value = "공지사항 쓰기")
    @PostMapping
    @SessionCheck
    public ResponseHandler<?> insertNotice(InsertNoticeReq req,HttpServletRequest request) {
        ResponseHandler<?> result = aNoticeService.insertNotice(request,req);
        return  result;
    }

    @ApiOperation(value = "공지사항 수정")
    @PostMapping("/edit")
    @SessionCheck
    public ResponseHandler<?> updateNotice(UpdateNoticeReq req,HttpServletRequest request) {
        ResponseHandler<?> result = aNoticeService.updateNotice(request,req);
        return  result;
    }

    @ApiOperation(value = "공지사항 삭제")
    @DeleteMapping
    public ResponseHandler<?> deleteNotice(@RequestBody(required = false) DeleteNoticeReq req) {
        ResponseHandler<?> result = aNoticeService.deleteNotice(req);
        return  result;
    }

    @ApiOperation(value = "이미지 삭제")
    @DeleteMapping("/deleteFile")
    public ResponseHandler<?> deleteFile(@RequestBody(required = false) DeleteNoticeImgReq req) {
        ResponseHandler<?> result = aNoticeService.deleteFile(req);
        return  result;
    }
}
