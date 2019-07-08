package com.nexon.admin.popup.dto.req;

import com.nexon.common.dto.req.CommonReq;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdatePopupReq extends CommonReq {

    private Integer popupSeq;
    private String popupNm;
    private String viewYn;
    private String viewStDt;
    private String viewEndDt;
    private String viewUnlimitYn;
    private String targetUrl;
    private String targetYn;
    private Integer fileGrpSeq;
    private Integer attach;
    private Integer attachSeq;
    private MultipartFile file;

}
