package com.jjh.admin.story.service;

import com.jjh.admin.AdminFile.AdminFileService;
import com.jjh.admin.story.dao.AstoryDao;
import com.jjh.admin.story.model.StoryListVO;
import com.jjh.admin.story.model.TopStoryVO;
import com.jjh.admin.story.req.*;
import com.jjh.admin.story.res.SelectStoryDetailRes;
import com.jjh.admin.story.res.SelectStoryRes;
import com.jjh.common.dto.res.ResponseHandler;
import com.jjh.common.file.config.ConfigFile;
import com.jjh.common.file.dto.req.FileDeleteReq;
import com.jjh.common.file.dto.req.FileListReq;
import com.jjh.common.file.service.FileService;
import com.jjh.common.type.ReturnType;
import com.jjh.common.util.CommonUtil;
import com.nhncorp.lucy.security.xss.XssPreventer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


            if(req.getImg()!=null) {
                int igs = adminFileService.setImg(req.getImg() , configFile.getSelectCategory4());
                req.setImgGrpSeq(igs);
            }

            if(req.getFile()!=null) {
                int fgs = adminFileService.setFile(req.getFile() , configFile.getSelectCategory4());
                req.setFileGrpSeq(fgs);
            }

            if(CommonUtil.isNotEmpty(req.getEditorDelImg())){
                adminFileService.setDeleteFileByEditor(req.getEditorDelImg(),configFile.getSelectCategory5());
            }

            aStoryDao.insertStory(req);

            this.updateOrderNo("insert",req.getImportantYn(),req.getStorySeq());

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

            if(req.getImg()!=null) {
                if(req.getImgSeq()!=null){
                    FileDeleteReq fr = adminFileService.setDeleteFile(req.getImgSeq(),configFile.getSelectCategory4());
                    fileService.deleteFiles(fr);
                }
                int igs = adminFileService.setImg(req.getImg() , configFile.getSelectCategory4());
                req.setImgGrpSeq(igs);
            }

            if(req.getFile()!=null) {
                int fgs = adminFileService.setFile(req.getFile() , configFile.getSelectCategory4());
                if(req.getFileGrpSeq()!=null){
                    Map map =new HashMap();
                    map.put("newFgs",fgs);
                    map.put("fgs",req.getFileGrpSeq());
                    aStoryDao.updateFileGrpSeq(map);
                    aStoryDao.deleteFileGrpSeq(map);
                }else{
                    req.setFileGrpSeq(fgs);
                }
            }

            if( CommonUtil.isNotEmpty( req.getDelete() )){
                for( String i : req.getDelete() ){
                    int fileSeq = Integer.parseInt(i);
                    DeleteStoryImgReq dReq = new DeleteStoryImgReq(fileSeq,"attach");
                    this.deleteFile(dReq);
                }
            }

            if(CommonUtil.isNotEmpty(req.getEditorDelImg())){
                adminFileService.setDeleteFileByEditor(req.getEditorDelImg(),configFile.getSelectCategory5());
            }

            String update = "update";

            SelectStoryDetailReq sReq = new SelectStoryDetailReq();
            sReq.setSeq(req.getSeq());
            SelectStoryDetailRes res = aStoryDao.selectStoryDetail(sReq);
            if ( !res.getImportantYn().equals( req.getImportantYn() ) ){
                update="update_1";
            }

            aStoryDao.updateStory(req);

            this.updateOrderNo(update ,req.getImportantYn() ,req.getSeq());

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
                List fList = fileService.getFileList(new FileListReq(fgs));
                res.setFList(fList);
            }

            Integer igs= res.getImgGrpSeq();
            if(igs!=null){
                List iList = fileService.getFileList(new FileListReq(igs));
                res.setIList(iList);
            }

            String clean = res.getTitle();
            String origin = XssPreventer.unescape(clean);
            res.setTitle(origin);

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

            //aStoryDao.deleteFile(req);

            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
        }

        return result;
    }

    public ResponseHandler<?> getTopStory(SelectTopStoryReq req) {
        ResponseHandler<Map> result = new ResponseHandler<>();

        try{
            Map map  = new HashMap<>();
            List<TopStoryVO> list = aStoryDao.getTopStory(req);
            map.put("list",list);

            result.setData(map);
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
        }

        return result;
    }

    public ResponseHandler<?> setTopStory(UpdateTopStoryReq req) {
        ResponseHandler<Map> result = new ResponseHandler<>();

        try{

            aStoryDao.deleteTopStory();
            List<Map> list = req.getList();
            for(Map map :list){
                aStoryDao.setTopStory(map);
            }

            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
        }

        return result;
    }

    private void updateOrderNo(String flag,String Yn,String seq){

        List<TopStoryVO> list = aStoryDao.getTopStory(new SelectTopStoryReq());

        if(Yn.equals("Y")){
            boolean updateYn=false;

            //n에서 y으로 바뀔때
            if(flag.equals("update_1")){ updateYn=true; }
            //등록시
            if(flag.equals("insert")){ updateYn=true; }

            if(updateYn){
                this.setOrderNo(1,seq);

                for(TopStoryVO vo : list ){
                    if(CommonUtil.isEmpty(vo.getOrderNo())){
                        continue;
                    }
                    if( vo.getOrderNo().equals("4") ){
                        this.setOrderNo(null,vo.getStorySeq()+"");
                    }else{
                        String ord = vo.getOrderNo();
                        this.setOrderNo(Integer.parseInt(ord)+1,vo.getStorySeq()+"");
                    }
                }
            }

        }else{

            //y에서 n으로 바뀔때
            if(flag.equals("update_1")){
                this.setOrderNo(null,seq);
                //삭제되고 기존게 앞으로 정렬
                int ord=1;
                for(TopStoryVO vo : list ){
                    if( vo.getStorySeq()==Integer.parseInt(seq) ){

                    }else{
                        this.setOrderNo(ord,vo.getStorySeq()+"");
                        ord++;
                    }
                }

            }

        }
    }

    private void setOrderNo(Integer ord,String seq){
        Map map = new HashMap();
        map.put("order", ord );
        map.put("seq", seq );
        aStoryDao.setTopStory(map);
    }
}
