package com.nexon.admin.notice.dao;


import com.nexon.admin.notice.req.*;
import com.nexon.admin.notice.res.SelectNoticeDetailRes;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AdminNoticeDao {

    void insertNotice(InsertNoticeReq req);

    List selectNotice(SelectNoticeReq req);

    int selectNoticeCnt(SelectNoticeReq req);

    void updateNotice(UpdateNoticeReq req);

    void deleteNotice(DeleteNoticeReq req);

    SelectNoticeDetailRes selectNoticeDetail(SelectNoticeDetailReq req);

    int selectViewLimitCnt();
}
