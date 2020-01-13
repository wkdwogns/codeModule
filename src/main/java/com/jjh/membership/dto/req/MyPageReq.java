package com.jjh.membership.dto.req;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "회원가입 파라미터")
public class MyPageReq implements Serializable {

    @ApiParam(value = "회원 시퀀스", hidden = true)
    private String userSeq;

    @ApiParam(value = "아이디", required = true)
    private String userId;
}
