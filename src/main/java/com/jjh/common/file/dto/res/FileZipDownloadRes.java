package com.jjh.common.file.dto.res;

import lombok.Data;

import java.io.ByteArrayOutputStream;

@Data
public class FileZipDownloadRes {
    ByteArrayOutputStream outputStreamResource;
}
