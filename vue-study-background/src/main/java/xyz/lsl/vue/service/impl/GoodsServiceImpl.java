package xyz.lsl.vue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.lsl.vue.entity.Goods;
import xyz.lsl.vue.mapper.GoodsMapper;
import xyz.lsl.vue.service.GoodsService;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author YIQU
 * @since 2022-03-30 12:07:26
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Resource
    private GoodsMapper goodsMapper;

    @Override
    public List<Goods> getGoods(String query) {
        return goodsMapper.getGoodsByGoodsName(query);
    }
}
