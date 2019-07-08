package com.nexon.common.file.dto.res;

import com.nexon.common.file.dto.model.FileResultInfo;
import lombok.Data;

import java.util.List;

@Data
public class FileInfoRes {
    List<FileResultInfo> fileInfos;
    int fileGrpSeq;
    boolean checkExtension; //true:정상파일
}
