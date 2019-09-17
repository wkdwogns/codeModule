package com.nexon.notice.service;

import com.nexon.common.dto.res.ResponseHandler;
import com.nexon.common.file.config.ConfigFile;
import com.nexon.common.file.dto.req.FileListReq;
import com.nexon.common.file.service.FileService;
import com.nexon.common.type.ReturnType;
import com.nexon.notice.dao.NoticeDao;
import com.nexon.notice.dto.model.NoticeDetailPrevNextVO;
import com.nexon.notice.dto.model.NoticeListVO;
import com.nexon.notice.dto.req.PutNoticeReq;
import com.nexon.notice.dto.req.SelectNoticeDetailReq;
import com.nexon.notice.dto.req.SelectNoticeReq;
import com.nexon.notice.dto.res.SelectNoticeDetailPrevNextRes;
import com.nexon.notice.dto.res.SelectNoticeDetailRes;
import com.nexon.notice.dto.res.SelectNoticeRes;
import com.nexon.story.dao.StoryDao;
import com.nexon.story.dto.req.StoryListReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoticeService {

    @Autowired
    private NoticeDao noticeDao;

    @Autowired
    private StoryDao storyDao;

    @Autowired
    private FileService fileService;
    
    public ResponseHandler<SelectNoticeRes> selectNotice(SelectNoticeReq req) {
        ResponseHandler<SelectNoticeRes> result = new ResponseHandler<>();

        try{

            req.setStartRow();
            List<NoticeListVO> list = noticeDao.selectNotice(req);
            int totalCnt = noticeDao.selectNoticeCnt(req);
            int storyCnt = storyDao.selectStoryListCnt(new StoryListReq());

            SelectNoticeRes res = new SelectNoticeRes();
            res.setList(list);
            res.setTotalCnt(totalCnt);
            res.setStoryCnt(storyCnt);

            result.setData(res);
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
        }

        return result;
    }


    public ResponseHandler<SelectNoticeDetailRes> selectNoticeDetail(SelectNoticeDetailReq req) {
        ResponseHandler<SelectNoticeDetailRes> result = new ResponseHandler<>();

        try{
            SelectNoticeDetailRes res = noticeDao.selectNoticeDetail(req);

            NoticeDetailPrevNextVO prevVo = noticeDao.selectPrevNotice(req);
            NoticeDetailPrevNextVO nextVo = noticeDao.selectNextNotice(req);

            res.setPrevNotice(prevVo);
            res.setNextNotice(nextVo);

            result.setData(res);
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
        }

        return result;
    }

    public ResponseHandler<SelectNoticeDetailPrevNextRes> selectNoticeDetailPrevNext(SelectNoticeDetailReq req) {
        ResponseHandler<SelectNoticeDetailPrevNextRes> result = new ResponseHandler<>();

        try{
            SelectNoticeDetailPrevNextRes res = new SelectNoticeDetailPrevNextRes();
            NoticeDetailPrevNextVO prevVo = noticeDao.selectPrevNotice(req);
            NoticeDetailPrevNextVO nextVo = noticeDao.selectNextNotice(req);

            res.setPrevNotice(prevVo);
            res.setNextNotice(nextVo);

            result.setData(res);
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
        }

        return result;
    }

    public ResponseHandler<?> putViewCnt(PutNoticeReq req) {
        ResponseHandler<SelectNoticeDetailPrevNextRes> result = new ResponseHandler<>();

        try{

            noticeDao.putViewCnt(req);
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
        }

        return result;
    }
}
