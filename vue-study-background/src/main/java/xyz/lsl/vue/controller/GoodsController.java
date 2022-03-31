package xyz.lsl.vue.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.lsl.vue.entity.Goods;
import xyz.lsl.vue.service.GoodsService;
import xyz.lsl.vue.util.FdfsUtil;
import xyz.lsl.vue.util.ResultUtil;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author YIQU
 * @since 2022-03-30 11:31:34
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Resource
    private GoodsService goodsService;

    @Resource
    private FdfsUtil fdfsUtil;

    @RequiresPermissions("商品列表")
    @GetMapping("/goods")
    public ResultUtil getGoods(@RequestParam(defaultValue = "1") Integer currentPage,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               String query){
        if (currentPage < 1 || pageSize < 1)
            return ResultUtil.fail("参数不合法，非法攻击将被封禁 IP");
        List<Goods> list = new LinkedList<>();
        if (StringUtils.isNotBlank(query))
            list = goodsService.getGoods(query);
        else {
            LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper
                    .select(Goods::getGoodsId,Goods::getGoodsName,Goods::getGoodsPrice,Goods::getGoodsNumber,Goods::getGoodsWeight,
                            Goods::getCreateTime,Goods::getUpdateTime,Goods::getHotNumber,Goods::getIsPromote,Goods::getGoodsState)
                    .orderByAsc(Goods::getCreateTime);
            list = goodsService.list(queryWrapper);
        }
        int size = list.size();
        Page page = new Page(currentPage,pageSize);
        if(size == 0)
            return ResultUtil.fail("数据库中没有商品");
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
        return ResultUtil.success("获取当前页商品信息成功",page);
    }

    @RequiresPermissions("添加商品")
    @PostMapping("/add")
    public ResultUtil addGoods(@RequestBody Goods goods){
        goodsService.save(goods);
        return ResultUtil.success("添加商品成功",null);
    }

    @RequiresPermissions("删除商品")
    @DeleteMapping("/delete/{id}")
    public ResultUtil deleteGoods(@PathVariable(name = "id")Integer id){
        goodsService.removeById(id);
        return ResultUtil.success("删除商品成功",null);
    }

    @RequiresPermissions("更新商品图片")
    @PostMapping("/upload")
    @ResponseBody
    public ResultUtil uploadFile(MultipartFile file) {
        String filePath = null;
        try {
            filePath = fdfsUtil.uploadFile(file);
        } catch (IOException e) {
            return ResultUtil.fail("上传文件失败");
        }
        String path = "xian-yun.xyz/"+filePath;
        return ResultUtil.success("上传成功",path);
    }
}
