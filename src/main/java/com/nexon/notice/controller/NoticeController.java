package com.nexon.notice.controller;

import com.nexon.common.dto.res.ResponseHandler;
import com.nexon.notice.dto.req.PutNoticeReq;
import com.nexon.notice.dto.req.SelectNoticeDetailReq;
import com.nexon.notice.dto.req.SelectNoticeReq;
import com.nexon.notice.service.NoticeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @ApiOperation(value = "공지사항 목록")
    @GetMapping
    public ResponseHandler<?> selectNotice(SelectNoticeReq req) {
        ResponseHandler<?> result = noticeService.selectNotice(req);
        return  result;
    }

    @ApiOperation(value = "공지사항 상세")
    @GetMapping("/detail")
    public ResponseHandler<?> selectNoticeDetail(SelectNoticeDetailReq req) {
        ResponseHandler<?> result = noticeService.selectNoticeDetail(req);
        return  result;
    }

    @ApiOperation(value = "공지사항 이전글,다음글")
    @GetMapping("/prevNext")
    public ResponseHandler<?> selectNoticeDetailPrevNext(SelectNoticeDetailReq req) {
        ResponseHandler<?> result = noticeService.selectNoticeDetailPrevNext(req);
        return  result;
    }

    @ApiOperation(value = "조회수")
    @PutMapping("/viewCnt")
    public ResponseHandler<?> putViewCnt(@RequestBody PutNoticeReq req) {
        ResponseHandler<?> result = noticeService.putViewCnt(req);
        return  result;
    }


}
