package com.jjh.comCode.dto.model;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "공통코드")
public class ComCodeVO implements Serializable {
    private String mstCd;
    private String dtlCd;
    private String dtlNm;
    private String useVal1;
    private String useVal2;
}
