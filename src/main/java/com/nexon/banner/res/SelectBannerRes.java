package com.nexon.banner.res;

import com.nexon.banner.model.BannerListVO;
import lombok.Data;

import java.util.List;

@Data
public class SelectBannerRes {
    List<BannerListVO> list;
}
