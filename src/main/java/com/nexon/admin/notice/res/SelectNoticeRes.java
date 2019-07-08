package com.nexon.admin.notice.res;

import com.nexon.admin.notice.model.NoticeListVO;
import lombok.Data;

import java.util.List;

@Data
public class SelectNoticeRes {
    List<NoticeListVO> list;
    int totalCnt;
}
