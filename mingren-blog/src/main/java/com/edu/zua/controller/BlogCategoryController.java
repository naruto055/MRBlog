package com.edu.zua.controller;

import com.edu.zua.annotation.SystemLog;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.service.BlogCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/category")
@Api(tags = "分类", description = "分类相关接口")
public class BlogCategoryController {

    @Resource
    private BlogCategoryService blogCategoryService;

    /**
     * 查询文章的分类数据
     * @return
     */
    @SystemLog(businessName = "查询文章的分类")
    @ApiOperation(value = "显示分类", notes = "显示所有的分类")
    @GetMapping("/getCategoryList")
    public ResponseResult getCategoryList() {
        return blogCategoryService.getCategoryList();
    }
}
