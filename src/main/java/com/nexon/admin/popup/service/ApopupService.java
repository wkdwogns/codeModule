package com.nexon.admin.popup.service;

import com.nexon.admin.AdminFile.AdminFileService;
import com.nexon.admin.popup.dao.AdminPopupDao;
import com.nexon.admin.popup.dto.model.PopupDtlVO;
import com.nexon.admin.popup.dto.model.PopupListVO;
import com.nexon.admin.popup.dto.req.InsertPopupReq;
import com.nexon.admin.popup.dto.req.PopupDtlReq;
import com.nexon.admin.popup.dto.req.SelectPopupListReq;
import com.nexon.admin.popup.dto.req.UpdatePopupReq;
import com.nexon.admin.popup.dto.res.PopupDtlRes;
import com.nexon.admin.popup.dto.res.PopupListRes;
import com.nexon.common.dto.res.ResponseHandler;
import com.nexon.common.file.config.ConfigFile;
import com.nexon.common.file.dto.req.FileDeleteReq;
import com.nexon.common.file.dto.req.FileListReq;
import com.nexon.common.file.service.FileService;
import com.nexon.common.type.ReturnType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApopupService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdminPopupDao adminPopupDao;

    @Autowired
    private AdminFileService adminFileService;

    @Autowired
    private ConfigFile configFile;

    @Autowired
    private FileService fileService;
    
    public ResponseHandler<PopupListRes> selectPopupList(SelectPopupListReq req) {
        ResponseHandler<PopupListRes> result = new ResponseHandler<>();

        try{
            logger.info("selectPopupList[SelectPopupListReq]", req);
            req.setStartRow();
            List<PopupListVO> list = adminPopupDao.selectPopupList(req);
            int cnt = adminPopupDao.selectPopupListCnt(req);
            if(cnt == 0) {
                result.setReturnCode(ReturnType.RTN_TYPE_NO_DATA);
                return result;
            }

            PopupListRes res = new PopupListRes();
            res.setList(list);
            res.setTotalCnt(cnt);

            result.setData(res);
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
            logger.info("selectPopupList[result]", result);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            logger.error("selectPopupList[Exception]", e);
        }

        return result;
    }

    public ResponseHandler<?> insertPopup(InsertPopupReq req) {
        ResponseHandler<?> result = new ResponseHandler<>();

        try{
            logger.info("insertPopup[InsertPopupReq]", req);

            if(checkPopupViewCnt() && "Y".equals(req.getViewYn())) {
                result.setReturnCode(ReturnType.RTN_TYPE_DATA_EXISTS);
                return result;
            }

            if(req.getFile() != null) {
                int fgs = adminFileService.setImg(req.getFile() , configFile.getSelectCategory2());
                if(fgs == 0) {
                    result.setReturnCode(ReturnType.RTN_TYPE_FILE_EXTENSION_NG);
                    return result;
                }
                req.setFileGrpSeq(fgs);
            }

            adminPopupDao.insertPopup(req);

            result.setReturnCode(ReturnType.RTN_TYPE_OK);
            logger.info("insertPopup[result]", result);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            logger.error("insertPopup[Exception]", e);
        }

        return result;
    }

    public ResponseHandler<?> updatePopup(UpdatePopupReq req) {
        ResponseHandler<?> result = new ResponseHandler<>();

        try{
            logger.info("updateNotice[UpdatePopupReq]", req);

            PopupDtlReq dtlReq = new PopupDtlReq();
            dtlReq.setPopupSeq(req.getPopupSeq());
            PopupDtlVO vo = adminPopupDao.selectPopupDetail(dtlReq);

            //노출 3개이고 파라미터 및 상세데이터 체크
            if(checkPopupViewCnt() && "Y".equals(req.getViewYn()) && !req.getViewYn().equals(vo.getViewYn())) {
                result.setReturnCode(ReturnType.RTN_TYPE_DATA_EXISTS);
                return result;
            }

            if(req.getFile()!=null) {
                int fgs = adminFileService.setImg(req.getFile() , configFile.getSelectCategory2());
                if(fgs == 0) {
                    result.setReturnCode(ReturnType.RTN_TYPE_FILE_EXTENSION_NG);
                    return result;
                }
                if(req.getAttachSeq() != null){
                    FileDeleteReq fr = adminFileService.setDeleteFile(req.getAttachSeq(),configFile.getSelectCategory2());
                    fileService.deleteFiles(fr);
                }
                req.setFileGrpSeq(fgs);
            }

            adminPopupDao.updatePopup(req);

            result.setReturnCode(ReturnType.RTN_TYPE_OK);
            logger.info("updateNotice[result]", result);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            logger.error("insertPopup[Exception]", e);
        }

        return result;
    }

    public ResponseHandler<?> deletePooup(PopupDtlReq req) {
        ResponseHandler<?> result = new ResponseHandler<>();

        try{
            logger.info("deletePooup[PopupDtlReq]", req);

            adminPopupDao.deletePooup(req);

            result.setReturnCode(ReturnType.RTN_TYPE_OK);
            logger.info("deletePooup[result]", result);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            logger.error("insertPopup[Exception]", e);
        }

        return result;
    }

    public ResponseHandler<PopupDtlRes> selectPopupDetail(PopupDtlReq req) {
        ResponseHandler<PopupDtlRes> result = new ResponseHandler<>();

        try{
            logger.info("selectPopupDetail[PopupDtlReq]", req);
            PopupDtlRes res = new PopupDtlRes();

            PopupDtlVO vo = adminPopupDao.selectPopupDetail(req);

            Integer fgs= vo.getFileGrpSeq();
            if(fgs != null){
                FileListReq fReq = new FileListReq();
                fReq.setFileGrpSeq(fgs);
                List fList = fileService.getFileList(fReq);
                res.setFList(fList);
            }
            res.setPopup(vo);

            result.setData(res);
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
            logger.info("selectPopupDetail[result]", result);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            logger.error("selectPopupDetail[Exception]", e);
        }

        return result;
    }

    private boolean checkPopupViewCnt() {
        boolean check = false;
        int cnt = adminPopupDao.selectPopupViewCnt();
        if(cnt >= 3) {
            check = true;
        }

        return check;
    }
}
