package com.edu.zua.domain.vo;

import com.edu.zua.domain.entity.Role;
import com.edu.zua.domain.entity.UserRole;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "修改用户信息vo")
@Accessors(chain = true)
public class EditUserDetailVO {
    private List<Long> roleIds;
    private List<Role> roles;
    private String phonenumber;
    private UserInfoVO user;

    public EditUserDetailVO(List<Long> roleIds, List<Role> roleList, UserInfoVO userInfoVO) {
        this.roleIds = roleIds;
        this.roles = roleList;
        this.user = userInfoVO;
    }
}
