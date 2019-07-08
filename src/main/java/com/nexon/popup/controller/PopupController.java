package com.nexon.popup.controller;

import com.nexon.common.dto.res.ResponseHandler;
import com.nexon.popup.service.PopupService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/popup")
public class PopupController {

    @Autowired
    private PopupService popupService;

    @ApiOperation(value = "팝업 목록")
    @GetMapping
    public ResponseHandler<?> selectNotice() {
        ResponseHandler<?> result = popupService.selectPopupList();
        return  result;
    }

}
