package com.edu.zua.domain.vo;

import com.edu.zua.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RoleMenuTreeSelectVo {
    private List<Long> checkedKeys;

    private List<MenuTreeVO> menus;
}
