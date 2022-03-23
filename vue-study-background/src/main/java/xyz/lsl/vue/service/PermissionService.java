package xyz.lsl.vue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import xyz.lsl.vue.common.vo.getMenusVo;
import xyz.lsl.vue.entity.Permission;

import java.util.List;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author YIQU
 * @since 2022-03-17 12:16:25
 */
@Transactional
public interface PermissionService extends IService<Permission> {

    List<getMenusVo> getMenus();
}
