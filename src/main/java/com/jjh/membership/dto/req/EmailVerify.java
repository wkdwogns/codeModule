package com.jjh.membership.dto.req;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value = "이메일 인증")
public class EmailVerify implements Serializable {

    @ApiParam(value = "아이디", required = true)
    @NotNull
    private String userId;

    @ApiParam(value = "인증번호", required = true)
    @NotNull
    private String verifyKey;
}
