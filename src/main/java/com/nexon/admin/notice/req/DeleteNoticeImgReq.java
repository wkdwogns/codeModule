package com.nexon.admin.notice.req;

import lombok.Data;

@Data
public class DeleteNoticeImgReq {
    public DeleteNoticeImgReq(){}
    public DeleteNoticeImgReq(Integer fileSeq,String type){
        this.fileSeq=fileSeq;
        this.type=type;
    }

    private Integer seq;
    private Integer fileSeq;
    private String type;
}
