package xyz.lsl.vue.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import xyz.lsl.vue.entity.Role;
import xyz.lsl.vue.entity.User;
import xyz.lsl.vue.mapper.PermissionMapper;
import xyz.lsl.vue.service.RoleService;
import xyz.lsl.vue.service.UserService;
import xyz.lsl.vue.util.JWTUtil;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class AccountRealm extends AuthorizingRealm {

    @Resource
    private JWTUtil jwtUtil;

    @Resource
    private UserService userService;

    @Resource PermissionMapper permissionMapper;
    
    @Resource RoleService roleService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("执行doGetAuthorizationInfo方法进行授权");
//        String username = JwtUtil.getUsername(principalCollection.toString());
        log.info(principals.toString());
//        log.info("登录的用户:" + username);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        AccountProfile accountProfile = (AccountProfile)principals.getPrimaryPrincipal();
        String roles = accountProfile.getRole();
        Role role = roleService.getOne(new LambdaQueryWrapper<Role>().eq(Role::getRoleName,roles));
        Assert.notNull(role,"用户授权的角色不存在,请重新登录");
        info.addRole(roles);
        log.info("当前角色: "+ roles);
        List<String> level2 = permissionMapper.getAllPermission(2);//数据库全部二级权限
        List<String> level3 = permissionMapper.getAllPermission(3);//数据库全部三级权限
        List<String> psIds = Arrays.asList(role.getPsIds().split(","));//该用户的权限
        for (String two : level2) {//判断该角色拥有的三级权限
            if (psIds.contains(two)){
                String permission = permissionMapper.getAllPermissionName(Integer.valueOf(two));
                info.addStringPermission(permission);
                log.info("授予了"+permission+"权限");
            }
        }
        for (String three : level3) {//判断该角色拥有的三级权限
            if (psIds.contains(three)){
                String permission = permissionMapper.getAllPermissionName(Integer.valueOf(three));
                info.addStringPermission(permission);
                log.info("授予了"+permission+"权限");
            }
        }
        return info;
    }

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("执行doGetAuthenticationInfo方法进行认证");
        JwtToken jwt = (JwtToken) token;
        String userId = (String) jwtUtil.getClaimByToken((String) jwt.getPrincipal()).get("userId");
        String username = (String) jwtUtil.getClaimByToken((String) jwt.getPrincipal()).get("username");
        User user = userService.getById(Long.parseLong(userId));
        if (user == null) {
            throw new UnknownAccountException("账户不存在！");
        }
        if (user.getStatus() == 0) {
            throw new LockedAccountException("账户已被锁定！");
        }
        if(!user.getUsername().equals(username)){
            throw new UnknownAccountException("userId与username不一致");
        }
        AccountProfile profile = new AccountProfile();
        //知道它的身份 principals
        BeanUtil.copyProperties(user, profile);
        log.info("认证信息-->" + profile.toString());
        return new SimpleAuthenticationInfo(profile, jwt.getCredentials(), getName());
    }

}
