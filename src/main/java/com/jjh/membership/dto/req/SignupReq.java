package com.jjh.membership.dto.req;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value = "회원가입 파라미터")
public class SignupReq implements Serializable {

    @ApiParam(value = "회원 시퀀스", hidden = true)
    @Ignore
    private String userSeq;

    @ApiParam(value = "아이디", required = true)
    @NotNull
    private String userId;

    @ApiParam(value = "비밀번호", required = true)
    @NotNull
    private String userPwd;

    @ApiParam(value = "회원 관리 번호", hidden = true)
    @Ignore
    private String mnNo;

    @ApiParam(value = "이메일")
    private String email;

    @ApiParam(value = "권한")
    private int authority;

    @ApiParam(value = "닉네임")
    private String nickName;

    @ApiParam(value = "이름")
    private String userName;

    @ApiParam(value = "프로필 이미지 파일 그룹 시퀀스")
    private int fileGrpSeq;

    @ApiParam(value = "내 소개")
    private String introduction;

    @ApiParam(value = "홈페이지 링크")
    private String homepageLink;

    @ApiParam(value = "뉴스레터 여부")
    private String newsletterYn;
}
