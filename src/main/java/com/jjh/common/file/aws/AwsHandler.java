package com.jjh.common.file.aws;

import com.amazonaws.regions.Regions;
import com.jjh.common.file.dto.res.FileDownloadRes;
import com.jjh.common.type.ReturnType;
import org.springframework.web.multipart.MultipartFile;

public interface AwsHandler {
    public ReturnType connect(String bucketName, String accessKey, String secretKey, Regions region) throws Exception;
    public ReturnType store(MultipartFile file, String subUrl, String fileName, boolean isImage) throws Exception;
    public ReturnType delete(String subUrl, String fileName) throws Exception;
    public FileDownloadRes download(String subUrl, String fileName) throws Exception;
}
