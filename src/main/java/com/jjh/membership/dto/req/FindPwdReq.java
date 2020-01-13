package com.jjh.membership.dto.req;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value = "비밀번호 찾기 파라미터")
public class FindPwdReq implements Serializable {
    @ApiParam(value = "아이디", required = true)
    @NotNull
    private String userId;
}
