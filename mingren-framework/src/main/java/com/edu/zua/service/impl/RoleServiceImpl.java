package com.edu.zua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.zua.domain.dto.RoleDto;
import com.edu.zua.domain.entity.Role;
import com.edu.zua.domain.entity.RoleMenu;
import com.edu.zua.domain.vo.PageVO;
import com.edu.zua.service.RoleMenuService;
import com.edu.zua.service.RoleService;
import com.edu.zua.mapper.RoleMapper;
import com.edu.zua.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wenqunsheng
 * @description 针对表【sys_role(角色信息表)】的数据库操作Service实现
 * @createDate 2024-04-11 16:17:50
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
        implements RoleService {

    @Resource
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {

        // 判断是否是管理员，如果是返回集合中只需要有admin即可
        if (id == 1L) {
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        // 否则查询用户所具有的的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public PageVO listAllRolesByPage(RoleDto roleDto) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(roleDto.getRoleName()), Role::getRoleName, roleDto.getRoleName());
        queryWrapper.eq(StringUtils.hasText(roleDto.getStatus()), Role::getStatus, roleDto.getStatus());
        queryWrapper.orderByAsc(Role::getRoleSort);
        Page<Role> rolePage = new Page<>(roleDto.getPageNum(), roleDto.getPageSize());
        page(rolePage, queryWrapper);
        List<Role> records = rolePage.getRecords();
        return new PageVO(records, rolePage.getTotal());
    }

    @Override
    public void changeStatus(RoleDto roleDto) {
        Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getId, roleDto.getRoleId());
        update(role, queryWrapper);
    }

    @Override
    @Transactional
    public void addRole(Role role) {
        save(role);
        // System.out.println(role.getId());
        if (role.getMenuIds() != null && role.getMenuIds().length > 0) {
            insertRoleMenu(role);
        }
    }

    @Override
    public void editRole(Role role) {
        // 先将角色修改了
        updateById(role);
        // 在修改该角色对应的菜单列表
        // 先删除
        roleMenuService.deleteRoleMenuByRoleId(role.getId());
        // 后插入
        insertRoleMenu(role);
    }

    private void insertRoleMenu(Role role) {
        List<RoleMenu> roleMenuList = Arrays.stream(role.getMenuIds())
                .map(memuId -> new RoleMenu(role.getId(), memuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenuList);
    }
}




