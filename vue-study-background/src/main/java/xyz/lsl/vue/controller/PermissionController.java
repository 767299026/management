package xyz.lsl.vue.controller;


import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.lsl.vue.service.PermissionService;
import xyz.lsl.vue.util.ResultUtil;

import javax.annotation.Resource;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author YIQU
 * @since 2022-03-17 12:16:25
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    @GetMapping("/menus")
    public ResultUtil getMenus() {
        return ResultUtil.success("获取菜单列表成功", permissionService.getMenus());
    }

    @RequiresPermissions("查看权限")
    @GetMapping("/rights/list")
    public ResultUtil getRightsList() {
        return ResultUtil.success("获取权限列表成功", permissionService.getRightsList());
    }

    @RequiresPermissions("查看权限")
    @GetMapping("/rights/tree")
    public ResultUtil getRightsTree() {
        return ResultUtil.success("获取权限列表成功", permissionService.getRightsTree());
    }
}
