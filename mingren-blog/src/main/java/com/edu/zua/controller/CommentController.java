package com.edu.zua.controller;

import com.edu.zua.annotation.SystemLog;
import com.edu.zua.constant.Constants;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.dto.AddCommentDto;
import com.edu.zua.domain.entity.BlogComment;
import com.edu.zua.service.BlogCommentService;
import com.edu.zua.utils.BeanCopyUtils;
import io.swagger.annotations.*;
import org.apiguardian.api.API;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论", description = "评论相关接口")
public class CommentController {

    @Resource
    private BlogCommentService blogCommentService;

    /**
     * 分页查询评论
     * @param articleId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/commentList")
    @ApiOperation(value = "分页查询评论", notes = "分页查询评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleId", value = "要查询的文章id"),
            @ApiImplicitParam(name = "pageNum", value = "页码"),
            @ApiImplicitParam(name = "pageSize", value = "每页评论数量")
    })
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize) {
        return blogCommentService.commentList(Constants.ARTICLE_COMMENT, articleId, pageNum, pageSize);
    }

    /**
     * 评论某文章或回复某评论
     * @param addCommentDto
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增评论", notes = "评论某文章或回复某评论")
    @ApiParam(value = "addCommentDto", name = "新增评论dto")
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto) {
        BlogComment comment = BeanCopyUtils.copyBean(addCommentDto, BlogComment.class);
        return blogCommentService.addComment(comment);
    }

    /**
     * 添加友链评论
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "获取友链评论列表", notes = "获取友链评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码"),
            @ApiImplicitParam(name = "pageSize", value = "每页评论数量")
    })
    @SystemLog(businessName = "添加友链评论")
    @GetMapping("/linkCommentList")
    public ResponseResult linkCommentList(Integer pageNum, Integer pageSize) {
        return blogCommentService.commentList(Constants.LINK_COMMENT,null, pageNum, pageSize);
    }
}
