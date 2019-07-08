package com.nexon.admin.banner.dao;


import com.nexon.admin.banner.model.UpdateBannerSortVO;
import com.nexon.admin.banner.req.*;
import com.nexon.admin.banner.res.SelectBannerDetailRes;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AdminBannerDao {

    void insertBanner(InsertBannerReq req);

    List selectBanner(SelectBannerReq req);

    int selectBannerCnt(SelectBannerReq req);

    void updateBanner(UpdateBannerReq req);

    void deleteBanner(DeleteBannerReq req);

    SelectBannerDetailRes selectBannerDetail(SelectBannerDetailReq req);

    void updateSortBanner(UpdateBannerSortVO req);

    void allUpdateViewN(UpdateBannerSortReq req);
}
