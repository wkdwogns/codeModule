package com.nexon.common.file.dto.req;

import lombok.Data;

@Data
public class FileListReq {

    public FileListReq() {}
    public FileListReq(Integer fileGrpSeq) {
        this.fileGrpSeq = fileGrpSeq;
    }

    Integer fileGrpSeq;
    Integer fileSeq;


}
