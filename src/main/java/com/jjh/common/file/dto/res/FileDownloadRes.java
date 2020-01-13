package com.jjh.common.file.dto.res;

import lombok.Data;
import org.springframework.core.io.InputStreamResource;

@Data
public class FileDownloadRes {
    InputStreamResource inputStreamResource;
    String contentType;
    long contentLength;
    String orgFileName;
}
