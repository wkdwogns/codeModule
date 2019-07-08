package com.nexon.common.file.dto.model;

import lombok.Data;

@Data
public class FileResultInfo {

    int fileGrpSeq;
    int fileSeq;

    String fileUrl;
    String fileSubUrl;
    String fileOriginName;
    String fileSysName;
}
