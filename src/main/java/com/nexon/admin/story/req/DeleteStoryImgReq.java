package com.nexon.admin.story.req;

import lombok.Data;

@Data
public class DeleteStoryImgReq {
    private Integer seq;
    private Integer fileSeq;
    private String type;
}
