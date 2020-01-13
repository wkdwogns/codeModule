package com.jjh.common.controller;

import com.jjh.common.dto.req.LangReq;
import com.jjh.common.dto.res.ResponseHandler;
import com.jjh.common.type.ReturnType;
import com.jjh.common.util.CookieUtil;
import com.jjh.membership.config.ConfigMembership;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 *  언어 설정 컨틀롤러
 *
 *
 */


@RestController
@RequestMapping("/api/lang")
public class LangSetController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConfigMembership configMembership;

    @ApiOperation(value = "언어 쿠키 설정")
    @PostMapping
    public ResponseHandler<?> selectQna(@Valid @RequestBody LangReq req, HttpServletResponse response) {
        ResponseHandler<?> result = new ResponseHandler<>();

        CookieUtil.setCookie(response, "lang", req.getLang(), configMembership.getCookie().getDomain(), -1);
        result.setReturnCode(ReturnType.RTN_TYPE_OK);
        return  result;
    }

}


