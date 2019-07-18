package com.nexon.notice.dao;


import com.nexon.notice.dto.model.NoticeDetailPrevNextVO;
import com.nexon.notice.dto.model.NoticeListVO;
import com.nexon.notice.dto.req.PutNoticeReq;
import com.nexon.notice.dto.req.SelectNoticeDetailReq;
import com.nexon.notice.dto.req.SelectNoticeReq;
import com.nexon.notice.dto.res.SelectNoticeDetailRes;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface NoticeDao {

    List<NoticeListVO> selectNotice(SelectNoticeReq req);

    List<NoticeListVO> selectNoticeImportantList();

    int selectNoticeCnt(SelectNoticeReq req);

    SelectNoticeDetailRes selectNoticeDetail(SelectNoticeDetailReq req);

    NoticeDetailPrevNextVO selectPrevNotice(SelectNoticeDetailReq req);

    NoticeDetailPrevNextVO selectNextNotice(SelectNoticeDetailReq req);

    void putViewCnt(PutNoticeReq req);
}
