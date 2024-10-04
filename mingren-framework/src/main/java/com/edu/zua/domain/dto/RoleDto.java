package com.edu.zua.domain.dto;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "角色列表dto")
public class RoleDto {
    private Integer pageSize;
    private Integer pageNum;
    private String roleName;
    private String status;
    private Long roleId;
    private List<Long > menuIds;
    private String remark;
    private String roleKey;
    private Integer roleSort;
}
