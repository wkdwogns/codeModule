package com.nexon.membership.dto.req;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value = "로그인 파라미터")
public class LoginReq implements Serializable {

    @ApiParam(value = "아이디", required = true)
    @NotNull
    private String userId;

    @ApiParam(value = "비밀번호", required = true)
    @NotNull
    private String userPwd;

    private String forever;

}
