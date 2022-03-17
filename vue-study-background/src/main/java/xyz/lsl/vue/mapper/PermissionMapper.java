package xyz.lsl.vue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import xyz.lsl.vue.common.vo.getMenusVo;
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
    List<getMenusVo> getLevelOne();
    //获取二级左侧菜单
    List<getMenusVo> getLevelTwo(@Param("ps_pid") String ps_pid);
}
