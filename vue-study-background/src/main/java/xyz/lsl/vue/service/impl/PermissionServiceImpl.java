package xyz.lsl.vue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.lsl.vue.common.vo.homeVo.getMenusVo;
import xyz.lsl.vue.common.vo.permissionVo.getRightsListVo;
import xyz.lsl.vue.common.vo.permissionVo.getRightsTreeVo;
import xyz.lsl.vue.entity.Permission;
import xyz.lsl.vue.mapper.PermissionMapper;
import xyz.lsl.vue.service.PermissionService;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author YIQU
 * @since 2022-03-17 12:16:25
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public List<getMenusVo> getMenus() {
        List<getMenusVo> menusVoList = permissionMapper.getLevelOne();//获取一级菜单
        for (getMenusVo menusVo : menusVoList) {
            menusVo.setChildren(permissionMapper.getLevelTwo(menusVo.getId().toString()));//填充children二级菜单
        }
        return menusVoList;
    }

    @Override
    public List<getRightsListVo> getRightsList() {
        return permissionMapper.getRightsList();
    }

    @Override
    public List<getRightsTreeVo> getRightsTree() {
        List<String> level1 = permissionMapper.getAllPermission(1);//数据库全部一级权限
        List<String> level2 = permissionMapper.getAllPermission(2);//数据库全部二级权限
        List<String> level3 = permissionMapper.getAllPermission(3);//数据库全部三级权限
        List<getRightsTreeVo> tops = permissionMapper.getPermissionTops(level1);
        for (getRightsTreeVo top : tops) {//遍历一级权限
            List<getRightsTreeVo.permission> permissions = permissionMapper.getPermissions(level2, top.getId());//获取二级权限
            for (getRightsTreeVo.permission permission : permissions) {//遍历二级权限
                permission.setChildren(permissionMapper.getChildren(level3, permission.getId()));//获取并填充三级权限
            }
            top.setChildren(permissions);//填充二级权限
        }
        return tops;
    }
}
