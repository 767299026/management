package xyz.lsl.vue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.lsl.vue.common.vo.getUserListVo;
import xyz.lsl.vue.entity.User;
import xyz.lsl.vue.mapper.UserMapper;
import xyz.lsl.vue.service.UserService;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author YIQU
 * @since 2022-03-18 13:13:49
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public List<getUserListVo> getUserList() {
        return userMapper.getUserList();
    }

    @Override
    public List<getUserListVo> getUserListByUsername(String username) {
        return userMapper.getUserListByUsername(username);
    }
}
