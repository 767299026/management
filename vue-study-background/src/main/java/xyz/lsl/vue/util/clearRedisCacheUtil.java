package xyz.lsl.vue.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.lsl.vue.entity.User;
import xyz.lsl.vue.service.UserService;

import javax.annotation.Resource;
import java.util.List;

@Component
@Slf4j
public class clearRedisCacheUtil {

    @Resource UserService userService;

    @Resource RedisUtil redisUtil;

    /**
     * 重新赋值权限
     */
    public void reloadAuthorizing(String role){
        List<User> users = userService.list(new LambdaQueryWrapper<User>().eq(User::getRole,role));
        String key = "shiro:cache:xyz.lsl.vue.shiro.AccountRealm.authorizationCache:";
        for (User user : users) {
            String shiroKey = key + user.getId().toString();
            if(redisUtil.hasKey(shiroKey))
                redisUtil.delete(shiroKey);
        }
    }
}
