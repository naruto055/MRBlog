package com.edu.zua.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogLoginVO {
    private String token;
    private UserInfoVO userInfo;
}
