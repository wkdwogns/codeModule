package com.nexon.common.file.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.nexon.common.file.dto.res.FileDownloadRes;
import com.nexon.common.type.ReturnType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;


public class AwsHandlerImpl implements AwsHandler {

    AmazonS3 s3 ;
    String bucket;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // File metadata를 확인 한다.
    private ObjectMetadata getObjectMetadata(MultipartFile file){

        ObjectMetadata metaData =  new ObjectMetadata();
        metaData.setContentLength(file.getSize());
        metaData.setContentType(file.getContentType());
        logger.info("[FileInfo] File Size : " + file.getSize());
        logger.info("[FileType] File Type : " + file.getContentType());
        return metaData;
    }

    /**
     *  Aws S3 서비스 접속
     *
     * @param bucketName
     * @param accessKey
     * @param secretKey
     * @param region
     * @return
     * @throws Exception
     */
    public ReturnType connect(String bucketName, String accessKey, String secretKey , Regions region) throws Exception {

        BasicAWSCredentials awsCreds  = new BasicAWSCredentials(accessKey, secretKey);

        bucket = bucketName;

        s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(region)
                .build();

        if(s3 == null) {
            return ReturnType.RTN_TYPE_NG;
        }

        return ReturnType.RTN_TYPE_OK;
    }


    private void setPermission(String objectUrl) throws Exception {
        // 유저 별 권한 설정
        Collection<Grant> grantCollection = new ArrayList<Grant>();

        // 소유주에게는 모든 권한
        Grant grant1 = new Grant(new CanonicalGrantee(s3.getS3AccountOwner().getId()), Permission.FullControl);
        grantCollection.add(grant1);

        // 모든 유저에게는 읽기 권한만 부여
        Grant grant2 = new Grant(GroupGrantee.AllUsers, Permission.Read);
        grantCollection.add(grant2);

        AccessControlList bucketAcl = s3.getBucketAcl(bucket);

        bucketAcl.getGrantsAsList().clear();
        bucketAcl.getGrantsAsList().addAll(grantCollection);

        // 저장한 파일에 권한을 설정한다.
        s3.setObjectAcl(bucket,objectUrl, bucketAcl);
    }

    /**
     *  AWS S3 스토리지에 파일 저장 하는 함수
     *
     *  S3 상의 버킷에 파일을 저장 한다.
     *  이 때, 외부에서 올려진 파일을 접근 할 수 있도록, 모든 유저에게 읽기 권한을 줘야 프론트
     *  에서 이미지를 확인 할 수 있다.
     *
     * @param file
     * @param subUrl
     * @param fileName
     * @return
     * @throws Exception
     */
    public ReturnType store(MultipartFile file, String subUrl , String fileName, boolean isImage) throws Exception{

        String fullFileName = subUrl + "/"+ fileName;
        logger.info("[StoreFile] FileName:" + fullFileName);
        InputStream isThumnailOriginal = file.getInputStream();

        PutObjectResult putObjectResult = s3.putObject(bucket, fullFileName, file.getInputStream(), getObjectMetadata(file));

        // Set permisstion
        this.setPermission(fullFileName);

        return ReturnType.RTN_TYPE_OK;
    }

    /**
     *  파일 삭제.
     *
     *  AWS S3 서버에 올려진 파일을 삭제한다.
     *
     * @param subUrl
     * @param fileName
     * @return
     * @throws Exception
     */
    public ReturnType delete(String subUrl , String fileName ) throws Exception {
        String fullFileName = subUrl + "/"+ fileName;

        logger.info("[Delete] :" + fullFileName);
        s3.deleteObject(bucket,fullFileName);

        try {
            s3.deleteObject(bucket,subUrl + "/"+ "thumnail" + "/" + fileName);
        }
        catch (Exception e) {
            ;
        }

        return ReturnType.RTN_TYPE_OK;
    }

    /**
     *  파일 다운로드
     *
     *  AWS S3 서버에 올려진 파일을 다운로드 한다.
     *
     * @param subUrl
     * @param fileName
     * @return
     * @throws Exception
     */
    public FileDownloadRes download(String subUrl , String fileName ) throws Exception {
        String fullFileName = subUrl + "/"+ fileName;

        S3Object object = s3.getObject(bucket, fullFileName);

        ObjectMetadata objectMetadata = object.getObjectMetadata();

        FileDownloadRes fileDownloadRes = new FileDownloadRes();

        fileDownloadRes.setInputStreamResource(new InputStreamResource(object.getObjectContent()));
        fileDownloadRes.setContentType(objectMetadata.getContentType());
        fileDownloadRes.setContentLength(objectMetadata.getContentLength());

        return fileDownloadRes;
    }

}
