package xyz.lsl.vue.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.lsl.vue.common.vo.permissionVo.getRightsTreeVo;
import xyz.lsl.vue.common.vo.roleVo.getRolesVo;
import xyz.lsl.vue.entity.Role;
import xyz.lsl.vue.mapper.PermissionMapper;
import xyz.lsl.vue.mapper.RoleMapper;
import xyz.lsl.vue.service.RoleService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author YIQU
 * @since 2022-03-24 20:03:48
 */
@SuppressWarnings("all")
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    PermissionMapper permissionMapper;

    @Override
    public List<getRolesVo> getRoles() {
        List<getRolesVo> roles = new LinkedList<>();
        List<String> level1 = permissionMapper.getAllPermission(1);//数据库全部一级权限
        List<String> level2 = permissionMapper.getAllPermission(2);//数据库全部二级权限
        List<String> level3 = permissionMapper.getAllPermission(3);//数据库全部三级权限
        List<Role> roleList = roleMapper.selectList(new LambdaQueryWrapper<Role>().select(Role::getRoleId, Role::getRoleName, Role::getPsIds, Role::getRoleDesc));
        for (Role role : roleList) {
            List<String> permissionOne = new LinkedList<>();//该用户拥有的一级权限
            List<String> permissionTwo = new LinkedList<>();//该用户拥有的二级权限
            List<String> permissionThree = new LinkedList<>();//该用户拥有的三级权限
            List<String> psIds = Arrays.asList(role.getPsIds().split(","));//该用户的权限
            for (String s : level1) {//判断该用户拥有的一级权限
                if (psIds.contains(s))
                    permissionOne.add(s);
            }
            for (String s : level2) {//判断该用户拥有的二级权限
                if (psIds.contains(s))
                    permissionTwo.add(s);
            }
            for (String s : level3) {//判断该用户拥有的三级权限
                if (psIds.contains(s))
                    permissionThree.add(s);
            }
            getRolesVo getRolesVo = new getRolesVo();
            BeanUtil.copyProperties(role, getRolesVo, "psIds", "psCa");
            if (permissionOne.size() == 0 || permissionTwo.size() == 0) //没有任何权限
                roles.add(getRolesVo);
            else {
                List<getRightsTreeVo> tops = permissionMapper.getPermissionTops(permissionOne);//获取一级权限
                for (getRightsTreeVo top : tops) {//遍历一级权限
                    List<getRightsTreeVo.permission> permissions = permissionMapper.getPermissions(permissionTwo, top.getId());//获取二级权限
                    for (getRightsTreeVo.permission permission : permissions) {//遍历二级权限
                        permission.setChildren(permissionMapper.getChildren(permissionThree, permission.getId()));//获取并填充三级权限
                    }
                    top.setChildren(permissions);//填充二级权限
                }
                getRolesVo.setChildren(tops);
                roles.add(getRolesVo);
            }
        }
        return roles;
    }
}
