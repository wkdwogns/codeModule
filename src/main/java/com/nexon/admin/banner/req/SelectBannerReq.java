package com.nexon.admin.banner.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiParam;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SelectBannerReq {
    @ApiParam(value = "검색조건")
    private String keyWord;

    @ApiParam(value = "배너타입 공통코드", hidden = true)
    @Ignore
    private String mstCd;

    @ApiParam(value = "배너 카테고리 - 상단, 컨텐츠", hidden = true)
    @Ignore
    private String category;

    @ApiParam(value = "노출여부", hidden = true)
    @Ignore
    private String viewYn;

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
