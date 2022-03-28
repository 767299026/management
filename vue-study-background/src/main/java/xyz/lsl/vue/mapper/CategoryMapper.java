package xyz.lsl.vue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import xyz.lsl.vue.common.vo.goodsVo.GoodsCategoriesVo;
import xyz.lsl.vue.entity.Category;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author YIQU
 * @since 2022-03-26 20:47:00
 */
public interface CategoryMapper extends BaseMapper<Category> {

    /*获取一级分类*/
    List<GoodsCategoriesVo> getLevelZero();

    /*获取二级分类*/
    List<GoodsCategoriesVo.goodsCategories> getLevelOne(@Param("pid") Integer categoryId);

    /*获取二级分类*/
    List<GoodsCategoriesVo.goodsCategories.children> getLevelTwo(@Param("pid") Integer categoryId);
}
