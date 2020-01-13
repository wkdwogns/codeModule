package com.jjh.comCode.dto.req;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "공통코드")
public class ComCodeListReq implements Serializable {
    @ApiParam(value = "공통 마스터 코드", required = true)
    @NotNull
    private List<String> dtlCdList;

    private String mstCd;
}
