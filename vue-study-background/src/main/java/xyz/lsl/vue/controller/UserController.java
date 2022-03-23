package xyz.lsl.vue.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import xyz.lsl.vue.common.vo.UserInfoVo;
import xyz.lsl.vue.common.vo.addUserVo;
import xyz.lsl.vue.common.vo.getUserListVo;
import xyz.lsl.vue.entity.User;
import xyz.lsl.vue.service.UserService;
import xyz.lsl.vue.util.ResultUtil;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author YIQU
 * @since 2022-03-16 11:31:18
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/users")
    public ResultUtil getUserList(@RequestParam(defaultValue = "1")Integer currentPage,
                                  @RequestParam(defaultValue = "4")Integer pageSize,
                                  String query){
        if(currentPage < 1 || pageSize < 1)
            return  ResultUtil.fail("参数不合法，非法攻击将被封禁 IP");
        List<getUserListVo> list;
        if(StringUtils.isNotBlank(query))
            list = userService.getUserListByUsername(query);
        else
            list = userService.getUserList();
        int size = list.size();
        Page page = new Page(currentPage,pageSize);
        if(size == 0)
            return ResultUtil.fail("数据库中没有用户");
        if (pageSize > size)
            pageSize = size;
        // 求出最大页数，防止currentPage越界
        int maxPage = size % pageSize == 0 ? size / pageSize : size / pageSize + 1;
        if (currentPage > maxPage)
            currentPage = maxPage;
        // 当前页第一条数据的下标
        int curIdx = currentPage > 1 ? (currentPage - 1) * pageSize : 0;
        List pageList = new ArrayList();
        // 将当前页的数据放进pageList
        for (int i = 0; i < pageSize && curIdx + i < size; i++)
            pageList.add(list.get(curIdx + i));
        page.setTotal(list.size()).setRecords(pageList);
        return ResultUtil.success("获取当前页用户信息成功",page);
    }

    @GetMapping("/getUserInfo/{id}")
    public ResultUtil getUserInfo(@PathVariable(name = "id")Integer id){
        UserInfoVo getUserInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userService.getById(id),getUserInfoVo);
        return ResultUtil.success("获取用户信息成功",getUserInfoVo);
    }

    @PutMapping("/changeStatus/{id}")
    public ResultUtil changeUserStatus(@PathVariable(name = "id")Integer id){
        if(id < 1)
            return  ResultUtil.fail("参数不合法，非法攻击将被封禁 IP");
        User user = userService.getById(id);
        Assert.notNull(user,"没有该用户，请联系管理员");
        if(user.getRole().equals("root"))
            return ResultUtil.fail("禁止禁用此用户");
        if (user.getStatus()==0)
            user.setStatus(1);
        else
            user.setStatus(0);
        userService.saveOrUpdate(user);
        return ResultUtil.success("更新用户状态成功",null);
    }

    @PostMapping("/add")
    public ResultUtil addUser(@RequestBody addUserVo addUserVo){
        User user = new User();
        addUserVo.setPassword(SecureUtil.md5(addUserVo.getPassword()));
        BeanUtils.copyProperties(addUserVo,user);
        if(addUserVo.isAdmin())
            user.setRole("root");
        else
            user.setRole("visitor");
        //默认头像
        user.setAvatar("https://cdn.jsdelivr.net/gh/767299026/CDN/blog/githubLogo.webp");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userService.save(user);
        return ResultUtil.success("新增用户成功",null);
    }

    @PutMapping("/edit")
    public ResultUtil editUser(@RequestBody UserInfoVo editUserVo){
        User user = userService.getById(editUserVo.getId());
        Assert.notNull(user);
        BeanUtil.copyProperties(editUserVo,user,"id");
        user.setUpdateTime(LocalDateTime.now());
        userService.saveOrUpdate(user);
        return ResultUtil.success("修改用户信息成功",null);
    }

    @DeleteMapping("/delete/{id}")
    public ResultUtil deleteUser(@PathVariable(name = "id")Integer id){
        userService.removeById(id);
        return ResultUtil.success("已移除该用户",null);
    }
}


