package com.nexon.notice.service;

import com.nexon.common.dto.res.ResponseHandler;
import com.nexon.common.file.config.ConfigFile;
import com.nexon.common.file.dto.req.FileListReq;
import com.nexon.common.file.service.FileService;
import com.nexon.common.type.ReturnType;
import com.nexon.notice.dao.NoticeDao;
import com.nexon.notice.dto.model.NoticeDetailPrevNextVO;
import com.nexon.notice.dto.model.NoticeListVO;
import com.nexon.notice.dto.req.SelectNoticeDetailReq;
import com.nexon.notice.dto.req.SelectNoticeReq;
import com.nexon.notice.dto.res.SelectNoticeDetailPrevNextRes;
import com.nexon.notice.dto.res.SelectNoticeDetailRes;
import com.nexon.notice.dto.res.SelectNoticeRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoticeService {

    @Autowired
    private NoticeDao noticeDao;

    @Autowired
    private ConfigFile configFile;

    @Autowired
    private FileService fileService;
    
    public ResponseHandler<SelectNoticeRes> selectNotice(SelectNoticeReq req) {
        ResponseHandler<SelectNoticeRes> result = new ResponseHandler<>();

        try{
            int impCnt = 0;
            int dataPerPage = req.getCntPerPage();
            List<NoticeListVO> impList = noticeDao.selectNoticeImportantList();
            if(impList != null && impList.size() > 0) {
                impCnt = impList.size();
            }

            //중요게시물을 제외한 한페이지에 보여지는 글 개수
            req.setCntPerPage(dataPerPage - impCnt);
            req.setStartRow();

            List<NoticeListVO> list = noticeDao.selectNotice(req);
            int cnt = noticeDao.selectNoticeCnt(req);

            //중요게시물을 제외한 전체 페이지 개수
            int pageCnt = 0;
            if(cnt > 0) {
                pageCnt = cnt % req.getCntPerPage();
                if(pageCnt == 0) {
                    pageCnt = cnt / req.getCntPerPage();
                } else {
                    pageCnt = (cnt / req.getCntPerPage()) + 1;
                }
            }

            //중요게시물을 포함한 전체 글 수
            int totalCnt = 0;
            if(pageCnt > 0) {
                totalCnt = cnt + (impCnt * pageCnt);
            } else {
                totalCnt = cnt + impCnt;
            }
            List<NoticeListVO> totalList = new ArrayList<>();
            totalList.addAll(impList);
            totalList.addAll(list);

            //페이지 번호 지정
            if(totalList != null && totalList.size() > 0) {
                int idx = (req.getCurrentPage() - 1) * dataPerPage + 1;
                for(int i=0; i<totalList.size(); i++) {
                    totalList.get(i).setNo(idx);
                    idx++;
                }
            }

            SelectNoticeRes res = new SelectNoticeRes();
            //res.setList(list);
            //res.setTotalCnt(cnt);
            res.setList(totalList);
            res.setTotalCnt(totalCnt);

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

            Integer fgs= res.getFileGrpSeq();
            if(fgs!=null){
                FileListReq fReq = new FileListReq();
                fReq.setFileGrpSeq(fgs);
                List fList = fileService.getFileList(fReq);
                res.setFList(fList);
            }

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
}
