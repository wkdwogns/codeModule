package com.jjh.admin.AdminFile;

import com.jjh.common.file.config.ConfigFile;
import com.jjh.common.file.dto.model.FileDeleteInfo;
import com.jjh.common.file.dto.req.FileDeleteReq;
import com.jjh.common.file.dto.req.FileSaveReq;
import com.jjh.common.file.dto.res.FileInfoRes;
import com.jjh.common.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

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
