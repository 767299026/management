package xyz.lsl.vue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import xyz.lsl.vue.common.vo.homeVo.MenusVo;
import xyz.lsl.vue.common.vo.permissionVo.RightsListVo;
import xyz.lsl.vue.common.vo.permissionVo.RightsTreeVo;
import xyz.lsl.vue.entity.Permission;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author YIQU
 * @since 2022-03-17 12:16:25
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    //获取一级左侧菜单
    List<MenusVo> getLevelOne();

    //获取二级左侧菜单
    List<MenusVo> getLevelTwo(@Param("ps_pid") String ps_pid);

    //获取权限列表
    List<RightsListVo> getRightsList();

    //获取所有权限
    List<String> getAllPermission(@Param("level") Integer level);

    //获取一级权限
    List<RightsTreeVo> getPermissionTops(@Param("tops") List<String> tops);

    //获取二级权限
    List<RightsTreeVo.permission> getPermissions(@Param("permissions") List<String> permissions, @Param("topId") Integer id);

    //获取三级权限
    List<RightsTreeVo.permission.children> getChildren(@Param("children") List<String> children, @Param("permissionId") Integer id);
}
