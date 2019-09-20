package com.nexon.notice.dto.req;

import lombok.Data;

@Data
public class SelectNoticeDetailReq {
    private Integer seq;
    private String prev;
    private String next;
}
