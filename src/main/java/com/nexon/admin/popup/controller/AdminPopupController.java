package com.nexon.admin.popup.controller;

import com.nexon.admin.popup.dto.req.InsertPopupReq;
import com.nexon.admin.popup.dto.req.PopupDtlReq;
import com.nexon.admin.popup.dto.req.SelectPopupListReq;
import com.nexon.admin.popup.dto.req.UpdatePopupReq;
import com.nexon.admin.popup.service.ApopupService;
import com.nexon.common.dto.res.ResponseHandler;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mapi/popup")
public class AdminPopupController {

    @Autowired
    private ApopupService apopupService;

    @ApiOperation(value = "팝업 목록")
    @GetMapping
    public ResponseHandler<?> selectNotice(SelectPopupListReq req) {
        ResponseHandler<?> result = apopupService.selectPopupList(req);
        return  result;
    }

    @ApiOperation(value = "팝업 상세")
    @GetMapping("/detail")
    public ResponseHandler<?> selectNoticeDetail(PopupDtlReq req) {
        ResponseHandler<?> result = apopupService.selectPopupDetail(req);
        return  result;
    }

    @ApiOperation(value = "팝업 쓰기")
    @PostMapping
    public ResponseHandler<?> insertNotice(InsertPopupReq req) {
        ResponseHandler<?> result = apopupService.insertPopup(req);
        return  result;
    }

    @ApiOperation(value = "팝업 수정")
    @PostMapping("/edit")
    public ResponseHandler<?> updateNotice(UpdatePopupReq req) {
        ResponseHandler<?> result = apopupService.updatePopup(req);
        return  result;
    }

    @ApiOperation(value = "팝업 삭제")
    @DeleteMapping
    public ResponseHandler<?> deleteNotice(@RequestBody(required = false) PopupDtlReq req) {
        ResponseHandler<?> result = apopupService.deletePooup(req);
        return  result;
    }
}
