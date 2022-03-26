package xyz.lsl.vue.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import xyz.lsl.vue.common.vo.RoleInfo;
import xyz.lsl.vue.entity.Role;
import xyz.lsl.vue.entity.User;
import xyz.lsl.vue.service.RoleService;
import xyz.lsl.vue.service.UserService;
import xyz.lsl.vue.util.ResultUtil;

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

    @GetMapping("/roles")
    public ResultUtil getRoles() {
        return ResultUtil.success("获取角色列表成功", roleService.getRoles());
    }

    @GetMapping("/rolesName")
    public ResultUtil getRolesName() {
        return ResultUtil.success("获取角色列表成功", roleService.list(new LambdaQueryWrapper<Role>().select(Role::getRoleId, Role::getRoleName)));
    }

    @GetMapping("/getRoleInfo/{id}")
    public ResultUtil getRoleInfo(@PathVariable(name = "id") Integer id) {
        RoleInfo getRoleInfo = new RoleInfo();
        BeanUtils.copyProperties(roleService.getById(id), getRoleInfo);
        return ResultUtil.success("获取角色信息成功", getRoleInfo);
    }

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

    @PutMapping("/edit")
    public ResultUtil editRole(@RequestBody Role editUserVo) {
        roleService.saveOrUpdate(editUserVo);
        return ResultUtil.success("修改角色信息成功", null);
    }

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

    @PutMapping("/allot/{id}")
    public ResultUtil allotPermission(@PathVariable(name = "id") Integer id, @RequestBody String rights) {
        JSONObject json = JSONObject.parseObject(rights);//这里没有用对象接收，要解析json串
        String ps_Ids = (String) json.get("rights");
        Role role = roleService.getById(id);
        Assert.notNull(role, "角色不存在,请重新获取");
        role.setPsIds(ps_Ids);
        roleService.saveOrUpdate(role);
        return ResultUtil.success("更新角色权限成功", null);
    }
}
