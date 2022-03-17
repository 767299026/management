package xyz.lsl.vue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.lsl.vue.common.vo.getMenusVo;
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
        for(getMenusVo menusVo : menusVoList) {
            menusVo.setChildren(permissionMapper.getLevelTwo(menusVo.getId().toString()));//填充children二级菜单
        }
        return menusVoList;
    }


}
