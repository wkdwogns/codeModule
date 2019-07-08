package com.nexon.popup.service;

import com.nexon.common.dto.res.ResponseHandler;
import com.nexon.common.type.ReturnType;
import com.nexon.popup.dao.PopupDao;
import com.nexon.popup.dto.model.PopupListVO;
import com.nexon.popup.dto.res.PopupListRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PopupService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PopupDao popupDao;

    public ResponseHandler<PopupListRes> selectPopupList() {
        ResponseHandler<PopupListRes> result = new ResponseHandler<>();

        try{
            List<PopupListVO> list = popupDao.selectPopupList();

            PopupListRes res = new PopupListRes();
            res.setList(list);

            result.setData(res);
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
            logger.info("selectPopupList[result]", result);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            logger.error("selectPopupList[Exception]", e);
        }

        return result;
    }

}
