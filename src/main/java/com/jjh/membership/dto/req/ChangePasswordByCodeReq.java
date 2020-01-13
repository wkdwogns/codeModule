package com.jjh.membership.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
@ApiModel(value = "인증 코드를 이용한 비밀번호 변경")
public class ChangePasswordByCodeReq {

    @ApiParam(value = "인증코드", format = "password")
    private String verifyCode;

    @ApiParam(value = "신규 비밀번호", format = "password", required = true )
    private String newUserPwd; //  new userPwd

    @ApiParam(value = "유저아이디", format = "password")
    private String userId;
}
