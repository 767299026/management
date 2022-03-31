package xyz.lsl.vue.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import xyz.lsl.vue.entity.Attribute;
import xyz.lsl.vue.service.AttributeService;
import xyz.lsl.vue.util.ResultUtil;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 属性表 前端控制器
 * </p>
 *
 * @author YIQU
 * @since 2022-03-28 21:54:01
 */
@RestController
@RequestMapping("/attribute")
public class AttributeController {

    @Resource
    private AttributeService attributeService;

    @RequiresPermissions("获取参数列表")
    @GetMapping("/{id}")
    public ResultUtil getAttribute(@PathVariable(name = "id") Integer id, @RequestParam String selected) {
        LambdaQueryWrapper<Attribute> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Attribute::getCatId, id).eq(Attribute::getAttrSel, selected);
        List<Attribute> attributes = attributeService.list(queryWrapper);
        return ResultUtil.success("获取该分类参数成功", attributes);
    }

    @RequiresPermissions("获取参数列表")
    @GetMapping("/attributeInfo/{id}")
    public ResultUtil getAttributeInfo(@PathVariable(name = "id") Integer id) {
        Attribute attribute = attributeService.getById(id);
        return ResultUtil.success("获取参数信息成功", attribute);
    }

    @RequiresPermissions("创建商品参数")
    @PostMapping("/add")
    public ResultUtil addAttribute(@RequestBody Attribute attribute) {
        attributeService.save(attribute);
        return ResultUtil.success("添加参数成功", null);
    }

    @RequiresPermissions("创建商品参数")
    @PutMapping("/edit")
    public ResultUtil editAttribute(@RequestBody Attribute attribute) {
        attributeService.saveOrUpdate(attribute);
        return ResultUtil.success("修改参数成功", null);
    }

    @RequiresPermissions("创建商品参数")
    @PutMapping("/editTag")
    public ResultUtil editAttributeTag(@RequestBody Attribute attribute) {
        attributeService.saveOrUpdate(attribute);
        return ResultUtil.success("修改参数信息成功", null);
    }

    @RequiresPermissions("删除商品参数")
    @DeleteMapping("/delete/{id}")
    public ResultUtil deleteAttribute(@PathVariable(name = "id") Integer id) {
        attributeService.removeById(id);
        return ResultUtil.success("删除参数成功", null);
    }
}
