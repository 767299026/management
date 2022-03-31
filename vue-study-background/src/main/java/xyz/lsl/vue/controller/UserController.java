package xyz.lsl.vue.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import xyz.lsl.vue.common.vo.userVo.UserInfoVo;
import xyz.lsl.vue.common.vo.userVo.UserListVo;
import xyz.lsl.vue.common.vo.userVo.addUserVo;
import xyz.lsl.vue.common.vo.userVo.allotRoleVo;
import xyz.lsl.vue.entity.Role;
import xyz.lsl.vue.entity.User;
import xyz.lsl.vue.service.RoleService;
import xyz.lsl.vue.service.UserService;
import xyz.lsl.vue.util.ResultUtil;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author YIQU
 * @since 2022-03-16 11:31:18
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @RequiresPermissions("用户列表")
    @GetMapping("/users")
    public ResultUtil getUserList(@RequestParam(defaultValue = "1") Integer currentPage,
                                  @RequestParam(defaultValue = "4") Integer pageSize,
                                  String query) {
        if (currentPage < 1 || pageSize < 1)
            return ResultUtil.fail("参数不合法，非法攻击将被封禁 IP");
        List<UserListVo> list = new LinkedList<>();
        if (StringUtils.isNotBlank(query))
            list = userService.getUserListByUsername(query);
        else {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.orderByAsc(User::getCreateTime);
            List<User> users = userService.list(queryWrapper);
            for (User user : users) {
                UserListVo userVo = new UserListVo();
                BeanUtils.copyProperties(user, userVo);
                list.add(userVo);
            }
        }
        int size = list.size();
        Page page = new Page(currentPage,pageSize);
        if(size == 0)
            return ResultUtil.fail("数据库中没有用户");
        if (pageSize > size)
            pageSize = size;
        // 求出最大页数，防止currentPage越界
        int maxPage = size % pageSize == 0 ? size / pageSize : size / pageSize + 1;
        if (currentPage > maxPage)
            currentPage = maxPage;
        // 当前页第一条数据的下标
        int curIdx = currentPage > 1 ? (currentPage - 1) * pageSize : 0;
        List pageList = new ArrayList();
        // 将当前页的数据放进pageList
        for (int i = 0; i < pageSize && curIdx + i < size; i++)
            pageList.add(list.get(curIdx + i));
        page.setTotal(list.size()).setRecords(pageList);
        return ResultUtil.success("获取当前页用户信息成功",page);
    }

    @RequiresPermissions("获取用户详情")
    @GetMapping("/getUserInfo/{id}")
    public ResultUtil getUserInfo(@PathVariable(name = "id")Integer id){
        UserInfoVo getUserInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userService.getById(id),getUserInfoVo);
        return ResultUtil.success("获取用户信息成功",getUserInfoVo);
    }

    @RequiresPermissions("设置管理状态")
    @PutMapping("/changeStatus/{id}")
    public ResultUtil changeUserStatus(@PathVariable(name = "id")Integer id){
        if(id < 1)
            return  ResultUtil.fail("参数不合法，非法攻击将被封禁 IP");
        User user = userService.getById(id);
        Assert.notNull(user,"没有该用户，请联系管理员");
        if (user.getStatus()==0)
            user.setStatus(1);
        else
            user.setStatus(0);
        user.setUpdateTime(LocalDateTime.now());
        userService.saveOrUpdate(user);
        return ResultUtil.success("更新用户状态成功",null);
    }

    @RequiresPermissions("添加用户")
    @PostMapping("/add")
    public ResultUtil addUser(@RequestBody addUserVo addUserVo) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, addUserVo.getUsername());
        User user = userService.getOne(queryWrapper);
        if (user != null)
            return ResultUtil.fail("用户名已存在");
        user = new User();
        addUserVo.setPassword(SecureUtil.md5(addUserVo.getPassword()));
        BeanUtils.copyProperties(addUserVo, user);
        //默认无角色
        user.setRole("null");
        //默认头像
        user.setAvatar("https://cdn.jsdelivr.net/gh/767299026/CDN/blog/githubLogo.webp");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userService.save(user);
        return ResultUtil.success("新增用户成功", null);
    }

    @RequiresPermissions("更新用户")
    @PutMapping("/edit")
    public ResultUtil editUser(@RequestBody UserInfoVo editUserVo){
        User user = userService.getById(editUserVo.getId());
        Assert.notNull(user, "用户不存在，请重新获取");
        BeanUtil.copyProperties(editUserVo, user, "id");
        user.setUpdateTime(LocalDateTime.now());
        userService.saveOrUpdate(user);
        return ResultUtil.success("修改用户信息成功", null);
    }

    @RequiresPermissions("删除用户")
    @DeleteMapping("/delete/{id}")
    public ResultUtil deleteUser(@PathVariable(name = "id") Integer id) {
        userService.removeById(id);
        return ResultUtil.success("已移除该用户", null);
    }

    @RequiresPermissions("分配用户角色")
    @PutMapping("/allotRole")
    public ResultUtil allotRole(@RequestBody allotRoleVo allotRoleVo) {
        User user = allotRoleVo.getUserInfo();
        Role role = roleService.getById(allotRoleVo.getRoleId());
        Assert.notNull(role, "该角色不存在，请重新获取");
        user.setRole(role.getRoleName());
        user.setUpdateTime(LocalDateTime.now());
        userService.saveOrUpdate(user);
        return ResultUtil.success("分配角色权限成功", null);
    }
}


