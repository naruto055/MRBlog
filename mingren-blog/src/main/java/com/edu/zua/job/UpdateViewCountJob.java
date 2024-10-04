package com.edu.zua.job;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.edu.zua.constant.Constants;
import com.edu.zua.domain.entity.BlogArticle;
import com.edu.zua.service.BlogArticleService;
import com.edu.zua.utils.RedisCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Component
public class UpdateViewCountJob {

    @Resource
    private RedisCache redisCache;

    @Resource
    private BlogArticleService articleService;

    @Scheduled(cron = "0/55 * * * * ?")
    public void updateViewCount() {
        // 获取redis中的浏览量
        Map<String, Integer> cacheMap = redisCache.getCacheMap(Constants.REDIS_VIEW_COUNT_KEY);
        List<BlogArticle> articles = cacheMap.entrySet().stream()
                .map(entry -> new BlogArticle(Long.valueOf(entry.getKey()), Long.valueOf(entry.getValue())))
                .collect(Collectors.toList());
        // 更新到数据库中
        for (BlogArticle article : articles) {
            LambdaUpdateWrapper<BlogArticle> queryWrapper = new LambdaUpdateWrapper<>();
            queryWrapper.eq(BlogArticle::getId, article.getId());
            queryWrapper.set(BlogArticle::getViewCount, article.getViewCount());
            articleService.update(queryWrapper);
        }
    }
}
