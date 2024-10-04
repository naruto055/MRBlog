package com.edu.zua.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkVO {
    @TableId
    private Long id;

    private String name;

    private String logo;
    // 网站地址
    private String address;
}
