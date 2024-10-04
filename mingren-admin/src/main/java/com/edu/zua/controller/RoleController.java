package com.edu.zua.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.edu.zua.annotation.SystemLog;
import com.edu.zua.constant.Constants;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.dto.RoleDto;
import com.edu.zua.domain.entity.Role;
import com.edu.zua.domain.vo.PageVO;
import com.edu.zua.service.RoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    /**
     * 查询所有用户角色
     * @param roleDto
     * @return
     */
    @SystemLog(businessName = "查询所有角色")
    @GetMapping("/list")
    public ResponseResult listAllRolesByPage(RoleDto roleDto) {
        PageVO roleList = roleService.listAllRolesByPage(roleDto);
        return ResponseResult.okResult(roleList);
    }

    /**
     * 改变角色状态
     * @param roleDto
     * @return
     */
    @SystemLog(businessName = "修改角色状态")
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody RoleDto roleDto) {
        roleService.changeStatus(roleDto);
        return ResponseResult.okResult();
    }

    /**
     * 新增角色
     * @param role
     * @return
     */
    @SystemLog(businessName = "新增角色")
    @PostMapping
    public ResponseResult addRole(@RequestBody Role role) {
        roleService.addRole(role);
        return ResponseResult.okResult();
    }

    /**
     * 根据id查询角色详情
     * @param id
     * @return
     */
    @SystemLog(businessName = "查询角色详情")
    @GetMapping("/{id}")
    public ResponseResult getRoleById(@PathVariable Long id) {
        Role byId = roleService.getById(id);
        return ResponseResult.okResult(byId);
    }

    /**
     * 修改角色
     * @param role
     * @return
     */
    @SystemLog(businessName = "修改角色")
    @PutMapping
    public ResponseResult editRole(@RequestBody Role role) {
        roleService.editRole(role);
        return ResponseResult.okResult();
    }

    /**
     * 删除角色（逻辑删除）
     * @param id
     * @return
     */
    @SystemLog(businessName = "删除角色")
    @DeleteMapping("/{id}")
    public ResponseResult delRole(@PathVariable Long id) {
        roleService.removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * 新增用户的时候，为用户添加角色，需要把角色列表查出来展示在下拉框中
     * @return
     */
    @GetMapping("/listAllRole")
    public ResponseResult listAllRole() {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getStatus, Constants.STATUS_NORMAL);
        return ResponseResult.okResult(roleService.list(queryWrapper));
    }
}
