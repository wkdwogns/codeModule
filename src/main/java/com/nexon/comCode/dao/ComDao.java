package com.nexon.comCode.dao;

import com.nexon.comCode.dto.model.ComCodeVO;
import com.nexon.comCode.dto.req.ComCodeReq;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import javax.validation.Valid;
import java.util.List;

@Repository
@Mapper
public interface ComDao
{
    /* 공통코드 조회 */
    List<ComCodeVO> selectComDtl(@Valid ComCodeReq req);
}
