package com.edu.zua.mapper;

import com.edu.zua.domain.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author wenqunsheng
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Mapper
* @createDate 2024-04-11 16:12:15
* @Entity com.edu.zua.domain.entity.Menu
*/
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(@Param("userId") Long userId);

    List<Menu> selectAllRouterMenu();

    List<Menu> selectRouterMenuTressByUserId(Long userId);

    List<Long> selectMenuListByRoleId(Long id);
}




