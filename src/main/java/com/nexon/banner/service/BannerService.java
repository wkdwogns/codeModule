package com.nexon.banner.service;

import com.nexon.banner.dao.BannerDao;
import com.nexon.banner.model.BannerListVO;
import com.nexon.banner.req.*;
import com.nexon.banner.res.SelectBannerRes;
import com.nexon.common.config.ConfigCommon;
import com.nexon.common.dto.res.ResponseHandler;
import com.nexon.common.type.ReturnType;
import com.nexon.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerService {

    @Autowired
    private BannerDao bannerDao;

    @Autowired
    private ConfigCommon configCommon;
    
    public ResponseHandler<SelectBannerRes> selectBanner(BannerTypeReq req) {
        ResponseHandler<SelectBannerRes> result = new ResponseHandler<>();

        try{

            if(CommonUtil.isEmpty(req.getCategory())){
                req.setCategory(configCommon.getDtl().getBannerTop());
            }

            List<BannerListVO> list = bannerDao.selectBanner(req);
            SelectBannerRes res = new SelectBannerRes();
            res.setList(list);
            result.setData(res);
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
        }

        return result;
    }

    public ResponseHandler<SelectBannerRes> selectContentsBanner(BannerTypeReq req) {
        req.setCategory(configCommon.getDtl().getBannerContent());
        return this.selectBanner(req);
    }

    public ResponseHandler<SelectBannerRes> selectPopupBanner() {
        ResponseHandler<SelectBannerRes> result = new ResponseHandler<>();

        try{
            List<BannerListVO> list = bannerDao.selectPopup();
            SelectBannerRes res = new SelectBannerRes();
            res.setList(list);
            result.setData(res);
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
        }

        return result;
    }
}
