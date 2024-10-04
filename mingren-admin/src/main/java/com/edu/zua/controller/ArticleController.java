package com.edu.zua.controller;

import com.edu.zua.annotation.SystemLog;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.dto.ArticleDto;
import com.edu.zua.domain.vo.EditArticleVO;
import com.edu.zua.domain.vo.PageVO;
import com.edu.zua.service.BlogArticleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Resource
    private BlogArticleService articleService;

    /**
     * 新增博客文章
     * @param articleDto
     * @return
     */
    @SystemLog(businessName = "发布推文")
    @PostMapping
    public ResponseResult add(@RequestBody ArticleDto articleDto) {
        return articleService.add(articleDto);
    }

    /**
     * 分页查询所有文章
     * @param articleDto
     * @return
     */
    @SystemLog(businessName = "后台查询所有推文")
    @GetMapping("/list")
    public ResponseResult listAllArticle(ArticleDto articleDto) {
        PageVO articleList = articleService.listAllArticle(articleDto);
        return ResponseResult.okResult(articleList);
    }

    /**
     * 获取需要修改的文章
     * @param id
     * @return
     */
    @SystemLog(businessName = "加载被修改的文章")
    @GetMapping("{id}")
    public ResponseResult getArticleById(@PathVariable Integer id) {
        EditArticleVO result =  articleService.getArticleById(id);
        return ResponseResult.okResult(result);
    }

    /**
     * 修改文章
     * @param editArticleVO
     * @return
     */
    @SystemLog(businessName = "修改推文")
    @PutMapping
    public ResponseResult editArticle(@RequestBody EditArticleVO editArticleVO) {
        Boolean result = articleService.editArticle(editArticleVO);
        return ResponseResult.okResult();
    }

    /**
     * 删除文章
     * @param id
     * @return
     */
    @SystemLog(businessName = "删除推文")
    @DeleteMapping("/{id}")
    public ResponseResult delArticle(@PathVariable Long id) {
        articleService.removeById(id);
        return ResponseResult.okResult();
    }
}
