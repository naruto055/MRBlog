package com.edu.zua.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 返回给前端的封装类，因为不需要将整个实体类封装并且返回，我们要不了那么多的数据
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotArticleVO {
    private Long Id;
    // 标题
    private String title;
    // 访问量
    private Long viewCount;
}
