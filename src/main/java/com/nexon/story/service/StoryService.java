package com.nexon.story.service;

import com.nexon.common.config.ConfigCommon;
import com.nexon.common.dto.res.ResponseHandler;
import com.nexon.common.file.dto.req.FileListReq;
import com.nexon.common.file.service.FileService;
import com.nexon.common.type.ReturnType;
import com.nexon.story.dao.StoryDao;
import com.nexon.story.dto.model.StoryDetailPrevNextVO;
import com.nexon.story.dto.model.StoryListVO;
import com.nexon.story.dto.req.SelectStoryDetailReq;
import com.nexon.story.dto.req.StoryListReq;
import com.nexon.story.dto.res.SelectStoryDetailRes;
import com.nexon.story.dto.res.StoryListImportRes;
import com.nexon.story.dto.res.StoryListRes;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class StoryService {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StoryDao storyDao;

    @Autowired
    private FileService fileService;

    @Autowired
    private ConfigCommon configCommon;

    public ResponseHandler<StoryListRes> selectStoryList(StoryListReq req) {
        ResponseHandler<StoryListRes> result = new ResponseHandler<>();

        try{
            StoryListRes res = new StoryListRes();

            req.setStartRow();
            List<StoryListVO> list = storyDao.selectStoryList(req);
            int totalCnt = storyDao.selectStoryListCnt(req);

            //List<StoryListVO> impList = storyDao.selectStoryImportantList();

            res.setList(list);
            res.setTotalCnt(totalCnt);

            result.setData(res);
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
            logger.info("selectStory[result]", result);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            logger.error("selectStory[Exception]", e);
        }

        return result;
    }

    public ResponseHandler<StoryListImportRes> selectStoryImportantList() {
        ResponseHandler<StoryListImportRes> result = new ResponseHandler<>();

        try{
            StoryListImportRes res = new StoryListImportRes();

            List<StoryListVO> impList = storyDao.selectStoryImportantList();

            res.setStoryImpList(impList);

            result.setData(res);
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
            logger.info("StoryListImportRes[result]", result);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            logger.error("StoryListImportRes[Exception]", e);
        }

        return result;
    }

    public ResponseHandler<SelectStoryDetailRes> selectStoryDetail(SelectStoryDetailReq req) {
        ResponseHandler<SelectStoryDetailRes> result = new ResponseHandler<>();

        try{
            SelectStoryDetailRes res = storyDao.selectStoryDetail(req);

            Integer fgs= res.getFileGrpSeq();
            if(fgs!=null){
                FileListReq fReq = new FileListReq();
                fReq.setFileGrpSeq(fgs);
                List fList = fileService.getFileList(fReq);
                res.setFList(fList);
            }

            StoryDetailPrevNextVO prevVO = storyDao.selectPrevStory(req);
            StoryDetailPrevNextVO nextVO = storyDao.selectNextStory(req);

            res.setPrevStory(prevVO);
            res.setNextStory(nextVO);

            result.setData(res);
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
        }

        return result;
    }

    /* 스토리 관리자에서 미리보기 */
    public ResponseHandler<SelectStoryDetailRes> selectStoryPreveiw(SelectStoryDetailReq req, HttpServletRequest request) {
        ResponseHandler<SelectStoryDetailRes> result = new ResponseHandler<>();
        if(configCommon.getDomain().getAdmin().equals(request.getServerName()) ){ // 관리자 사이트에 접속시
            req.setIsPreview(true);
            return this.selectStoryDetail(req);
        } else {
            result.setReturnCode(ReturnType.RTN_TYPE_MEMBERSSHIP_AUTHORITY_NG);
        }
        return result;
    }
}
