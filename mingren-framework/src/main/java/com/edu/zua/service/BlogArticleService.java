package com.edu.zua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.dto.ArticleDto;
import com.edu.zua.domain.vo.EditArticleVO;
import com.edu.zua.domain.entity.BlogArticle;
import com.edu.zua.domain.vo.PageVO;

/**
 * 文章表(BlogArticle)表服务接口
 *
 * @author makejava
 * @since 2024-04-02 21:51:42
 */
public interface BlogArticleService extends IService<BlogArticle> {

    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(ArticleDto blogArticle);

    PageVO listAllArticle(ArticleDto articleDto);

    EditArticleVO getArticleById(Integer id);

    Boolean editArticle(EditArticleVO editArticleVO);
}

