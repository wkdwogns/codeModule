package com.jjh.common.file.dto.model;

import lombok.Data;

@Data
public class FileInfo {

    // File Grp Info
    String grpNm;
    int category;

    // File Detail info
    int fileSeq;
    int fileGrpSeq;
    String orgFileNm;
    String sysFileNm;
    int orderNo;
    String filePath;
    String type;
    String subType;

    // Common info
    String delYn;
    int creId;
    int updId;
}
