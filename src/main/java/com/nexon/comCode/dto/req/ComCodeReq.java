package com.nexon.comCode.dto.req;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value = "공통코드")
public class ComCodeReq implements Serializable {
    @ApiParam(value = "공통 마스터 코드", required = true)
    @NotNull
    private String[] mstCdArr;
}
