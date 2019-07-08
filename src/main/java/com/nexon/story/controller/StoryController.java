package com.nexon.story.controller;

import com.nexon.common.dto.res.ResponseHandler;
import com.nexon.story.dto.req.SelectStoryDetailReq;
import com.nexon.story.dto.req.StoryListReq;
import com.nexon.story.dto.res.SelectStoryDetailRes;
import com.nexon.story.dto.res.StoryListImportRes;
import com.nexon.story.dto.res.StoryListRes;
import com.nexon.story.service.StoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/story")
public class StoryController {

    @Autowired
    private StoryService storyService;

    @ApiOperation(value = "스토리 목록")
    @GetMapping
    public ResponseHandler<StoryListRes> selectStoryList(StoryListReq req) {
        ResponseHandler<StoryListRes> result = storyService.selectStoryList(req);
        return  result;
    }

    @ApiOperation(value = "스토리 목록")
    @GetMapping("/important")
    public ResponseHandler<StoryListImportRes> selectStoryImportantList() {
        ResponseHandler<StoryListImportRes> result = storyService.selectStoryImportantList();
        return  result;
    }

    @ApiOperation(value = "스토리 상세")
    @GetMapping("/detail")
    public ResponseHandler<SelectStoryDetailRes> selectStoryDetail(SelectStoryDetailReq req) {
        ResponseHandler<SelectStoryDetailRes> result = storyService.selectStoryDetail(req);
        return  result;
    }

    /*
    * 스토리 미리보기(관리자에서 호출)
    * */
    @ApiOperation(value = "스토리 상세")
    @GetMapping("/preview")
    public ResponseHandler<SelectStoryDetailRes> selectStoryPreveiw(SelectStoryDetailReq req, HttpServletRequest request) {
        ResponseHandler<SelectStoryDetailRes> result = storyService.selectStoryPreveiw(req, request);
        return  result;
    }

}
