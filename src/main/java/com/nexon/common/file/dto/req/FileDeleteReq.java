package com.nexon.common.file.dto.req;

import com.nexon.common.file.dto.model.FileDeleteInfo;
import lombok.Data;

import java.util.List;

@Data
public class FileDeleteReq {

    // 필수 입력값 - 카테고리
    int category;

    // 파일 그룹 시퀀스로 지울 때, 이때는 하기의 설정 들이 무시 된다.
    // 파일 그룹 시퀀스 지정의 경우 자동으로 "isSaveInfos"는 true로 취급된다.
    int fileGrpSeq;

    // File Seq로 지울 때, 해당 값이 false 면 파일이름으로 지운다.
    Boolean isSaveInfos;
    List<FileDeleteInfo> fileDeleteInfos;

}
