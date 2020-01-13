package com.jjh.common.file.dto.req;

import lombok.Data;

@Data
public class FileZipDownloadReq {

    // 파일 카테고리를 선택 한다. configFile.properties 참고
    int category;

    int fileGrpSeq;

    String zipFileName;
}
