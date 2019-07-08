package com.nexon.notice.dto.res;

import com.nexon.notice.dto.model.NoticeDetailPrevNextVO;
import lombok.Data;

@Data
public class SelectNoticeDetailPrevNextRes {
    private NoticeDetailPrevNextVO prevNotice;
    private NoticeDetailPrevNextVO nextNotice;
}
