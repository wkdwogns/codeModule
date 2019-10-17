package com.nexon.admin.AdminFile;

import com.nexon.common.dto.res.ResponseHandler;
import com.nexon.common.file.config.ConfigFile;
import com.nexon.common.file.dto.model.FileDeleteInfo;
import com.nexon.common.file.dto.req.FileDeleteReq;
import com.nexon.common.file.dto.req.FileSaveReq;
import com.nexon.common.file.dto.res.FileInfoRes;
import com.nexon.common.file.service.FileService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Service
public class AdminFileService {
    @Autowired
    private FileService fileService;

    @Autowired
    ConfigFile configFile;

    public int setImg(MultipartFile file, int cate) throws Exception{
        MultipartFile[] files = {file};
        FileSaveReq fileSaveReq = new FileSaveReq();
        fileSaveReq.setCategory(cate);
        fileSaveReq.setIsSaveInfos(true);
        fileSaveReq.setCheckExtCategory(configFile.getExtCategory3());
        FileInfoRes fileInfoRes = fileService.storeFiles(files, fileSaveReq);
        return fileInfoRes.getFileGrpSeq();
    }

    public int setFile(MultipartFile[] file, int cate) throws Exception{

        FileSaveReq fileSaveReq = new FileSaveReq();
        fileSaveReq.setCategory(cate);
        fileSaveReq.setIsSaveInfos(true);
        fileSaveReq.setCheckExtCategory(configFile.getExtCategory3());
        FileInfoRes fileInfoRes = fileService.storeFiles(file, fileSaveReq);
        return fileInfoRes.getFileGrpSeq();
    }

    public FileDeleteReq setDeleteFile(int seq, int cate) throws Exception{
        FileDeleteInfo info = new FileDeleteInfo();
        info.setFileSeq(seq);

        List<FileDeleteInfo> dList = new ArrayList<>();
        dList.add(info);


        FileDeleteReq fr = new FileDeleteReq();
        fr.setIsSaveInfos(true);
        fr.setFileDeleteInfos(dList);
        fr.setCategory(cate);
        return fr;
    }

    public void setDeleteFileByEditor(ArrayList<String> EditorDelImg,int cate){
        //3. 공지사항 삭제된 이미지 물리 삭제

        List<FileDeleteInfo> fileDelInfoList = new ArrayList<>();

        ArrayList<String> imgs = EditorDelImg;
        for(String nm : imgs){
            FileDeleteInfo fileDeleteInfo = new FileDeleteInfo();
            fileDeleteInfo.setFileName(nm);
            fileDelInfoList.add(fileDeleteInfo);
        }

        FileDeleteReq fileDeleteReq = new FileDeleteReq();
        fileDeleteReq.setCategory(cate);
        fileDeleteReq.setIsSaveInfos(false);
        fileDeleteReq.setFileDeleteInfos(fileDelInfoList);

        try {
            fileService.deleteFiles(fileDeleteReq);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
