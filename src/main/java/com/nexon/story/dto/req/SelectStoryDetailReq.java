package com.nexon.story.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
public class SelectStoryDetailReq {
    private String seq;
    @ApiParam(value = "스토리 미리보기", hidden = true)
    @JsonIgnore
    private Boolean isPreview = false;
    private String viewStDt;
    private String prev;
    private String next;
}
