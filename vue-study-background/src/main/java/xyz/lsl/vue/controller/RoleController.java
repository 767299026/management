package xyz.lsl.vue.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import xyz.lsl.vue.common.vo.roleVo.RoleInfoVo;
import xyz.lsl.vue.entity.Role;
import xyz.lsl.vue.entity.User;
import xyz.lsl.vue.service.RoleService;
import xyz.lsl.vue.service.UserService;
import xyz.lsl.vue.util.ResultUtil;
import xyz.lsl.vue.util.clearRedisCacheUtil;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author YIQU
 * @since 2022-03-24 20:03:48
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @Resource
    private UserService userService;

    @Resource clearRedisCacheUtil clearRedisCacheUtil;

    @RequiresPermissions("获取角色列表")
    @GetMapping("/roles")
    public ResultUtil getRoles() {
        return ResultUtil.success("获取角色列表成功", roleService.getRoles());
    }

    @GetMapping("/rolesName")
    public ResultUtil getRolesName() {
        return ResultUtil.success("获取角色列表成功", roleService.list(new LambdaQueryWrapper<Role>().select(Role::getRoleId, Role::getRoleName)));
    }

    @RequiresPermissions("获取角色详情")
    @GetMapping("/getRoleInfo/{id}")
    public ResultUtil getRoleInfo(@PathVariable(name = "id") Integer id) {
        RoleInfoVo getRoleInfoVo = new RoleInfoVo();
        BeanUtils.copyProperties(roleService.getById(id), getRoleInfoVo);
        return ResultUtil.success("获取角色信息成功", getRoleInfoVo);
    }

    @RequiresPermissions("添加角色")
    @PostMapping("/add")
    public ResultUtil addRole(@RequestBody Role addRoleVo) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getRoleName, addRoleVo.getRoleName());
        Role role = roleService.getOne(queryWrapper);
        if (role != null)
            return ResultUtil.fail("该角色已存在");
        roleService.save(addRoleVo);
        return ResultUtil.success("新增角色成功", null);
    }

    @RequiresPermissions("更新角色信息")
    @PutMapping("/edit")
    public ResultUtil editRole(@RequestBody Role editRoleVo) {
        Role role = roleService.getById(editRoleVo.getRoleId());
        if(!role.getRoleName().equals(editRoleVo.getRoleName())) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getRole, role.getRoleName());
            List<User> users = userService.list(queryWrapper);
            if (users.size() > 0) {
                for (User user : users) {//将分配了该角色的用户重置角色
                    user.setRole(editRoleVo.getRoleName());
                    user.setUpdateTime(LocalDateTime.now());
                    userService.saveOrUpdate(user);
                }
            }
        }
        roleService.saveOrUpdate(editRoleVo);
        return ResultUtil.success("修改角色信息成功", null);
    }

    @RequiresPermissions("删除角色")
    @DeleteMapping("/delete/{id}")
    public ResultUtil deleteRole(@PathVariable(name = "id") Integer id) {
        Role role = roleService.getById(id);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getRole, role.getRoleName());
        List<User> users = userService.list(queryWrapper);
        if (users.size() > 0) {
            for (User user : users) {//将分配了该角色的用户重置角色
                user.setRole("null");
                user.setUpdateTime(LocalDateTime.now());
                userService.saveOrUpdate(user);
            }
        }
        roleService.removeById(id);
        return ResultUtil.success("已移除该角色", null);
    }

    @RequiresPermissions({"角色授权","更新角色权限"})
    @PutMapping("/allot/{id}")
    public ResultUtil allotPermission(@PathVariable(name = "id") Integer id, @RequestBody String rights) {
        JSONObject json = JSONObject.parseObject(rights);//这里没有用对象接收，要解析json串
        String ps_Ids = (String) json.get("rights");
        Role role = roleService.getById(id);
        Assert.notNull(role, "角色不存在,请重新获取");
        role.setPsIds(ps_Ids);
        roleService.saveOrUpdate(role);
        clearRedisCacheUtil.reloadAuthorizing(role.getRoleName());
        return ResultUtil.success("更新角色权限成功", null);
    }
}
