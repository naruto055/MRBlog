package com.edu.zua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.zua.constant.Constants;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.dto.GetAllCategoryDto;
import com.edu.zua.domain.entity.BlogArticle;
import com.edu.zua.domain.entity.BlogCategory;
import com.edu.zua.domain.vo.BlogCategoryVO;
import com.edu.zua.domain.vo.PageVO;
import com.edu.zua.mapper.BlogCategoryMapper;
import com.edu.zua.service.BlogArticleService;
import com.edu.zua.service.BlogCategoryService;
import com.edu.zua.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(BlogCategory)表服务实现类
 *
 * @author makejava
 * @since 2024-04-03 17:20:58
 */
@Service("blogCategoryService")
public class BlogCategoryServiceImpl extends ServiceImpl<BlogCategoryMapper, BlogCategory> implements BlogCategoryService {

    @Resource
    private BlogArticleService blogArticleService;

    /**
     * 获取分类列表数据
     * @return
     */
    @Override
    public ResponseResult getCategoryList() {
        // 1. 先查询文章表，状态为已发布的
        LambdaQueryWrapper<BlogArticle> articleQuery = new LambdaQueryWrapper<>();
        articleQuery.eq(BlogArticle::getStatus, Constants.ARTICLE_STATUS_NORMAL);
        List<BlogArticle> articleList = blogArticleService.list(articleQuery);

        // 2. 获取文章的分类id，并且去重
        Set<Long> categoryId = articleList.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());

        // 3. 查询分类表
        List<BlogCategory> categoryList = listByIds(categoryId);
        categoryList = categoryList.stream()
                .filter(category -> Constants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());

        // 4. 封装成vo
        List<BlogCategoryVO> categoryVos = BeanCopyUtils.copyBeanList(categoryList, BlogCategoryVO.class);

        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public List<BlogCategory> listAllCategory() {
        LambdaQueryWrapper<BlogCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlogCategory::getStatus, Constants.ARTICLE_STATUS_NORMAL);
        List<BlogCategory> list = list(queryWrapper);
        return list;
    }

    @Override
    public PageVO getAllCategory(GetAllCategoryDto getAllCategoryDto) {
        Page<BlogCategory> page = new Page<>(getAllCategoryDto.getPageNum(), getAllCategoryDto.getPageSize());
        LambdaQueryWrapper<BlogCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(getAllCategoryDto.getStatus()), BlogCategory::getStatus, Constants.STATUS_NORMAL);
        queryWrapper.eq(StringUtils.hasText(getAllCategoryDto.getName()), BlogCategory::getName, getAllCategoryDto.getName());
        page(page, queryWrapper);
        return new PageVO(page.getRecords(), page.getTotal());
    }

    @Override
    public void delCategory(String ids) {
        List<String> list = Arrays.asList(ids.split(","));
        List<Long> idsList = list.stream().map(id -> Long.parseLong(id))
                .collect(Collectors.toList());
        removeBatchByIds(idsList);
    }
}

