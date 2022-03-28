package xyz.lsl.vue.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import xyz.lsl.vue.common.vo.goodsVo.CategoryInfoVo;
import xyz.lsl.vue.common.vo.goodsVo.GoodsCategoriesVo;
import xyz.lsl.vue.entity.Category;
import xyz.lsl.vue.service.CategoryService;
import xyz.lsl.vue.util.ResultUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author YIQU
 * @since 2022-03-26 20:47:00
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/cate")
    public ResultUtil getGoodsCategories(Integer currentPage,
                                         Integer pageSize,
                                         Integer type) {
        if (currentPage != null && pageSize != null) {
            if (currentPage < 1 || pageSize < 1 || (type != null && (type < 1 || type > 3)))
                return ResultUtil.fail("参数不合法，非法攻击将被封禁 IP");
            List<GoodsCategoriesVo> list = categoryService.categories(type);
            int size = list.size();
            Page page = new Page(currentPage, pageSize);
            if (size == 0)
                return ResultUtil.fail("数据库中没有分类");
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
            return ResultUtil.success("获取商品分类成功", page);
        } else {
            if (type != null && (type < 1 || type > 3))
                return ResultUtil.fail("参数不合法，非法攻击将被封禁 IP");
            return ResultUtil.success("获取商品分类成功", categoryService.categories(type));
        }
    }

    @GetMapping("/cate/{id}")
    public ResultUtil getCategory(@PathVariable(name = "id") Integer id) {
        Category category = categoryService.getById(id);
        Assert.notNull(category, "没有该分类");
        CategoryInfoVo categoriesVo = new CategoryInfoVo();
        categoriesVo.setCategoryId(id);
        categoriesVo.setCategoryName(category.getCatName());
        categoriesVo.setCategoryLevel(category.getCatLevel());
        if (category.getCatDeleted() == 0)
            categoriesVo.setCategoryStatus(false);
        else
            categoriesVo.setCategoryStatus(true);
        if (category.getCatLevel() == 0)
            categoriesVo.setParentId(new Integer[]{0});
        else if (category.getCatLevel() == 1)
            categoriesVo.setParentId(new Integer[]{category.getCatPid()});
        else {
            Category father = categoryService.getById(category.getCatPid());
            categoriesVo.setParentId(new Integer[]{father.getCatPid(), category.getCatPid()});
        }
        return ResultUtil.success("获取分类信息成功", categoriesVo);
    }

    @PostMapping("/add")
    public ResultUtil addCategory(@RequestBody GoodsCategoriesVo addCategoryVo) {
        Category category = new Category();
        category.setCatName(addCategoryVo.getCategoryName());
        category.setCatPid(addCategoryVo.getParentId());
        category.setCatLevel(addCategoryVo.getCategoryLevel());
        if (!addCategoryVo.isCategoryStatus())
            category.setCatDeleted(0);
        else
            category.setCatDeleted(1);
        categoryService.save(category);
        return ResultUtil.success("添加分类成功", null);
    }

    @PutMapping("/edit")
    public ResultUtil editCategory(@RequestBody CategoryInfoVo editCategoryVo) {
        Category category = categoryService.getById(editCategoryVo.getCategoryId());
        Assert.notNull(category, "没有该分类");
        if (category.getCatLevel() != editCategoryVo.getCategoryLevel())
            return ResultUtil.fail("不能改变分类的等级");
        category.setCatName(editCategoryVo.getCategoryName());
        category.setCatPid(editCategoryVo.getParentId()[editCategoryVo.getParentId().length - 1]);
        category.setCatLevel(editCategoryVo.getCategoryLevel());
        if (!editCategoryVo.isCategoryStatus())
            category.setCatDeleted(0);
        else
            category.setCatDeleted(1);
        categoryService.saveOrUpdate(category);
        return ResultUtil.success("修改分类成功", null);
    }

    @DeleteMapping("/delete/{id}")
    public ResultUtil deleteCategory(@PathVariable(name = "id") Integer id) {
        Category category = categoryService.getById(id);
        if (category.getCatLevel() == 2) {
            categoryService.removeById(id);
            return ResultUtil.success("删除分类成功", null);
        }
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getCatPid, id);
        if (category.getCatLevel() == 1) {//先获取三级分类
            List<Category> Two = categoryService.list(queryWrapper);
            for (Category two : Two) {//遍历三级子分类删除
                categoryService.removeById(two.getCatId());
            }
            categoryService.removeById(id);
        } else {//先获取二级分类
            List<Category> One = categoryService.list(queryWrapper);
            queryWrapper = new LambdaQueryWrapper<>();
            for (Category one : One) {
                queryWrapper.eq(Category::getCatPid, one.getCatPid());
                List<Category> Two = categoryService.list(queryWrapper);
                for (Category two : Two) {
                    categoryService.removeById(two.getCatId());
                }
                categoryService.removeById(one.getCatId());
            }
            categoryService.removeById(id);
        }
        return ResultUtil.success("删除分类成功", null);
    }
}
