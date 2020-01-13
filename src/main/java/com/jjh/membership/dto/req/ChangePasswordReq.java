package com.jjh.membership.dto.req;

import com.jjh.common.dto.req.CommonReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
@ApiModel(value = "비밀번호 변경")
public class ChangePasswordReq extends CommonReq {

    @ApiParam(value = "비밀번호", format = "password")
    private String userPwd; //  userPwd

    @ApiParam(value = "신규 비밀번호", format = "password", required = true )
    private String newUserPwd; //  new userPwd
}
