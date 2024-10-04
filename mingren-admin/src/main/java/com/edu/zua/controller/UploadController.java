package com.edu.zua.controller;

import com.edu.zua.annotation.SystemLog;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.service.UploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
public class UploadController {
    @Resource
    private UploadService uploadService;

    /**
     * 写文章的时候上传图片以及文章的缩略图
     * @param img
     * @return
     */
    @PostMapping("/upload")
    public ResponseResult uploadImg(@RequestParam("img") MultipartFile img) {
        try {
            return uploadService.uploadImg(img);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("文件上传失败");
        }
    }
}
