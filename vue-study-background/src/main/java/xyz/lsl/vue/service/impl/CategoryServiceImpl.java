package xyz.lsl.vue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.lsl.vue.common.vo.goodsVo.getGoodsCategoriesVo;
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
    public List<getGoodsCategoriesVo> categories() {
        return null;
    }
}
