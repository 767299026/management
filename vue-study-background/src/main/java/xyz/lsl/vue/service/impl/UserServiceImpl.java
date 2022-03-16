package xyz.lsl.vue.service.impl;

import xyz.lsl.vue.entity.User;
import xyz.lsl.vue.mapper.UserMapper;
import xyz.lsl.vue.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author YIQU
 * @since 2022-03-16 11:31:18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
