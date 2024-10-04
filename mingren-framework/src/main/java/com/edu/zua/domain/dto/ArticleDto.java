package com.edu.zua.domain.dto;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "添加文章dto")
public class ArticleDto {
    private Long id;
    // 标题
    private String title;
    // 文章内容
    private String content;
    // 文章摘要
    private String summary;
    // 所属分类id
    private Long categoryId;
    // 缩略图
    private String thumbnail;
    // 是否置顶（0否，1是）
    private String isTop;
    // 状态（0已发布，1草稿）
    private String status;
    // 是否允许评论 1是，0否
    private String isComment;
    private List<Long> tags;

    private Integer pageNum;
    private Integer pageSize;
}
