package com.jjh.common.file.dto.req;

import lombok.Data;

@Data
public class FileDownloadReq {

    // 파일 카테고리를 선택 한다. configFile.properties 참고
    int category;

    int fileSeq;

    String fileName;

    String enc;

}
