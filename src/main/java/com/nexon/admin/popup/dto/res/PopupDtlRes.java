package com.nexon.admin.popup.dto.res;

import com.nexon.admin.popup.dto.model.PopupDtlVO;
import com.nexon.common.file.dto.model.FileInfo;
import lombok.Data;

import java.util.List;

@Data
public class PopupDtlRes {
    PopupDtlVO popup;
    private List<FileInfo> fList;
}
