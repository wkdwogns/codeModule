package com.jjh.membership.dto.req;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "회원정보 조회")
public class SessionReq implements Serializable {

    @ApiParam(value = "access token")
    private String accessToken;

}
