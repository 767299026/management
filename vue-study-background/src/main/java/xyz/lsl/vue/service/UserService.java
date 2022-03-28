package xyz.lsl.vue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import xyz.lsl.vue.common.vo.userVo.UserListVo;
import xyz.lsl.vue.entity.User;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YIQU
 * @since 2022-03-18 13:13:49
 */
@Transactional
public interface UserService extends IService<User> {

    List<UserListVo> getUserList();

    List<UserListVo> getUserListByUsername(String username);
}
