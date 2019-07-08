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

import javax.servlet.http.HttpServletRequest;
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

    public ResponseHandler<?> insertStory(HttpServletRequest request, InsertStoryReq req) {
        ResponseHandler<?> result = new ResponseHandler<>();

        try{
            //중요 여부 15개 제한
            if(checkStoryViewCnt() && "Y".equals(req.getViewYn())) {
                result.setReturnCode(ReturnType.RTN_TYPE_DATA_EXISTS);
                return result;
            }

            //파일 업로드
            if(req.getImage()!=null) {
                int fgs = adminFileService.setImg(req.getImage() , configFile.getSelectCategory4());
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

    public ResponseHandler<?> updateStory(HttpServletRequest request,UpdateStoryReq req) {
        ResponseHandler<?> result = new ResponseHandler<>();

        try{

            SelectStoryDetailReq dtlReq = new SelectStoryDetailReq();
            dtlReq.setSeq(req.getSeq());
            SelectStoryDetailRes res = aStoryDao.selectStoryDetail(dtlReq);

            //중요 여부 15개 제한 : 노출 15개이고 파라미터 및 상세데이터 체크
            if(checkStoryViewCnt() && "Y".equals(req.getViewYn()) && !req.getViewYn().equals(res.getViewYn())) {
                result.setReturnCode(ReturnType.RTN_TYPE_DATA_EXISTS);
                return result;
            }

            //파일 업로드
            if(req.getImage()!=null) {
                if(req.getThumbnailSeq()!=null){
                    FileDeleteReq fr = adminFileService.setDeleteFile(req.getThumbnailSeq(),configFile.getSelectCategory4());
                    fileService.deleteFiles(fr);
                }
                int fgs = adminFileService.setImg(req.getImage() , configFile.getSelectCategory4());
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
}
