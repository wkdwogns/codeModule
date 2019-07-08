package com.nexon.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  Page 컨틀롤러
 *
 *
 */


@RestController
public class HealthCheckController {

    @GetMapping("/health/check")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(200);

        return "200";
    }

}


