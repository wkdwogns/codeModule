package com.nexon.popup.dao;


import com.nexon.popup.dto.model.PopupListVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PopupDao {

    List<PopupListVO> selectPopupList();

}
