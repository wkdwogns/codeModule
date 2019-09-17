package com.nexon.story.service;

import com.nexon.common.config.ConfigCommon;
import com.nexon.common.dto.res.ResponseHandler;
import com.nexon.common.file.dto.req.FileListReq;
import com.nexon.common.file.service.FileService;
import com.nexon.common.type.ReturnType;
import com.nexon.notice.dao.NoticeDao;
import com.nexon.notice.dto.req.SelectNoticeReq;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class StoryService {

    @Autowired
    private StoryDao storyDao;

    @Autowired
    private NoticeDao noticeDao;

    @Autowired
    private FileService fileService;

    @Autowired
    private ConfigCommon configCommon;

    public ResponseHandler<StoryListRes> selectStoryList(StoryListReq req) {
        ResponseHandler<StoryListRes> result = new ResponseHandler<>();

        try{

            req.setStartRow();
            List<StoryListVO> list = storyDao.selectStoryList(req);
            int totalCnt = storyDao.selectStoryListCnt(req);
            int noticeCnt = noticeDao.selectNoticeCnt(new SelectNoticeReq());

            StoryListRes res = new StoryListRes();
            res.setList(list);
            res.setTotalCnt(totalCnt);
            res.setNoticeCnt(noticeCnt);

            result.setData(res);
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
        }

        return result;
    }

    public ResponseHandler<SelectStoryDetailRes> selectStoryDetail(SelectStoryDetailReq req) {
        ResponseHandler<SelectStoryDetailRes> result = new ResponseHandler<>();

        try{
            SelectStoryDetailRes res = storyDao.selectStoryDetail(req);

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

    public ResponseHandler<?> topStory() {
        ResponseHandler<List> result = new ResponseHandler<>();

        try{

            List list = storyDao.topStory();
            Iterator it =  list.iterator();
            List newList = new ArrayList();
            while(it.hasNext()){
                Map map = (Map)it.next();
                if(map.get("IMG_GRP_SEQ")!=null){
                    String fgs = map.get("IMG_GRP_SEQ").toString();
                    FileListReq req = new FileListReq();
                    req.setFileGrpSeq(Integer.parseInt(fgs));
                    List fList = fileService.getFileList(req);
                    map.put("fList",fList);
                }
                newList.add(map);
            }

            result.setData(newList);
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
        }

        return result;
    }

}
