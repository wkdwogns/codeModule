package com.nexon.admin.story.service;

import com.nexon.admin.AdminFile.AdminFileService;
import com.nexon.admin.story.dao.AstoryDao;
import com.nexon.admin.story.model.StoryListVO;
import com.nexon.admin.story.req.*;
import com.nexon.admin.story.res.SelectStoryDetailRes;
import com.nexon.admin.story.res.SelectStoryRes;
import com.nexon.common.dto.res.ResponseHandler;
import com.nexon.common.file.config.ConfigFile;
import com.nexon.common.file.dto.req.FileDeleteReq;
import com.nexon.common.file.dto.req.FileListReq;
import com.nexon.common.file.service.FileService;
import com.nexon.common.type.ReturnType;
import com.nexon.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AstoryService {

    @Autowired
    private AstoryDao aStoryDao;
    @Autowired
    ConfigFile configFile;
    @Autowired
    private FileService fileService;
    @Autowired
    private AdminFileService adminFileService;
    
    public ResponseHandler<SelectStoryRes> selectStory(SelectStoryReq req) {
        ResponseHandler<SelectStoryRes> result = new ResponseHandler<>();

        try{
            req.setStartRow();
            List<StoryListVO> list = aStoryDao.selectStory(req);
            int cnt = aStoryDao.selectStoryCnt(req);

            SelectStoryRes res = new SelectStoryRes();
            res.setList(list);
            res.setTotalCnt(cnt);
            res.setThumbnailUrl( configFile.getSubUrlCategory3() );
            result.setData(res);
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
        }

        return result;
    }

    public ResponseHandler<?> insertStory(InsertStoryReq req) {
        ResponseHandler<?> result = new ResponseHandler<>();

        try{

            if( req.getViewYn().equals("Y")
                    && req.getImportantYn().equals("Y")
                    && req.getOrderNo()>4 ){
                result.setMessage("4 이하의 숫자만 입력해주세요.");
                result.setReturnCode(ReturnType.RTN_TYPE_NG);
                return result;
            }

            //대표이미지
            if(req.getImg()!=null) {
                int igs = adminFileService.setImg(req.getImg() , configFile.getSelectCategory4());
                req.setImgGrpSeq(igs);
            }

            if(req.getFile()!=null) {
                int fgs = adminFileService.setImg(req.getFile() , configFile.getSelectCategory4());
                req.setFileGrpSeq(fgs);
            }

            if(CommonUtil.isNotEmpty(req.getEditorDelImg())){
                adminFileService.setDeleteFileByEditor(req.getEditorDelImg(),configFile.getSelectCategory5());
            }

            aStoryDao.insertStory(req);

            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
        }

        return result;
    }

    public ResponseHandler<?> updateStory(UpdateStoryReq req) {
        ResponseHandler<?> result = new ResponseHandler<>();

        try{

            if( req.getViewYn().equals("Y")
                    && req.getImportantYn().equals("Y")
                    && req.getOrderNo()>4 ){
                result.setMessage("4 이하의 숫자만 입력해주세요.");
                result.setReturnCode(ReturnType.RTN_TYPE_NG);
                return result;
            }

            if(req.getImg()!=null) {
                if(req.getImgSeq()!=null){
                    FileDeleteReq fr = adminFileService.setDeleteFile(req.getImgSeq(),configFile.getSelectCategory4());
                    fileService.deleteFiles(fr);
                }
                int igs = adminFileService.setImg(req.getImg() , configFile.getSelectCategory4());
                req.setImgGrpSeq(igs);
            }

            if(req.getFile()!=null) {
                if(req.getAttachSeq()!=null){
                    FileDeleteReq fr = adminFileService.setDeleteFile(req.getAttachSeq(),configFile.getSelectCategory4());
                    fileService.deleteFiles(fr);
                }
                int fgs = adminFileService.setImg(req.getFile() , configFile.getSelectCategory4());
                req.setFileGrpSeq(fgs);
            }

            if(CommonUtil.isNotEmpty(req.getEditorDelImg())){
                adminFileService.setDeleteFileByEditor(req.getEditorDelImg(),configFile.getSelectCategory5());
            }

            aStoryDao.updateStory(req);

            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
        }

        return result;
    }

    public ResponseHandler<?> deleteStory(DeleteStoryReq req) {
        ResponseHandler<?> result = new ResponseHandler<>();

        try{

            aStoryDao.deleteStory(req);

            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
        }

        return result;
    }

    public ResponseHandler<SelectStoryDetailRes> selectStoryDetail(SelectStoryDetailReq req) {
        ResponseHandler<SelectStoryDetailRes> result = new ResponseHandler<>();

        try{
            SelectStoryDetailRes res = aStoryDao.selectStoryDetail(req);

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

    private boolean checkStoryViewCnt() {
        boolean check = false;
        int cnt = aStoryDao.selectViewLimitCnt();
        if(cnt >= 15) {
            check = true;
        }

        return check;
    }

    public ResponseHandler<?> deleteFile(DeleteStoryImgReq req) {
        ResponseHandler<?> result = new ResponseHandler<>();

        try{

            FileDeleteReq fr = adminFileService.setDeleteFile(req.getFileSeq(),configFile.getSelectCategory4());
            fileService.deleteFiles(fr);

            aStoryDao.deleteFile(req);

            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
        }

        return result;
    }

}
