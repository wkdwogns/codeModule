package com.nexon.admin.banner.res;

import com.nexon.admin.banner.model.BannerListVO;
import lombok.Data;

import java.util.List;

@Data
public class SelectBannerRes {
    List<BannerListVO> list;
    int totalCnt;
}
