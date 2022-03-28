package xyz.lsl.vue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.lsl.vue.common.vo.goodsVo.GoodsCategoriesVo;
import xyz.lsl.vue.entity.Category;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author YIQU
 * @since 2022-03-26 20:47:00
 */
public interface CategoryService extends IService<Category> {

    List<GoodsCategoriesVo> categories(Integer type);
}
