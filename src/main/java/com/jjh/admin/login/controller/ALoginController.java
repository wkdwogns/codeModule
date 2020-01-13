package com.jjh.admin.login.controller;

import com.jjh.common.dto.res.ResponseHandler;
import com.jjh.membership.dto.req.LoginReq;
import com.jjh.membership.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
public class ALoginController {

    @Autowired
    LoginService loginService;

    @PostMapping(value = "/mapi/login")
	public ResponseHandler loginOauth(@Valid @RequestBody(required = false) final LoginReq req, HttpServletResponse response) {
		ResponseHandler result = loginService.loginOauth(req, response);

		return result;
	}

	@PostMapping(value = "/mapi/logout")
	public ResponseHandler logoutOauth(HttpServletResponse response) {
		ResponseHandler result = loginService.logoutOauth(response);

		return result;
	}

}
