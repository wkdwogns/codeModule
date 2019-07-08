package com.nexon.notice.dto.res;

import com.nexon.notice.dto.model.NoticeListVO;
import lombok.Data;

import java.util.List;

@Data
public class SelectNoticeRes {
    List<NoticeListVO> list;
    int totalCnt;
}
