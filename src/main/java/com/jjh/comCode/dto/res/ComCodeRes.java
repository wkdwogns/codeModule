package com.jjh.comCode.dto.res;

import com.jjh.comCode.dto.model.ComCodeVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "공통코드")
public class ComCodeRes implements Serializable {
    private List<ComCodeVO> comCodeList;
}
