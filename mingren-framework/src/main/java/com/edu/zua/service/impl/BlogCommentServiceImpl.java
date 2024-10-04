package com.edu.zua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.zua.constant.Constants;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.enums.AppHttpCodeEnum;
import com.edu.zua.domain.vo.CommentVO;
import com.edu.zua.domain.vo.PageVO;
import com.edu.zua.exception.SystemException;
import com.edu.zua.mapper.BlogCommentMapper;
import com.edu.zua.domain.entity.BlogComment;
import com.edu.zua.service.BlogUserService;
import com.edu.zua.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import com.edu.zua.service.BlogCommentService;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;


/**
 * 评论表(BlogComment)表服务实现类
 *
 * @author makejava
 * @since 2024-04-04 17:43:05
 */
@Service("blogCommentService")
public class BlogCommentServiceImpl extends ServiceImpl<BlogCommentMapper, BlogComment> implements BlogCommentService {

    @Resource
    private BlogUserService blogUserService;

    /**
     * 添加评论
     * @param comment
     * @return
     */
    @Override
    public ResponseResult addComment(BlogComment comment) {
        // 评论不能为空
        if (!StringUtils.hasText(comment.getContent())) {
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }

    /**
     * 查询对应文章的根评论
     *
     * @param commentType 评论类型
     * @param articleId 文章id
     * @param pageNum 页码
     * @param pageSize 每页展示评论跳数
     * @return
     */
    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        // 查询对应文章的根评论
        LambdaQueryWrapper<BlogComment> queryWrapper = new LambdaQueryWrapper<>();

        // 对articleId进行判断
        queryWrapper.eq(Constants.ARTICLE_COMMENT.equals(commentType),BlogComment::getArticleId, articleId);

        // 根评论 rootId = -1
        queryWrapper.eq(BlogComment::getRootId, Constants.ROOT_COMMENT);
        // 评论类型
        queryWrapper.eq(BlogComment::getType, commentType);

        // 分页查询
        Page<BlogComment> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        // 封装为CommentVO对象
        List<CommentVO> commentVOList = toCommentVOList(page.getRecords());

        // 查询所有根评论对应的子评论集合，并且赋值给对应的属性
        for (CommentVO vo : commentVOList) {
            // 查询对应的子评论
            List<CommentVO> children = getChildren(vo.getId());
            // 赋值
            vo.setChildren(children);
        }

        return ResponseResult.okResult(new PageVO(commentVOList, page.getTotal()));
    }

    /**
     * 根据根评论的id查询所有对应的子评论
     * @param id
     * @return
     */
    private List<CommentVO> getChildren(Long id) {
        LambdaQueryWrapper<BlogComment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlogComment::getRootId, id);
        queryWrapper.orderByAsc(BlogComment::getCreateTime);
        List<BlogComment> list = list(queryWrapper);
        return toCommentVOList(list);
    }

    /**
     * 将查询出来的评论封装成CommentVO对象
     * @param list
     * @return
     */
    private List<CommentVO> toCommentVOList(List<BlogComment> list) {
        List<CommentVO> vos = BeanCopyUtils.copyBeanList(list, CommentVO.class);
        for (CommentVO vo : vos) {
            // 通过creatBy查询用户的昵称并赋值
            String nickName = blogUserService.getById(vo.getCreateBy()).getNickName();
            vo.setUsername(nickName);

            // 通过toCommentUserId查询用户的昵称并赋值
            if (vo.getToCommentUserId() != -1) {
                String toCommentUserName = blogUserService.getById(vo.getToCommentUserId()).getNickName();
                vo.setToCommentUserName(toCommentUserName);
            }
        }

        return vos;
    }
}

