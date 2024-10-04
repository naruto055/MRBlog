package com.edu.zua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.zua.constant.Constants;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.dto.ArticleDto;
import com.edu.zua.domain.entity.Tag;
import com.edu.zua.domain.vo.EditArticleVO;
import com.edu.zua.domain.entity.ArticleTag;
import com.edu.zua.domain.entity.BlogArticle;
import com.edu.zua.domain.entity.BlogCategory;
import com.edu.zua.domain.vo.ArticleDetailVO;
import com.edu.zua.domain.vo.ArticleListVO;
import com.edu.zua.domain.vo.HotArticleVO;
import com.edu.zua.domain.vo.PageVO;
import com.edu.zua.mapper.BlogArticleMapper;
import com.edu.zua.service.ArticleTagService;
import com.edu.zua.service.BlogArticleService;
import com.edu.zua.service.BlogCategoryService;
import com.edu.zua.utils.BeanCopyUtils;
import com.edu.zua.utils.RedisCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BlogArticleServiceImpl extends ServiceImpl<BlogArticleMapper, BlogArticle> implements BlogArticleService {

    @Resource
    private BlogCategoryService blogCategoryService;

    @Resource
    private RedisCache redisCache;

    @Resource
    private ArticleTagService articleTagService;

    /**
     * 获取热门文章列表，展示10条
     *
     * @return
     */
    @Override
    public ResponseResult hotArticleList() {
        LambdaQueryWrapper<BlogArticle> queryWrapper = new LambdaQueryWrapper<>();
        // 文章必须是发布的文章，不是草稿
        queryWrapper.eq(BlogArticle::getStatus, Constants.ARTICLE_STATUS_NORMAL);
        // 按照浏览量进行排序
        queryWrapper.orderByDesc(BlogArticle::getViewCount);
        // 最多查询10条
        Page<BlogArticle> page = new Page<>(1, Constants.PAGE_SIZE);
        page(page, queryWrapper);
        List<BlogArticle> articles = page.getRecords();

        for (BlogArticle article : articles) {
            // 从redis中获取viewCount
            Integer viewCount = redisCache.getCacheMapValue(Constants.REDIS_VIEW_COUNT_KEY, article.getId().toString());
            if(viewCount == null) {
                article.setViewCount(0L);
            } else {
                article.setViewCount(Long.valueOf(viewCount));
            }
        }


        // bean拷贝
        /*for (BlogArticle article : articles) {
            HotArticleVO vo = new HotArticleVO();
            BeanUtils.copyProperties(article, vo);
            articleVos.add(vo);
        }*/
        List<HotArticleVO> vs = BeanCopyUtils.copyBeanList(articles, HotArticleVO.class);
        return ResponseResult.okResult(vs);
    }

    /**
     * 分页查询文章
     *
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */
    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        // 查询条件
        LambdaQueryWrapper<BlogArticle> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 判断categoryId
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0, BlogArticle::getCategoryId, categoryId);
        // 正式发布的文章
        lambdaQueryWrapper.eq(BlogArticle::getStatus, Constants.ARTICLE_STATUS_NORMAL);
        // 是否是置顶文章，即对isTop字段进行排序
        lambdaQueryWrapper.orderByDesc(BlogArticle::getIsTop);

        // 分页查询
        Page<BlogArticle> page = new Page<>(pageNum, pageSize);
        page(page, lambdaQueryWrapper);

        // 查询分类的名称
        List<BlogArticle> articles = page.getRecords();
        // 根据articleId查询categoryName
        articles = articles.stream()
                .map(blogArticle -> {
                    // 获取分类id，查询分类信息，获取分类名称
                    if (blogArticle.getCategoryId() != null) {
                        blogArticle.setCategoryName(blogCategoryService.getById(blogArticle.getCategoryId()).getName());
                    } else {
                        blogArticle.setCategoryName("无");
                    }

                    // 从redis中获取viewCount
                    Integer viewCount = redisCache.getCacheMapValue(Constants.REDIS_VIEW_COUNT_KEY, blogArticle.getId().toString());
                    if (viewCount == null) blogArticle.setViewCount(0L);
                    else blogArticle.setViewCount(Long.valueOf(viewCount));

                    return blogArticle;
                }).collect(Collectors.toList());
        /*for (BlogArticle article : articles) {
            BlogCategory category = blogCategoryService.getById(article.getCategoryId());
            article.setCategoryName(category.getName());
        }*/


        // 封装成VO
        List<ArticleListVO> listVOS = BeanCopyUtils.copyBeanList(articles, ArticleListVO.class);
        PageVO pageVO = new PageVO(listVOS, page.getTotal());

        return ResponseResult.okResult(pageVO);
    }

    /**
     * 根据id查询文章详情
     * @param id
     * @return
     */
    @Override
    public ResponseResult getArticleDetail(Long id) {
        // 根据id查询
        BlogArticle article = getById(id);

        // 从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue(Constants.REDIS_VIEW_COUNT_KEY, id.toString());
        article.setViewCount(Long.valueOf(viewCount));

        // 转换成VO
        ArticleDetailVO articleDetailVO = BeanCopyUtils.copyBean(article, ArticleDetailVO.class);
        // 根据分类id查询分类名
        BlogCategory category = blogCategoryService.getById(articleDetailVO.getCategoryId());
        if (category != null) {
            articleDetailVO.setCategoryName(category.getName());
        }
        // 封装响应返回
        return ResponseResult.okResult(articleDetailVO);
    }

    /**
     * 更新redis中的对应文章的浏览量
     * @param id
     * @return
     */
    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.incrementCacheMapValue(Constants.REDIS_VIEW_COUNT_KEY, id.toString(), 1);
        return ResponseResult.okResult();
    }

    /**
     * 新增文章
     * @param articleDto
     * @return
     */
    @Override
    @Transactional
    public ResponseResult add(ArticleDto articleDto) {
        BlogArticle article = BeanCopyUtils.copyBean(articleDto, BlogArticle.class);
        article.setViewCount(0L);
        save(article);
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        // 添加博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public PageVO listAllArticle(ArticleDto articleDto) {
        LambdaQueryWrapper<BlogArticle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(articleDto.getTitle()), BlogArticle::getTitle, articleDto.getTitle());
        queryWrapper.like(StringUtils.hasText(articleDto.getSummary()), BlogArticle::getSummary, articleDto.getSummary());
        queryWrapper.eq(BlogArticle::getStatus, Constants.ARTICLE_STATUS_NORMAL);
        Page<BlogArticle> page = new Page<>(articleDto.getPageNum(), articleDto.getPageSize());
        page(page, queryWrapper);
        List<BlogArticle> articles = page.getRecords();
        return new PageVO(articles, page.getTotal());
    }

    @Override
    public EditArticleVO getArticleById(Integer id) {
        // 先查文章
        BlogArticle article = getById(id);
        EditArticleVO editArticleVO = BeanCopyUtils.copyBean(article, EditArticleVO.class);
        // 查出来文章之后，把文章对应的标签也查出来，封装为VO返回
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(ArticleTag::getTagId);
        queryWrapper.eq(ArticleTag::getArticleId, article.getId());
        List<ArticleTag> list = articleTagService.list(queryWrapper);
        for (ArticleTag articleTag : list) {
            // 将标签的tagId添加到editArticleVO的tags属性中
            editArticleVO.getTags().add(articleTag.getTagId());
        }
        return editArticleVO;
    }

    @Override
    public Boolean editArticle(EditArticleVO editArticleVO) {
        BlogArticle article = BeanCopyUtils.copyBean(editArticleVO, BlogArticle.class);
        //更新博客信息
        updateById(article);
        //删除原有的 标签和博客的关联
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,article.getId());
        articleTagService.remove(articleTagLambdaQueryWrapper);
        //添加新的博客和标签的关联信息
        List<ArticleTag> articleTags = editArticleVO.getTags().stream()
                .map(tagId -> new ArticleTag(editArticleVO.getId(), tagId))
                .collect(Collectors.toList());
        boolean b = articleTagService.saveBatch(articleTags);
        return b;
    }
}
