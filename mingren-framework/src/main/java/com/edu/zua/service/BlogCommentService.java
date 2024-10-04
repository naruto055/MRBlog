package com.edu.zua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.entity.BlogComment;

/**
 * 评论表(BlogComment)表服务接口
 *
 * @author makejava
 * @since 2024-04-04 17:43:04
 */
public interface BlogCommentService extends IService<BlogComment> {

    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(BlogComment comment);
}

