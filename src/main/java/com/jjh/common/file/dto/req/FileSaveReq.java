package com.jjh.common.file.dto.req;

import com.jjh.common.file.dto.model.FileDetailInfo;
import lombok.Data;

import java.util.List;

@Data
public class FileSaveReq {

    //파일 별 디테일 정보를 저장 한다. (선택)
    List<FileDetailInfo> fileDetailInfos;

    // 파일 카테고리를 선택 한다. configFile.properties 참고
    int category;

    // 파일 정보 DB 저장 여부를 파악 한다.
    Boolean isSaveInfos;

    // 현존하는 그룹이 없을 경우 0으로 설정, 이미 존재하는 그룹에 저장할 경우 해당 번호를 입력 한다.
    int fileGrpSeq;

    // 그룹을 만들어야 할 경우, 그룹명 지정
    String grpNm;

    int checkExtCategory;

}
