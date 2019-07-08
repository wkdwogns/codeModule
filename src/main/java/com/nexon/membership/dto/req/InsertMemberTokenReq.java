package com.nexon.membership.dto.req;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value = "회원 토큰")
public class InsertMemberTokenReq implements Serializable {

    @ApiParam(value = "회원번호", required = true)
    @NotNull
    private Integer userSeq;

    @ApiParam(value = "리프레쉬토큰", required = true)
    @NotNull
    private String refreshToken;

    private Integer creId;

}
