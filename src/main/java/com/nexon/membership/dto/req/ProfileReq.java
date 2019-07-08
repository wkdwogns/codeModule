package com.nexon.membership.dto.req;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "회원가입 파라미터")
public class ProfileReq implements Serializable {

    @ApiParam(value = "회원 시퀀스", hidden = true)
    private String userSeq;

    @ApiParam(value = "아이디", required = true)
    private String userId;

    @ApiParam(value = "닉네임")
    private String nickName;

    @ApiParam(value = "유저이름")
    private String userName;

    @ApiParam(value = "소개")
    private String introduction;

    @ApiParam(value = "홈페이지 링크")
    private String homepageLink;

    @ApiParam(value = "마케팅광고수신여부")
    private String newsletterYn;
}
