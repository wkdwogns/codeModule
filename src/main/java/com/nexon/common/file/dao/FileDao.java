package com.nexon.common.file.dao;

import com.nexon.common.file.dto.model.FileInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface FileDao
{
    void insertFileGrp(Map req);
    void insertFile(Map req);
    void updateFile(Map req);
    void updateFileGrp(Map req);

    List<FileInfo> selectFile(Map req);

}
