package com.edu.zua.runner;

import com.edu.zua.constant.Constants;
import com.edu.zua.domain.entity.BlogArticle;
import com.edu.zua.mapper.BlogArticleMapper;
import com.edu.zua.utils.RedisCache;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {
    @Resource
    private BlogArticleMapper articleMapper;

    @Resource
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        // 查询博客信息  id  viewCount
        List<BlogArticle> articles = articleMapper.selectList(null);
        Map<String, Integer> viewCountMap = articles.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(), article -> {
                    return article.getViewCount().intValue();
                }));
        // 存储到redis中
        redisCache.setCacheMap(Constants.REDIS_VIEW_COUNT_KEY, viewCountMap);
    }
}
