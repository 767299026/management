package xyz.lsl.vue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import xyz.lsl.vue.common.vo.userVo.getUserListVo;
import xyz.lsl.vue.entity.User;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author YIQU
 * @since 2022-03-18 13:13:49
 */
public interface UserMapper extends BaseMapper<User> {

    /*获取用户列表*/
    List<getUserListVo> getUserList();

    /*根据用户名查询用户*/
    List<getUserListVo> getUserListByUsername(@Param("username") String username);
}
