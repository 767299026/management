package xyz.lsl.vue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xyz.lsl.vue.common.vo.goodsVo.getGoodsCategoriesVo;
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
    List<getGoodsCategoriesVo> getLevelZero();
    /**/
}
