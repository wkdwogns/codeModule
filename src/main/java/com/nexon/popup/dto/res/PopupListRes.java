package com.nexon.popup.dto.res;

import com.nexon.popup.dto.model.PopupListVO;
import lombok.Data;

import java.util.List;

@Data
public class PopupListRes {
    List<PopupListVO> list;
    int totalCnt;
}
