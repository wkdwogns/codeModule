package com.nexon.common.mail.dto.res;

import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
public class MailReq {
    @ApiParam(value = "메일 받는사람")
    private String toEmail;

    @ApiParam(value = "메일 제목")
    private String mailTitle;

    @ApiParam(value = "메일템플릿 path")
    private String velocityPath;
}
