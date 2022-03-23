package xyz.lsl.vue.controller;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.lsl.vue.common.dto.LoginDto;
import xyz.lsl.vue.entity.User;
import xyz.lsl.vue.service.UserService;
import xyz.lsl.vue.util.JWTUtil;
import xyz.lsl.vue.util.ResultUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AccountController {

    @Resource
    UserService userService;

    @Resource
    JWTUtil jwtUtil;

    @PostMapping("/login")
    public ResultUtil login(@RequestBody LoginDto loginDto, HttpServletResponse response){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,loginDto.getUsername());
        User user = userService.getOne(queryWrapper);
        Assert.notNull(user,"没有该用户");
        if(!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword())))
            return ResultUtil.fail("密码错误");
        String token = jwtUtil.generateToken(user.getId(),user.getUsername());
        response.setHeader("Authorization", token);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        return ResultUtil.success("登录成功",user);
    }
}
