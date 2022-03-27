package xyz.lsl.vue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.lsl.vue.common.vo.roleVo.getRolesVo;
import xyz.lsl.vue.entity.Role;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author YIQU
 * @since 2022-03-24 20:03:48
 */
public interface RoleService extends IService<Role> {

    List<getRolesVo> getRoles();

}
