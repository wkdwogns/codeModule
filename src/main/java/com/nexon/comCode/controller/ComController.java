package com.nexon.comCode.controller;

import com.nexon.comCode.dto.req.ComCodeReq;
import com.nexon.comCode.service.ComService;
import com.nexon.common.dto.res.ResponseHandler;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *  Page 컨틀롤러
 *
 *
 */


@RequestMapping(value="/api/com" )
@RestController
public class ComController {

    @Autowired
    ComService comService;
    /*
	 * 공통코드 조회
	 */
    @ApiOperation(value = "공통코드 조회")
    @GetMapping(value="/code")
    @ResponseBody
    public ResponseHandler<?> selectCode(ComCodeReq req) {
        ResponseHandler<?> result = comService.selectComCodeList(req);
        return  result;
    }
}


