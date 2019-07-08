package com.nexon.admin.popup.dao;


import com.nexon.admin.popup.dto.model.PopupDtlVO;
import com.nexon.admin.popup.dto.model.PopupListVO;
import com.nexon.admin.popup.dto.req.InsertPopupReq;
import com.nexon.admin.popup.dto.req.PopupDtlReq;
import com.nexon.admin.popup.dto.req.SelectPopupListReq;
import com.nexon.admin.popup.dto.req.UpdatePopupReq;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AdminPopupDao {

    List<PopupListVO> selectPopupList(SelectPopupListReq req);

    int selectPopupListCnt(SelectPopupListReq req);

    PopupDtlVO selectPopupDetail(PopupDtlReq req);

    void insertPopup(InsertPopupReq req);

    void updatePopup(UpdatePopupReq req);

    void deletePooup(PopupDtlReq req);

    int selectPopupViewCnt();


}
