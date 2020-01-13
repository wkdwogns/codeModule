package com.jjh.common.file.service;

import com.jjh.common.config.ConfigCommon;
import com.jjh.common.file.aws.AwsHandler;
import com.jjh.common.file.aws.AwsHandlerImpl;
import com.jjh.common.file.config.ConfigFile;
import com.jjh.common.file.dao.FileDao;
import com.jjh.common.file.dto.model.FileDeleteInfo;
import com.jjh.common.file.dto.model.FileInfo;
import com.jjh.common.file.dto.model.FileResultInfo;
import com.jjh.common.file.dto.req.*;
import com.jjh.common.file.dto.res.FileDownloadRes;
import com.jjh.common.file.dto.res.FileInfoRes;
import com.jjh.common.file.dto.res.FileZipDownloadRes;
import com.jjh.common.file.ftp.FtpHandler;
import com.jjh.common.file.ftp.FtpHandlerImpl;
import com.jjh.common.file.storage.StorigeHandler;
import com.jjh.common.file.storage.StorigeHandlerImpl;
import com.jjh.common.password.PasswordHandler;
import com.jjh.common.type.ReturnType;
import com.jjh.common.util.ComEncDecUtil;
import com.jjh.common.util.CommonUtil;
import com.jjh.common.util.ConvertUtil;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ConfigFile configFile;

    @Autowired
    PasswordHandler passwordHandler;

    @Autowired
    FileDao fileDao;

    @Autowired
    ConfigCommon configCommon;

    CommonUtil commonUtil = new CommonUtil();

    /**
     * 파일 저장 함수
     *
     * 여러개 파일을 동시에 저장 할 수 있다.
     *
     *
     * @param files 저장할 파일들
     * @param req 파일에 대한 정보
     * @return
     * @throws Exception
     */
    public FileInfoRes storeFiles(MultipartFile[] files, FileSaveReq req) throws Exception{
        FileInfoRes fileInfoRes = new FileInfoRes();

        /////////////////////////////////////////////////////////////////////////////////
        // 파일 요청 수 확인
        int maxFilesCnt = configFile.getMaxFilesCnt();

        if(files.length > maxFilesCnt) {
            return null;
        }

        if(files.length <= 0) {
            return null;
        }

        boolean checkExt = false;

        //확장자 유효성 체크
        for(MultipartFile file  : files) {
            String fileOriginName = file.getOriginalFilename();

            //파일 확장자 체크
            if(req.getCheckExtCategory() != 0) {
                checkExt = checkFileExtension(req.getCheckExtCategory(), fileOriginName);
                if(!checkExt) {
                    break;
                }
            }
        }

        if(checkExt) {
            fileInfoRes.setCheckExtension(true);
        } else {
            fileInfoRes.setCheckExtension(false);
            return fileInfoRes;
        }

        int selection = 0;
        FtpHandler ftpHandler = null;
        StorigeHandler storigeHandler = null;
        AwsHandler awsHandler = null;
        ReturnType rtn = ReturnType.RTN_TYPE_NG;

//        int userSeq = membershipService.currentSessionUserInfo().getUserSeq();
        int userSeq = 1;
        int fileGrpSeq = req.getFileGrpSeq();

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Connect
        if(configFile.getSelectSavingWay() == configFile.getSelectSavingFtp()) {
            // FTP 이미지 서버
            selection = 1;
            ftpHandler = new FtpHandlerImpl();

            // 연결
            rtn = ftpHandler.connect(configFile.getSettingFtpServer(), configFile.getSettingFtpUserId() ,configFile.getSettingFtpPassword());

            if(rtn != ReturnType.RTN_TYPE_OK)
            {
                return null;
            }
        }
        else if (configFile.getSelectSavingWay() == configFile.getSelectSavingStorage()){
            // 로컬 스토리지
            selection = 2;
            storigeHandler = new StorigeHandlerImpl();
        }
        else if (configFile.getSelectSavingWay() == configFile.getSelectSavingAws()) {
            // AWS
            selection = 3;
            awsHandler = new AwsHandlerImpl();

            // Connect
            awsHandler.connect(configFile.getSettingAwsFileBucketName(), configFile.getSettingAwsAccessKey(),
                    configFile.getSettingAwsSecretKey(), configFile.getRegions());
        }
        else {
            return null;
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        int category = req.getCategory();
        String subUrl = configFile.getSubUrlBySelection(category);

        // DB 파일 저장 필요 시
        if(req.getIsSaveInfos()) {

            // 그룹파일이 만들어 지지 않은 경우, 그룹 파일을 만든다.
            if(fileGrpSeq <= 0 ) {
                FileInfo fileInfo = new FileInfo();

                fileInfo.setCategory(category);
                fileInfo.setGrpNm(req.getGrpNm());
                fileInfo.setCreId(userSeq); // 추후에 수정
                fileInfo.setUpdId(userSeq); // 추후에 수정
                Map<String, Object> params = ConvertUtil.convertObjectToMap(fileInfo);
                fileDao.insertFileGrp(params);
                fileGrpSeq = (int)params.get("fileGrpSeq");
            }
        }

        List<FileResultInfo> fileResultInfos = new ArrayList<>();
        int fileCnt = 0;

        /////////////////////////////////////////////////////////////////////////////////
        //  파일 저장 시작
        for(MultipartFile file  : files) {

            String fileUrl = null;
            String subFileUrl= null;

            // original name
            String fileOriginName = file.getOriginalFilename();

            if(CommonUtil.isNotEmpty(fileOriginName)){
                // 암호화 된 랜덤 파일이름 생성
                String fileName = passwordHandler.makeRandomKey(configFile.getLengthFilename());

                // Connect
                switch (selection) {
                    case 1:
                        rtn = ftpHandler.store(file, subUrl, fileName);
                        fileUrl = configFile.getImageServerUrl() + "/" + subUrl + "/" + fileName;
                        subFileUrl = "/" + subUrl + "/" + fileName;
                        break;

                    case 2:
                        rtn = storigeHandler.store(file,configFile.getStroageLoc()+"/"+subUrl,fileName);
                        fileUrl =  configFile.getUrlStorage() + "/" + subUrl +  "/" + fileName;
                        subFileUrl = "/" + subUrl + "/" + fileName;
                        break;

                    case 3:
                        rtn = awsHandler.store(file, subUrl, fileName, configFile.checkImageYn(category));
                        fileUrl = configFile.getUrlAwsS3() + "/" + subUrl+ "/"+fileName;
                        subFileUrl = "/" + subUrl + "/" + fileName;
                }

                FileResultInfo fileResultInfo = new FileResultInfo();

                fileOriginName = fileOriginName.replaceAll("&", "&amp;");
                fileOriginName = fileOriginName.replaceAll("<", "&lt;");
                fileOriginName = fileOriginName.replaceAll(">", "&gt;");

                fileResultInfo.setFileOriginName(fileOriginName);
                fileResultInfo.setFileSysName(fileName);
                fileResultInfo.setFileSubUrl(subFileUrl);
                fileResultInfo.setFileUrl(fileUrl);

                if(req.getIsSaveInfos()) {
                    FileInfo fileInfo = new FileInfo();

                    fileInfo.setUpdId(userSeq);
                    fileInfo.setCategory(category);
                    fileInfo.setCreId(userSeq);
                    fileInfo.setFileGrpSeq(fileGrpSeq);
                    fileInfo.setFilePath(fileUrl);
                    fileInfo.setOrgFileNm(fileOriginName);
                    fileInfo.setSysFileNm(fileName);

                    if(req.getFileDetailInfos() != null && req.getFileDetailInfos().size() > fileCnt) {
                        fileInfo.setOrderNo(req.getFileDetailInfos().get(fileCnt).getOrderNo());
                        fileInfo.setType(req.getFileDetailInfos().get(fileCnt).getType());
                        fileInfo.setSubType(req.getFileDetailInfos().get(fileCnt).getSubType());
                    }

                    fileCnt++;

                    Map<String, Object> params = ConvertUtil.convertObjectToMap(fileInfo);
                    fileDao.insertFile(params);

                    fileResultInfo.setFileSeq((int)params.get("fileSeq"));
                }

                fileResultInfos.add(fileResultInfo);
            }
        }

        fileInfoRes.setFileGrpSeq(fileGrpSeq);
        fileInfoRes.setFileInfos(fileResultInfos);

        return fileInfoRes;
    }


    /**
     *
     * 파일 삭제 함수
     *
     * 우선순위1. 파일그룹전체를 삭제할 경우 : req 파일 그룹을 설정
     * 2. DB에 저장된 파일의 경우 : DB 저장 여부를 true로 설정
     * 3. 파일Seq 혹은 파일 이름 중 알고 있는 정보로 설정
     *
     *
     * @param req
     * @return
     * @throws Exception
     */

    public ReturnType deleteFiles(FileDeleteReq req) throws  Exception {

        /////////////////////////////////////////////////////////////////////////////////
        // 파일 요청 수 확인
        int maxFilesCnt = configFile.getMaxFilesCnt();

        if(req.getFileDeleteInfos().size() <= 0) {
            return null;
        }

        int selection = 0;
        FtpHandler ftpHandler = null;
        StorigeHandler storigeHandler = null;
        AwsHandler awsHandler = null;
        ReturnType rtn = ReturnType.RTN_TYPE_NG;

        if(configFile.getSelectSavingWay() == configFile.getSelectSavingFtp()) {

            // FTP 이미지 서버
            selection = 1;
            ftpHandler = new FtpHandlerImpl();

            // 연결
            rtn = ftpHandler.connect(configFile.getSettingFtpServer(), configFile.getSettingFtpUserId() ,configFile.getSettingFtpPassword());

            if(rtn != ReturnType.RTN_TYPE_OK)
            {
                return null;
            }
        }
        else if (configFile.getSelectSavingWay() == configFile.getSelectSavingStorage()){
            // 로컬 스토리지
            selection = 2;
            storigeHandler = new StorigeHandlerImpl();
        }
        else if (configFile.getSelectSavingWay() == configFile.getSelectSavingAws()) {

            // AWS
            selection = 3;
            awsHandler = new AwsHandlerImpl();

            // Connect
            awsHandler.connect(configFile.getSettingAwsFileBucketName(), configFile.getSettingAwsAccessKey(),
                    configFile.getSettingAwsSecretKey(), configFile.getRegions());
        }
        else {
            return null;
        }

        int category = req.getCategory();
        String subUrl = configFile.getSubUrlBySelection(category);

        // 삭제 정보
        List<FileDeleteInfo> fileDeleteInfos = new ArrayList<>();

        if(req.getFileGrpSeq() > 0 ) {
            // 파일그룹 시퀀스 삭제 시

            Map<String, Object> params = ConvertUtil.convertObjectToMap(req);
            params.put("delYn", 'Y');
            // 파일 그룹 삭제
            fileDao.updateFileGrp(params);

            List<FileInfo> fileInfos = fileDao.selectFile(params);

            for(FileInfo fileInfo : fileInfos) {
                FileDeleteInfo fileDeleteInfo = new FileDeleteInfo();
                fileDeleteInfo.setFileSeq(fileInfo.getFileSeq());
                fileDeleteInfo.setFileName(fileInfo.getSysFileNm());

                fileDeleteInfos.add(fileDeleteInfo);

                Map<String, Object> params2 = ConvertUtil.convertObjectToMap(fileDeleteInfo);
                // DB 상에서 파일 삭제
                params2.put("delYn", 'Y');
                fileDao.updateFile(params2);
            }
        }
        else {
            // 파일 선택 삭제 시

            fileDeleteInfos = req.getFileDeleteInfos();

            for(FileDeleteInfo fileDeleteInfo : fileDeleteInfos) {

                // DB에 저장된 값 이거나 파일 이름이 없어 DB에서 반드시 가져와야할 경우
                if(req.getIsSaveInfos() || !commonUtil.isExist(fileDeleteInfo.getFileName())) {

                    // 파일 삭제 정보 상의 FileSeq로 검색
                    Map<String, Object> params = ConvertUtil.convertObjectToMap(fileDeleteInfo);
                    List<FileInfo> fileInfos = fileDao.selectFile(params);

                    // DB 상에서 파일 삭제
                    params.put("delYn", 'Y');
                    fileDao.updateFile(params);

                    // 이름 저장
                    fileDeleteInfo.setFileName(fileInfos.get(0).getSysFileNm());
                }
            }
        }

        /////////////////////////////////////////////////////////////////////////////////
        //  파일 삭제 시작
        for(FileDeleteInfo fileDeleteInfo : fileDeleteInfos) {

            String fileName = fileDeleteInfo.getFileName();

            switch (selection) {
                case 1:
                    // 파일 삭제
                    ftpHandler.delete(subUrl, fileName);
                    break;

                case 2:
                    storigeHandler.delete(configFile.getStroageLoc()+"/"+subUrl,fileName);
                    break;

                case 3:
                    awsHandler.delete(subUrl, fileName);
                    break;
            }
        }

        return ReturnType.RTN_TYPE_OK;
    }

    /**
     *
     * @param req
     * @return
     * @throws Exception
     */
    public FileDownloadRes downloadFile(FileDownloadReq req) throws  Exception {

        String subUrl = null;
        int category = configFile.getSelectNoCategory();
        String fileName = req.getFileName();
        String orgFileName = fileName;
        FileDownloadRes fileDownloadRes = new FileDownloadRes();

        if(configFile.getSelectCategory8() == req.getCategory()) {
            String enc = req.getEnc();
            if(CommonUtil.isNotEmpty(enc)) {
                String decStr = ComEncDecUtil.getDecrypted(enc, configCommon.getAes128Key());
                String[] array = decStr.split("[|]");
                if(array != null && array.length > 1) {
                    int seq = Integer.parseInt(array[0]);
                    int qnaCategory = Integer.parseInt(array[1]);
                    if(configFile.getSelectCategory8() == qnaCategory) {
                        if(req.getFileSeq() != seq) {
                            return null;
                        }
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }




        ////////////////////////////////////////////////////////////////////////
        // Get category
        ////////////////////////////////////////////////////////////////////////
         if(req.getCategory() > 0) {
            category = req.getCategory();
        }

        ////////////////////////////////////////////////////////////////////////
        // Get filename
        ////////////////////////////////////////////////////////////////////////
        if(req.getFileSeq() > 0) {
            // 그룹 시퀀스로 파일이름을 지정한 경우, 원본 파일이름으로 변환 한다.
            Map<String, Object> params = ConvertUtil.convertObjectToMap(req);
            List<FileInfo> fileInfos = fileDao.selectFile(params);
            fileName = fileInfos.get(0).getSysFileNm();
            orgFileName = fileInfos.get(0).getOrgFileNm();

        } else {
            // 파일이름으로 선택한 경우, 저장된 파일을 원본 파일로 생각 한다.
            if (!commonUtil.isExist(req.getFileName())) {
                return null;
            }
        }

        // 폴더 위치를 가지고 온다.
        subUrl = configFile.getSubUrlBySelection(category);

        // 이미지 FTP 서버 사용 시
        if(configFile.getSelectSavingWay() == configFile.getSelectSavingFtp()) {
            ;
        }

        // 로컬 폴더 사용 시
        else if (configFile.getSelectSavingWay() == configFile.getSelectSavingStorage()) {

            File file = new File(configFile.getStroageLoc() + "/" + subUrl + "/" + fileName);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            //FileDownloadRes fileDownloadRes = new FileDownloadRes();
            fileDownloadRes.setContentLength(file.length());
            fileDownloadRes.setInputStreamResource(resource);
            //return fileDownloadRes;
        }
        else if (configFile.getSelectSavingWay() == configFile.getSelectSavingAws()) {

            AwsHandler awsHandler = new AwsHandlerImpl();

            // Connect
            awsHandler.connect(configFile.getSettingAwsFileBucketName(), configFile.getSettingAwsAccessKey(),
                    configFile.getSettingAwsSecretKey(), configFile.getRegions());

            // get data
            fileDownloadRes = awsHandler.download(subUrl, fileName);
        }
        else {
            return null;
        }

        fileDownloadRes.setOrgFileName(orgFileName);
        return fileDownloadRes;
    }

    /**
     *  Zip 파일 다운로드
     *
     * @param req
     * @return
     * @throws Exception
     */
    public FileZipDownloadRes downloadZipFile(FileZipDownloadReq req) throws  Exception {
        FileZipDownloadRes fileZipDownloadRes = new FileZipDownloadRes();
        fileZipDownloadRes.setOutputStreamResource(new ByteArrayOutputStream());

        ZipOutputStream zippedOut = new ZipOutputStream(fileZipDownloadRes.getOutputStreamResource());

        Map<String, Object> params = ConvertUtil.convertObjectToMap(req);
        List<FileInfo> fileInfos = fileDao.selectFile(params);

        if(fileInfos.size() <= 0 ) {
            return null;
        }

        for (FileInfo file : fileInfos) {

            FileDownloadReq req2 = new FileDownloadReq();
            req2.setCategory(req.getCategory());
            req2.setFileSeq(file.getFileSeq());

            // 파일 다운로드
            FileDownloadRes fileDownloadRes =  this.downloadFile(req2);

            System.out.println(file.getOrgFileNm());
            // 파일 정보 저장
            ZipEntry entry = new ZipEntry(file.getOrgFileNm());
            entry.setSize(fileDownloadRes.getContentLength());
            entry.setTime(System.currentTimeMillis());
            zippedOut.putNextEntry(entry);

            //파일 복사
            StreamUtils.copy(fileDownloadRes.getInputStreamResource().getInputStream(), zippedOut);
            // 1 파일 종료
            zippedOut.closeEntry();
        }

        zippedOut.finish();

        return fileZipDownloadRes;
    }

    /**
     * 파일 정보를 갱신하는 함수
     *
     * @param fileInfo
     * @return
     * @throws Exception
     */
    public ReturnType updateFileInfo(FileInfo fileInfo) throws  Exception {

        if(CommonUtil.isEmpty(fileInfo.getFileSeq())) {
            return ReturnType.RTN_TYPE_NG;
        }

        Map<String, Object> params = ConvertUtil.convertObjectToMap(fileInfo);
        fileDao.updateFile(params);

        return ReturnType.RTN_TYPE_OK;
    }



    /**
     * 파일 목록 조회하는 함수
     *
     * @param req
     * @return
     * @throws Exception
     */
    public List<FileInfo> getFileList(FileListReq req) throws  Exception{

        Map<String, Object> params = ConvertUtil.convertObjectToMap(req);
        return fileDao.selectFile(params);

    }

    /**
     * 파일 확장자 유효성 체크
     * @param category
     * @param fileOriginName
     * @return
     */
    public boolean checkFileExtension(int category, String fileOriginName) {
        boolean result = false;
        String ext = FilenameUtils.getExtension(fileOriginName).toLowerCase();
        String[] extArray = configFile.getCheckExtension(category);

        if(CommonUtil.isNotEmpty(ext) && extArray != null && extArray.length > 0) {
            result = Arrays.asList(extArray).contains(ext);
        }

        return result;
    }


    public FileDownloadRes downloadFiles(FileDownloadReq req) throws  Exception {

        int category = req.getCategory();
        logger.info("getFileName###", req.getFileName());
        logger.info("getFileName###" + req.getFileName());
        String fileName = req.getFileName();
        String orgFileName = fileName;
        FileDownloadRes fileDownloadRes = new FileDownloadRes();

        // 폴더 위치를 가지고 온다.
        String subUrl = configFile.getSubUrlBySelection(category);
        File file = new File(configFile.getStroageLoc() + "/" + subUrl + "/" + fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        fileDownloadRes.setContentLength(file.length());
        fileDownloadRes.setInputStreamResource(resource);
        fileDownloadRes.setOrgFileName(orgFileName);
        return fileDownloadRes;
    }

}
