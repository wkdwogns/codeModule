package com.nexon.comCode.service;


import com.nexon.comCode.dao.ComDao;
import com.nexon.comCode.dto.model.ComCodeVO;
import com.nexon.comCode.dto.req.ComCodeReq;
import com.nexon.comCode.dto.res.ComCodeRes;
import com.nexon.common.dto.res.ResponseHandler;
import com.nexon.common.type.ReturnType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

/**
 *  LoginService를 위한 IF Class
 *  Spring Security사용을 위해 UserDetailsService를 Override해야 한다.
 *
 *
 */
@Service
public class ComService {

    @Autowired
    ComDao comDao;

    /* 공통코드 조회 */
    public ResponseHandler<?> selectComCodeList(@Valid ComCodeReq req) {
        final ResponseHandler<ComCodeRes> result = new ResponseHandler<>();
        try {
            ComCodeRes comCodeRes = new ComCodeRes();
            List<ComCodeVO> comCodeList = comDao.selectComDtl(req);
            comCodeRes.setComCodeList(comCodeList);
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
            result.setData(comCodeRes);
        } catch (Exception e){
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
        }
        return result;
    }


    /* 공통코드 조회 */
    public ResponseHandler<?> selectCategory(String[] mstCd) {
        ComCodeReq comCodeReq = new ComCodeReq();
        comCodeReq.setMstCdArr(mstCd);
        return this.selectComCodeList(comCodeReq);
    }
}
