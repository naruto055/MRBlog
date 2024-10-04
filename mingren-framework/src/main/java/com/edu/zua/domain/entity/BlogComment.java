package com.edu.zua.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 评论表(BlogComment)表实体类
 *
 * @author makejava
 * @since 2024-04-04 17:43:04
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("blog_comment")
@ApiModel(description = "添加评论实体类")
public class BlogComment {

    @TableId
    private Long id;
    // 评论类型（0代表文章评论，1代表友链评论）
    private String type;
    // 文章id
    private Long articleId;
    // 根评论id
    private Long rootId;
    // 评论内容
    private String content;
    // 所回复的目标评论的userid
    private Long toCommentUserId;
    // 回复目标评论id
    private Long toCommentId;

    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    // 删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;

}

