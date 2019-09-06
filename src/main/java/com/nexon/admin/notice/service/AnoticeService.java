package com.nexon.admin.notice.service;

import com.nexon.admin.AdminFile.AdminFileService;
import com.nexon.admin.notice.dao.AdminNoticeDao;
import com.nexon.admin.notice.model.NoticeListVO;
import com.nexon.admin.notice.req.*;
import com.nexon.admin.notice.res.SelectNoticeDetailRes;
import com.nexon.admin.notice.res.SelectNoticeRes;
import com.nexon.common.dto.res.ResponseHandler;
import com.nexon.common.file.config.ConfigFile;
import com.nexon.common.file.dto.req.FileDeleteReq;
import com.nexon.common.file.dto.req.FileListReq;
import com.nexon.common.file.service.FileService;
import com.nexon.common.type.ReturnType;
import com.nexon.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class AnoticeService {

    @Autowired
    private AdminNoticeDao adminNoticeDao;
    @Autowired
    private AdminFileService adminFileService;
    @Autowired
    private ConfigFile configFile;
    @Autowired
    private FileService fileService;
    
    public ResponseHandler<SelectNoticeRes> selectNotice(SelectNoticeReq req) {
        ResponseHandler<SelectNoticeRes> result = new ResponseHandler<>();

        try{
            req.setStartRow();
            List<NoticeListVO> list = adminNoticeDao.selectNotice(req);
            int cnt = adminNoticeDao.selectNoticeCnt(req);

            SelectNoticeRes res = new SelectNoticeRes();
            res.setList(list);
            res.setTotalCnt(cnt);

            result.setData(res);
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
        }

        return result;
    }

    public ResponseHandler<?> insertNotice(HttpServletRequest request, InsertNoticeReq req) {
        ResponseHandler<?> result = new ResponseHandler<>();

        try{

            //중요 여부 2개 제한
//            if(checkNoticeViewCnt() && "Y".equals(req.getViewYn()) && "Y".equals(req.getImportantYn())) {
//                result.setReturnCode(ReturnType.RTN_TYPE_DATA_EXISTS);
//                return result;
//            }

            if(req.getImg()!=null) {
                int igs = adminFileService.setImg(req.getImg() , configFile.getSelectCategory3());
                req.setImgGrpSeq(igs);
            }

            if(req.getFile()!=null) {
                int fgs = adminFileService.setImg(req.getFile() , configFile.getSelectCategory3());
                req.setFileGrpSeq(fgs);
            }

            if(CommonUtil.isNotEmpty(req.getEditorDelImg())){
                adminFileService.setDeleteFileByEditor(req.getEditorDelImg(),configFile.getSelectCategory3());
            }

            adminNoticeDao.insertNotice(req);
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
        }

        return result;
    }

    public ResponseHandler<?> updateNotice(HttpServletRequest request,UpdateNoticeReq req) {
        ResponseHandler<?> result = new ResponseHandler<>();

        try{
/*
            SelectNoticeDetailReq dtlReq = new SelectNoticeDetailReq();
            dtlReq.setSeq(req.getSeq());
            SelectNoticeDetailRes res = adminNoticeDao.selectNoticeDetail(dtlReq);

            //중요 여부 2개 제한 : 노출 2개이고 파라미터 및 상세데이터 체크
            if(checkNoticeViewCnt() && "Y".equals(req.getViewYn()) && "Y".equals(req.getImportantYn()) && !req.getViewYn().equals(res.getViewYn())) {
                result.setReturnCode(ReturnType.RTN_TYPE_DATA_EXISTS);
                return result;
            }*/

            if(req.getImg()!=null) {
                if(req.getImgSeq()!=null){
                    FileDeleteReq fr = adminFileService.setDeleteFile(req.getImgSeq(),configFile.getSelectCategory3());
                    fileService.deleteFiles(fr);
                }
                int igs = adminFileService.setImg(req.getImg() , configFile.getSelectCategory3());
                req.setImgGrpSeq(igs);
            }

            if(req.getFile()!=null) {
                if(req.getAttachSeq()!=null){
                    FileDeleteReq fr = adminFileService.setDeleteFile(req.getAttachSeq(),configFile.getSelectCategory3());
                    fileService.deleteFiles(fr);
                }
                int fgs = adminFileService.setImg(req.getFile() , configFile.getSelectCategory3());
                req.setFileGrpSeq(fgs);
            }

            if(CommonUtil.isNotEmpty(req.getEditorDelImg())){
                adminFileService.setDeleteFileByEditor(req.getEditorDelImg(),configFile.getSelectCategory3());
            }

            adminNoticeDao.updateNotice(req);

            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
        }

        return result;
    }

    public ResponseHandler<?> deleteNotice(DeleteNoticeReq req) {
        ResponseHandler<?> result = new ResponseHandler<>();

        try{

            adminNoticeDao.deleteNotice(req);

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

            SelectNoticeDetailRes res = adminNoticeDao.selectNoticeDetail(req);

            Integer fgs= res.getFileGrpSeq();
            if(fgs!=null){
                FileListReq fReq = new FileListReq();
                fReq.setFileGrpSeq(fgs);
                List fList = fileService.getFileList(fReq);
                res.setFList(fList);
            }
            Integer igs = res.getImgGrpSeq();
            if(igs!=null){
                FileListReq fReq = new FileListReq();
                fReq.setFileGrpSeq(igs);
                List fList = fileService.getFileList(fReq);
                res.setIList(fList);
            }

            result.setData(res);
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
        }

        return result;
    }

    private boolean checkNoticeViewCnt() {
        boolean check = false;
        int cnt = adminNoticeDao.selectViewLimitCnt();
        if(cnt >= 2) {
            check = true;
        }

        return check;
    }

    public ResponseHandler<?> deleteFile(DeleteNoticeImgReq req) {
        ResponseHandler<?> result = new ResponseHandler<>();

        try{
            FileDeleteReq fr = adminFileService.setDeleteFile(req.getFileSeq(),configFile.getSelectCategory3());
            fileService.deleteFiles(fr);

            adminNoticeDao.deleteFile(req);

            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
        }

        return result;
    }
}
