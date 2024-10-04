package com.edu.zua.controller;

import com.edu.zua.annotation.SystemLog;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.service.BlogArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
@Api(tags = "文章", description = "文章相关接口")
public class BlogArticleController {

    @Autowired
    private BlogArticleService blogArticleService;

    /**
     * 获取热门文章
     * @return
     */
    @SystemLog(businessName = "查询热门文章")
    @GetMapping("/hotArticleList")
    @ApiOperation(value = "查询热门文章", notes = "查询热门文章")
    public ResponseResult hotArticle() {
        // 查询热门文章，封装成ResponseResult返回
        return blogArticleService.hotArticleList();
    }

    /**
     * 首页中分页查询文章，分类查询
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */
    @SystemLog(businessName = "分页查询前台首页中所有的文章")
    @ApiOperation(value = "分页查询文章", notes = "分页查询首页中的文章或者按照分类查询文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码"),
            @ApiImplicitParam(name = "pageSize", value = "每页评论数量"),
            @ApiImplicitParam(name = "categoryId", value = "分类id")
    })
    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        return blogArticleService.articleList(pageNum, pageSize, categoryId);
    }

    /**
     * 根据id查询文章的详情
     * @param id
     * @return
     */
    @SystemLog(businessName = "阅读文章")
    @GetMapping("/{id}")
    @ApiOperation(value = "查询文章详情", notes = "根据id查询文章详情")
    @ApiImplicitParam("文章id")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id) {
        return blogArticleService.getArticleDetail(id);
    }

    /**
     * 更新浏览量
     * @param id
     * @return
     */
    @SystemLog(businessName = "阅读量+1")
    @PutMapping("/updateViewCount/{id}")
    @ApiImplicitParam("文章id")
    @ApiOperation(value = "更新阅读量", notes = "阅读量+1")
    public ResponseResult updateViewCount(@PathVariable("id") Long id) {
        return blogArticleService.updateViewCount(id);
    }
}
