package com.nexon.banner.dao;


import com.nexon.banner.model.BannerListVO;
import com.nexon.banner.req.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BannerDao {

    List selectBanner(BannerTypeReq req);

    List<BannerListVO> selectPopup();
}
