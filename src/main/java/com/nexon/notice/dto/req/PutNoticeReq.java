package com.nexon.notice.dto.req;

import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
public class PutNoticeReq {

    @ApiParam(value = "글번호", required = true)
    private int no;

}
