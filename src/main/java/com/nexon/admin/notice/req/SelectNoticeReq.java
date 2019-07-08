package com.nexon.admin.notice.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SelectNoticeReq {
    private String keyField;
    private String keyWord;

    @ApiParam(value = "페이지 조회 개수", required = true)
    private int currentPage;

    @ApiParam(value = "노출 게시물 개수", required = true)
    @NotNull
    private int cntPerPage;

    @JsonIgnore
    @ApiParam(value = "시작 번호")
    private int startRow;

    public void setStartRow() {
        this.startRow = (this.currentPage - 1) * this.cntPerPage;
    }
}
