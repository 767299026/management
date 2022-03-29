package xyz.lsl.vue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.lsl.vue.common.vo.goodsVo.GoodsCategoriesVo;
import xyz.lsl.vue.entity.Category;
import xyz.lsl.vue.mapper.CategoryMapper;
import xyz.lsl.vue.service.CategoryService;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author YIQU
 * @since 2022-03-26 20:47:00
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public List<GoodsCategoriesVo> categories(Integer type) {
        List<GoodsCategoriesVo> levelZero = categoryMapper.getLevelZero();//一级分类
        List<GoodsCategoriesVo.goodsCategories> levelOne;//二级分类
        if (type == null || type == 3) {
            for (GoodsCategoriesVo zero : levelZero) {
                levelOne = categoryMapper.getLevelOne(zero.getCategoryId());
                for (GoodsCategoriesVo.goodsCategories one : levelOne) {
                    one.setChildren(categoryMapper.getLevelTwo(one.getCategoryId()));//三级分类
                }
                zero.setChildren(levelOne);
            }
        } else if (type == 1)
            return levelZero;
        else {
            for (GoodsCategoriesVo zero : levelZero) {
                levelOne = categoryMapper.getLevelOne(zero.getCategoryId());
                zero.setChildren(levelOne);
            }
        }
        return levelZero;
    }
}
