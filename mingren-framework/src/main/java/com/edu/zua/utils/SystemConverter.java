package com.edu.zua.utils;

import com.edu.zua.domain.entity.Menu;
import com.edu.zua.domain.vo.MenuTreeVO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SystemConverter {
    private SystemConverter() {
    }


    public static List<MenuTreeVO> buildMenuSelectTree(List<Menu> menus) {
        List<MenuTreeVO> MenuTreeVOs = menus.stream()
                .map(m -> new MenuTreeVO(m.getId(), m.getMenuName(), m.getParentId(), null))
                .collect(Collectors.toList());
        List<MenuTreeVO> options = MenuTreeVOs.stream()
                .filter(o -> o.getParentId().equals(0L))
                .map(o -> o.setChildren(getChildList(MenuTreeVOs, o)))
                .collect(Collectors.toList());


        return options;
    }


    /**
     * 得到子节点列表
     */
    private static List<MenuTreeVO> getChildList(List<MenuTreeVO> list, MenuTreeVO option) {
        List<MenuTreeVO> options = list.stream()
                .filter(o -> Objects.equals(o.getParentId(), option.getId()))
                .map(o -> o.setChildren(getChildList(list, o)))
                .collect(Collectors.toList());
        return options;

    }
}
