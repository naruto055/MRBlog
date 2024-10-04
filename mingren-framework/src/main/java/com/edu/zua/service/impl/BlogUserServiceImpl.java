package com.edu.zua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.dto.AddUserDto;
import com.edu.zua.domain.dto.UserDto;
import com.edu.zua.domain.entity.Role;
import com.edu.zua.domain.entity.User;
import com.edu.zua.domain.entity.UserRole;
import com.edu.zua.domain.enums.AppHttpCodeEnum;
import com.edu.zua.domain.vo.EditUserDetailVO;
import com.edu.zua.domain.vo.PageVO;
import com.edu.zua.domain.vo.UserInfoVO;
import com.edu.zua.exception.SystemException;
import com.edu.zua.mapper.SysUserMapper;
import com.edu.zua.service.BlogUserService;
import com.edu.zua.service.RoleService;
import com.edu.zua.service.UserRoleService;
import com.edu.zua.utils.BeanCopyUtils;
import com.edu.zua.utils.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogUserServiceImpl extends ServiceImpl<SysUserMapper, User> implements BlogUserService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private RoleService roleService;

    /**
     * 查询用户详情信息
     * @return
     */
    @Override
    public ResponseResult userInfo() {
        // 获取当前用户id
        Long userId = SecurityUtil.getUserId();
        // 根据用户id查询用户信息
        User loignUser = getById(userId);
        // 封装成UserInfoVO
        UserInfoVO vo = BeanCopyUtils.copyBean(loignUser, UserInfoVO.class);
        return ResponseResult.okResult(vo);
    }

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    /**
     * 注册用户
     * @param user
     * @return
     */
    @Override
    public ResponseResult register(User user) {

        // 对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        // 对数据进行是否存在的判断
        if (userNameExist(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (emailExist(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        if (nickNameExist(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIIST);
        }
        // 对密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        save(user);

        // 存入数据库
        return ResponseResult.okResult();
    }

    @Override
    public PageVO listAllUser(UserDto userDto) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(userDto.getStatus()), User::getStatus, userDto.getStatus());
        queryWrapper.like(StringUtils.hasText(userDto.getUserName()), User::getUserName, userDto.getUserName());
        queryWrapper.like(StringUtils.hasText(userDto.getPhonenumber()), User::getPhonenumber, userDto.getPhonenumber());
        Page<User> page = new Page<>();
        page.setCurrent(userDto.getPageNum());
        page.setSize(userDto.getPageSize());
        page(page,queryWrapper);
        return new PageVO(page.getRecords(), page.getTotal());
    }

    @Override
    @Transactional
    public void addUser(AddUserDto addUserDto) {
        if (!StringUtils.hasText(addUserDto.getUserName())) throw new RuntimeException("用户名不能为空");
        // 存储新用户
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
        // 将新用户的角色关联关系也存储起来
        Long id = user.getId();
        List<Long> roleIds = addUserDto.getRoleIds();
        List<UserRole> userRoles = new ArrayList<>();
        for (Long roleId : roleIds) {
            userRoles.add(new UserRole(id, roleId));
        }
        userRoleService.saveBatch(userRoles);
    }

    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        return count(queryWrapper) > 0;
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName, nickName);
        return count(queryWrapper) > 0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, userName);
        return count(queryWrapper) > 0;
    }

    @Override
    public boolean checkUserNameUnique(String userName) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getUserName,userName))==0;
    }

    @Override
    public boolean checkPhoneUnique(AddUserDto user) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getPhonenumber,user.getPhonenumber()))==0;
    }

    @Override
    public boolean checkEmailUnique(AddUserDto user) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getEmail,user.getEmail()))==0;
    }

    @Override
    public EditUserDetailVO getUserInfo(Long id) {
        // 查出用户信息
        User user = getById(id);
        UserInfoVO userInfoVO = BeanCopyUtils.copyBean(user, UserInfoVO.class);
        // 查出所有的角色信息
        List<Role> roleList = roleService.list();
        // 查出该用户的角色id
        List<UserRole> userRoles = userRoleService.getUserRoles(id);
        List<Long> roleIds = userRoles.stream().map(userRole -> userRole.getRoleId())
                .collect(Collectors.toList());
        EditUserDetailVO editUserDetailVO = new EditUserDetailVO(roleIds, roleList, userInfoVO);
        return editUserDetailVO;
    }

    @Override
    @Transactional
    public void editUserDetail(AddUserDto editUserDto) {
        // 删除用户与角色关联
        LambdaQueryWrapper<UserRole> userRoleUpdateWrapper = new LambdaQueryWrapper<>();
        userRoleUpdateWrapper.eq(UserRole::getUserId,editUserDto.getId());
        userRoleService.remove(userRoleUpdateWrapper);
        // 存储用户-角色相关的信息
        List<UserRole> collect = editUserDto.getRoleIds().stream().map(roleId -> new UserRole(editUserDto.getId(), roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(collect);

        // 更新用户信息
        updateById(BeanCopyUtils.copyBean(editUserDto, User.class));
    }


}
