package com.edu.zua.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 友链(BlogLink)表实体类
 *
 * @author makejava
 * @since 2024-04-03 21:27:32
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("blog_link")
public class BlogLink {

    @TableId
    private Long id;

    private String name;

    private String logo;

    private String description;
    // 网站地址
    private String address;
    // 审核状态 (0代表审核通过，1代表审核未通过，2代表未审核)
    private String status;

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

