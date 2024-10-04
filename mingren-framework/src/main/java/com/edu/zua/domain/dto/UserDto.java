package com.edu.zua.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "查询所有用户dto")
public class UserDto {
    private Integer pageNum;
    private Integer pageSize;
    private String userName;
    private String phonenumber;
    private String  status;
}
