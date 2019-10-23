package com.nexon.admin.story.req;

import lombok.Data;

@Data
public class DeleteStoryImgReq {
    public DeleteStoryImgReq(){}
    public DeleteStoryImgReq(Integer fileSeq,String type){
        this.fileSeq=fileSeq;
        this.type=type;
    }
    private Integer seq;
    private Integer fileSeq;
    private String type;
}
