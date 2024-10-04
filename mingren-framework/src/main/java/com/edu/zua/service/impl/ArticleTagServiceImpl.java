package com.edu.zua.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.zua.domain.entity.ArticleTag;
import com.edu.zua.service.ArticleTagService;
import com.edu.zua.mapper.ArticleTagMapper;
import org.springframework.stereotype.Service;

/**
* @author wenqunsheng
* @description 针对表【blog_article_tag(文章标签关联表)】的数据库操作Service实现
* @createDate 2024-04-15 16:52:40
*/
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag>
    implements ArticleTagService{

}




