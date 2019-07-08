package com.nexon.admin.banner.service;

import com.nexon.admin.AdminFile.AdminFileService;
import com.nexon.admin.banner.dao.AdminBannerDao;
import com.nexon.admin.banner.model.BannerListVO;
import com.nexon.admin.banner.model.UpdateBannerSortVO;
import com.nexon.admin.banner.req.*;
import com.nexon.admin.banner.res.SelectBannerDetailRes;
import com.nexon.admin.banner.res.SelectBannerRes;
import com.nexon.common.config.ConfigCommon;
import com.nexon.common.dto.res.ResponseHandler;
import com.nexon.common.file.config.ConfigFile;
import com.nexon.common.file.dto.model.FileDeleteInfo;
import com.nexon.common.file.dto.model.FileInfo;
import com.nexon.common.file.dto.req.FileDeleteReq;
import com.nexon.common.file.dto.req.FileListReq;
import com.nexon.common.file.dto.req.FileSaveReq;
import com.nexon.common.file.dto.res.FileInfoRes;
import com.nexon.common.file.service.FileService;
import com.nexon.common.type.ReturnType;
import com.nexon.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ABannerService {

    @Autowired
    private AdminBannerDao adminBannerDao;
    @Autowired
    private ConfigFile configFile;
    @Autowired
    private ConfigCommon configCommon;
    @Autowired
    private AdminFileService adminFileService;
    @Autowired
    private FileService fileService;

    //배너조회
    public ResponseHandler<SelectBannerRes> selectBanner(SelectBannerReq req) {
        ResponseHandler<SelectBannerRes> result = new ResponseHandler<>();
        try{
            if(CommonUtil.isEmpty(req.getCategory())){
                req.setCategory(configCommon.getDtl().getBannerTop());
            }

            req.setMstCd(configCommon.getMst().getBannerType());
            req.setStartRow();
            List<BannerListVO> list = adminBannerDao.selectBanner(req);
            int cnt = adminBannerDao.selectBannerCnt(req);

            SelectBannerRes res = new SelectBannerRes();
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

    //컨텐츠 배너 조회
    public ResponseHandler<SelectBannerRes> selectContentBanner(SelectBannerReq req) {
        req.setCategory(configCommon.getDtl().getBannerContent());
        return this.selectBanner(req);
    }

    //노출 배너조회
    public ResponseHandler<SelectBannerRes> selectViewBanner(SelectBannerReq req) {
        ResponseHandler<SelectBannerRes> result = new ResponseHandler<>();
        try{
            if(CommonUtil.isEmpty(req.getCategory())){
                req.setCategory(configCommon.getDtl().getBannerTop());
            }

            req.setMstCd(configCommon.getMst().getBannerType());
            req.setViewYn("Y");
            List<BannerListVO> list = adminBannerDao.selectBanner(req);

            SelectBannerRes res = new SelectBannerRes();
            res.setList(list);

            result.setData(res);
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
        }

        return result;
    }

    //컨텐츠 노출배너 조회
    public ResponseHandler<SelectBannerRes> selectViewContentsBanner(SelectBannerReq req) {
        req.setCategory(configCommon.getDtl().getBannerContent());
        return this.selectViewBanner(req);
    }

    //배너 등록
    public ResponseHandler<?> insertBanner(InsertBannerReq req) {
        ResponseHandler<?> result = new ResponseHandler<>();

        try{

            int fileCate = configFile.getSelectCategory1();
            if(CommonUtil.isEmpty(req.getCategory())){
                fileCate = configFile.getSelectCategory0();
                req.setCategory(configCommon.getDtl().getBannerTop());
            }

            //파일 업로드
            if(req.getImage() != null) {
                int fgs = adminFileService.setImg(req.getImage() , fileCate);
                req.setFileGrpSeq(fgs);
            }

            adminBannerDao.insertBanner(req);

            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
        }

        return result;
    }

    //컨텐츠 배너 등록
    public ResponseHandler<?> insertContentsBanner(InsertBannerReq req) {
        req.setCategory(configCommon.getDtl().getBannerContent());
        return this.insertBanner(req);
    }

    //배너수정
    public ResponseHandler<?> updateBanner(UpdateBannerReq req) {
        ResponseHandler<?> result = new ResponseHandler<>();

        try{

            int fileCate = configFile.getSelectCategory1();
            if(CommonUtil.isEmpty(req.getCategory())){
                fileCate = configFile.getSelectCategory0();
            }


            MultipartFile[] files = {req.getImage()};
            //파일 업로드
            if(req.getImage() != null) {
                //1. 확장자 유효성 체크
                for(MultipartFile file  : files) {
                    String fileOriginName = file.getOriginalFilename();

                    Boolean checkExt = fileService.checkFileExtension(configFile.getExtCategory1(), fileOriginName);
                    if(!checkExt) {
                        result.setReturnCode(ReturnType.RTN_TYPE_FILE_EXTENSION_NG);
                        return result;
                    }
                }

                FileSaveReq fileSaveReq = new FileSaveReq();
                fileSaveReq.setIsSaveInfos(true);

                fileSaveReq.setFileGrpSeq(0);
                fileSaveReq.setCategory(fileCate);
                fileSaveReq.setCheckExtCategory(configFile.getExtCategory1());

                if(CommonUtil.isNotEmpty(req.getFileGrpSeq())){
                    fileSaveReq.setFileGrpSeq(req.getFileGrpSeq());

                    //2. 기존 파일 삭제
                    ArrayList<Integer> seq = new ArrayList<>();
                    FileListReq fileListReq = new FileListReq();
                    fileListReq.setFileGrpSeq(req.getFileGrpSeq());
                    List<FileInfo> fileList = fileService.getFileList(fileListReq);

                    for(FileInfo file : fileList){
                        seq.add(file.getFileSeq());
                    }

                    if(seq.size() > 0){
                        this.deleteFiles(seq, fileSaveReq.getCategory());
                    }
                }

                //3. 신규파일 등록
                FileInfoRes fileInfoRes = fileService.storeFiles(files, fileSaveReq);
                if(!fileInfoRes.isCheckExtension()){
                    result.setReturnCode(ReturnType.RTN_TYPE_FILE_EXTENSION_NG);
                    return result;
                }
            }

             adminBannerDao.updateBanner(req);

            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
        }

        return result;
    }

    //컨텐츠 배너 수정
    public ResponseHandler<?> updateContentsBanner(UpdateBannerReq req) {
        req.setCategory(configCommon.getDtl().getBannerContent());
        return this.updateBanner(req);
    }

    public ResponseHandler<?> deleteBanner(DeleteBannerReq req) {
        ResponseHandler<?> result = new ResponseHandler<>();

        try{
            req.setDelYn("Y");
            adminBannerDao.deleteBanner(req);

            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
        }

        return result;
    }

    public ResponseHandler<SelectBannerDetailRes> selectBannerDetail(SelectBannerDetailReq req) {
        ResponseHandler<SelectBannerDetailRes> result = new ResponseHandler<>();

        try{

            SelectBannerDetailRes res = adminBannerDao.selectBannerDetail(req);

            FileListReq fReq = new FileListReq();
            if(res.getFileGrpSeq()!=null){
                fReq.setFileGrpSeq(res.getFileGrpSeq());
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

    /* 배너 정렬 및 노출 관리 */
    @Transactional
    public ResponseHandler<?> updateViewControl(UpdateBannerSortReq req) {
        ResponseHandler<?> result = new ResponseHandler<>();
        try{
            if(CommonUtil.isEmpty(req.getCategory())){
                req.setCategory(configCommon.getDtl().getBannerTop());
            }

            //1. 모든 노출여부 N
            adminBannerDao.allUpdateViewN(req);

            for(UpdateBannerSortVO paramVo : req.getSortArry()){
                //2. 노출 및 정렬 관리
                paramVo.setUserSeq(req.getUserSeq());
                adminBannerDao.updateSortBanner(paramVo);
            }
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        return result;
    }


    /**
     * 파일삭제
     * **/
    public void deleteFiles(ArrayList<Integer> files, Integer category) throws Exception {
        List<FileDeleteInfo> fileDelInfoList = new ArrayList<>();

        if(CommonUtil.isNotEmpty(files)){

            for(Integer seq : files){
                FileDeleteInfo fileDeleteInfo = new FileDeleteInfo();
                fileDeleteInfo.setFileSeq(seq);
                fileDelInfoList.add(fileDeleteInfo);
            }

            FileDeleteReq fileDeleteReq = new FileDeleteReq();
            fileDeleteReq.setCategory(category);
            fileDeleteReq.setIsSaveInfos(true);
            fileDeleteReq.setFileDeleteInfos(fileDelInfoList);

            fileService.deleteFiles(fileDeleteReq);
        }
    }

    /* 컨텐츠 배너 정렬 */
    public ResponseHandler<?> updateViewContentsControl(UpdateBannerSortReq req) {
        req.setCategory(configCommon.getDtl().getBannerContent());
        return this.updateViewControl(req);
    }
}
