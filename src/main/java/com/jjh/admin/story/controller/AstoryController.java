package com.jjh.admin.story.controller;

import com.jjh.admin.story.req.*;
import com.jjh.admin.story.res.SelectStoryDetailRes;
import com.jjh.admin.story.res.SelectStoryRes;
import com.jjh.admin.story.service.AstoryService;
import com.jjh.common.dto.res.ResponseHandler;
import com.jjh.common.session.SessionCheck;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/mapi/story")
public class AstoryController {

    @Autowired
    private AstoryService aStoryService;

    @ApiOperation(value = "스토리 목록")
    @GetMapping
    public ResponseHandler<SelectStoryRes> selectStory(SelectStoryReq req) {
        ResponseHandler<SelectStoryRes> result = aStoryService.selectStory(req);
        return  result;
    }

    @ApiOperation(value = "스토리 상세")
    @GetMapping("/detail")
    public ResponseHandler<SelectStoryDetailRes> selectStoryDetail(SelectStoryDetailReq req) {
        ResponseHandler<SelectStoryDetailRes> result = aStoryService.selectStoryDetail(req);
        return  result;
    }

    @ApiOperation(value = "스토리 쓰기")
    @PostMapping
    @SessionCheck
    public ResponseHandler<?> insertStory(InsertStoryReq req) {
        System.out.println(req);

        ResponseHandler<?> result = aStoryService.insertStory(req);
        return  result;
    }

    @ApiOperation(value = "스토리 수정")
    @PostMapping("/edit")
    @SessionCheck
    public ResponseHandler<?> updateStory(UpdateStoryReq req) {
        ResponseHandler<?> result = aStoryService.updateStory(req);
        return  result;
    }

    @ApiOperation(value = "스토리 삭제")
    @DeleteMapping
    public ResponseHandler<?> deleteStory(@RequestBody(required = false) DeleteStoryReq req) {
        ResponseHandler<?> result = aStoryService.deleteStory(req);
        return  result;
    }

}
