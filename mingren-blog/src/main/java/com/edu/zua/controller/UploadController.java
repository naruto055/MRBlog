package com.edu.zua.controller;

import com.edu.zua.domain.ResponseResult;
import com.edu.zua.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@Api(tags = "上传文件", description = "文件上传相关接口")
public class UploadController {

    @Resource
    private UploadService uploadService;

    /**
     * 用户上传头像
     * @param img
     * @return
     */
    @ApiOperation(value = "上传头像")
    @ApiImplicitParam(name = "img", value = "用户头像")
    @PostMapping("/upload")
    public ResponseResult uploadImg(MultipartFile img) {
        return uploadService.uploadImg(img);
    }
}
