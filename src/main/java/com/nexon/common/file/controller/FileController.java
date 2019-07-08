package com.nexon.common.file.controller;

import com.nexon.common.dto.res.ResponseHandler;
import com.nexon.common.file.config.ConfigFile;
import com.nexon.common.file.dto.req.FileDownloadReq;
import com.nexon.common.file.dto.req.FileSaveReq;
import com.nexon.common.file.dto.req.FileZipDownloadReq;
import com.nexon.common.file.dto.res.FileDownloadRes;
import com.nexon.common.file.dto.res.FileInfoRes;
import com.nexon.common.file.dto.res.FileZipDownloadRes;
import com.nexon.common.file.service.FileService;
import com.nexon.common.util.CommonUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;

@RestController
@RequestMapping(value="/api/file" )
public class FileController {

    @Autowired
    FileService fileService;

    @Autowired
    ConfigFile configFile;

    CommonUtil commonUtil = new CommonUtil();
    @ApiOperation(value="사용자 파일 다운로드")
    @GetMapping(value="/attach")
    public ResponseEntity<InputStreamResource> downloads(FileDownloadReq req) throws Exception{
        FileDownloadRes fileDownloadRes = new FileDownloadRes();
        try {
            fileDownloadRes = fileService.downloadFiles(req);

        }
        catch(Exception e) {
            return null;
        }

        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLEncoder.encode(fileDownloadRes.getOrgFileName(), "UTF-8").replaceAll("\\+", "%20"))
                // Content-Type
                .contentType(MediaType.parseMediaType("application/octet-stream;UTF-8"))
                // Contet-Length
                .contentLength(fileDownloadRes.getContentLength())
                // Input Stream
                .body(fileDownloadRes.getInputStreamResource());
    }

    @ApiOperation(value="1개 파일 다운로드")
    @GetMapping(value="/download")
    public ResponseEntity<InputStreamResource> downloadFile(FileDownloadReq req) throws Exception{

        FileDownloadRes fileDownloadRes = new FileDownloadRes();

        try {
            fileDownloadRes = fileService.downloadFile(req);


        }
        catch(Exception e) {
            return null;
        }

        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLEncoder.encode(fileDownloadRes.getOrgFileName(), "UTF-8").replaceAll("\\+", "%20"))
                // Content-Type
                .contentType(MediaType.parseMediaType("application/octet-stream;UTF-8"))
                // Contet-Length
                .contentLength(fileDownloadRes.getContentLength())
                // Input Stream
                .body(fileDownloadRes.getInputStreamResource());
    }

    @ApiOperation(value="전체 파일 압축 다운로드")
    @GetMapping(value="/download/zip")
    public ResponseEntity<InputStreamResource> downloadZipFile(FileZipDownloadReq req){

        FileZipDownloadRes fileDownloadRes = new FileZipDownloadRes();

        try {
            fileDownloadRes = fileService.downloadZipFile(req);
        }
        catch(Exception e) {
            return null;
        }

        String contentType = "application/zip";
        String zipFileName;

        if(!commonUtil.isExist(req.getZipFileName())) {
            zipFileName = "downloadFiles.zip";
        } else {
            zipFileName = req.getZipFileName() + ".zip";
        }

        InputStream is = new ByteArrayInputStream(fileDownloadRes.getOutputStreamResource().toByteArray());
        InputStreamResource inputStreamResource = new InputStreamResource(is);

        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + zipFileName)
                // Content-Type
                .contentType(MediaType.parseMediaType(contentType))
                // Contet-Length
                //.contentLength(???)
                // Input Stream
                .body(inputStreamResource);
                //.body(fileDownloadRes.getOutputStreamResource());
    }

    /**
     *  블로그 에디터 파일 업로드
     */
    @ApiOperation(value = "블로그 에디터 파일 업로드")
    @PostMapping(value = "/editor/imgs")
    public ResponseHandler<FileInfoRes> imgUpload(@RequestParam("file") MultipartFile[] files, FileSaveReq fileSaveReq) {
        final ResponseHandler<FileInfoRes> result = new ResponseHandler<>();
        System.out.println(files);
        FileInfoRes fileInfoRes;
        fileSaveReq.setCategory(fileSaveReq.getCategory());
        fileSaveReq.setCheckExtCategory(configFile.getExtCategory1());
        fileSaveReq.setIsSaveInfos(true);

        try {
            fileInfoRes = fileService.storeFiles(files, fileSaveReq);
            result.setData(fileInfoRes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  result;
    }
}
